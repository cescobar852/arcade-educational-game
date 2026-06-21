package com.arcade.service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * Loads and plays the background music playlist for the application.
 */
public class AudioService {

    private static final Logger LOGGER = Logger.getLogger(AudioService.class.getName());
    private static final String PLAYLIST_RESOURCE_DIRECTORY = "audio/8-Bit jingles";
    private static final String AUDIO_EXTENSION = ".mp3";

    private final ClassLoader classLoader;
    private final List<String> playlistResources;

    private MediaPlayer currentPlayer;
    private int currentTrackIndex;
    private int failureStreak;
    private boolean playlistLoaded;
    private boolean shutdownRequested;

    /**
     * Creates an audio service that uses the default application class loader.
     */
    public AudioService() {
        this(AudioService.class.getClassLoader());
    }

    /**
     * Creates an audio service that uses the provided class loader.
     *
     * @param classLoader the class loader used to locate audio resources
     */
    public AudioService(ClassLoader classLoader) {
        this.classLoader = classLoader == null ? AudioService.class.getClassLoader() : classLoader;
        this.playlistResources = new ArrayList<>();
        this.currentTrackIndex = -1;
    }

    /**
     * Loads every .mp3 file under the playlist resource folder.
     * Missing folders, unreadable resources, and unsupported media are logged as warnings.
     */
    public synchronized void loadPlaylist() {
        playlistResources.clear();
        currentTrackIndex = -1;
        failureStreak = 0;
        playlistLoaded = true;

        URL playlistDirectoryUrl = classLoader.getResource(PLAYLIST_RESOURCE_DIRECTORY);
        if (playlistDirectoryUrl == null) {
            LOGGER.log(Level.WARNING, "Audio playlist folder not found: {0}", PLAYLIST_RESOURCE_DIRECTORY);
            return;
        }

        try {
            String protocol = playlistDirectoryUrl.getProtocol();
            if ("file".equalsIgnoreCase(protocol)) {
                loadPlaylistFromFileSystem(playlistDirectoryUrl);
            } else if ("jar".equalsIgnoreCase(protocol)) {
                loadPlaylistFromJar(playlistDirectoryUrl);
            } else {
                LOGGER.log(Level.WARNING, "Unsupported audio resource location: {0}", playlistDirectoryUrl);
            }
        } catch (IOException | URISyntaxException exception) {
            LOGGER.log(Level.WARNING, "Unable to load background music playlist.", exception);
        }

        if (playlistResources.isEmpty()) {
            LOGGER.log(Level.WARNING, "No playable .mp3 files were found in {0}", PLAYLIST_RESOURCE_DIRECTORY);
        }
    }

    /**
     * Starts playback if a playlist has been loaded and no track is currently active.
     */
    public synchronized void play() {
        shutdownRequested = false;

        if (!playlistLoaded) {
            loadPlaylist();
        }

        if (playlistResources.isEmpty() || currentPlayer != null) {
            return;
        }

        playNext();
    }

    /**
     * Stops playback and disposes the active media player.
     */
    public synchronized void stop() {
        shutdownRequested = true;
        disposeCurrentPlayer();
        currentTrackIndex = -1;
        failureStreak = 0;
    }

    /**
     * Plays the next track in the playlist, wrapping back to the first track after the last one.
     */
    public synchronized void playNext() {
        if (shutdownRequested) {
            return;
        }

        if (!playlistLoaded) {
            loadPlaylist();
        }

        if (playlistResources.isEmpty()) {
            return;
        }

        if (failureStreak >= playlistResources.size()) {
            LOGGER.log(Level.WARNING, "No playable audio tracks are available in {0}.", PLAYLIST_RESOURCE_DIRECTORY);
            return;
        }

        int attempts = 0;
        int nextTrackIndex = currentTrackIndex;

        while (attempts < playlistResources.size() && failureStreak < playlistResources.size()) {
            nextTrackIndex = (nextTrackIndex + 1) % playlistResources.size();

            if (startTrack(nextTrackIndex)) {
                currentTrackIndex = nextTrackIndex;
                failureStreak = 0;
                return;
            }

            currentTrackIndex = nextTrackIndex;
            failureStreak++;
            attempts++;
        }

        if (failureStreak >= playlistResources.size()) {
            LOGGER.log(Level.WARNING, "No playable audio tracks are available in {0}.", PLAYLIST_RESOURCE_DIRECTORY);
        }
    }

    private void loadPlaylistFromFileSystem(URL playlistDirectoryUrl) throws URISyntaxException, IOException {
        Path playlistDirectoryPath = Paths.get(playlistDirectoryUrl.toURI());
        if (!Files.isDirectory(playlistDirectoryPath)) {
            LOGGER.log(Level.WARNING, "Audio playlist path is not a directory: {0}", playlistDirectoryPath);
            return;
        }

        try (Stream<Path> audioFiles = Files.list(playlistDirectoryPath)) {
            audioFiles
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(AUDIO_EXTENSION))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString(), String.CASE_INSENSITIVE_ORDER))
                    .forEach(path -> playlistResources.add(
                            PLAYLIST_RESOURCE_DIRECTORY + "/" + path.getFileName().toString()));
        }
    }

    private void loadPlaylistFromJar(URL playlistDirectoryUrl) throws IOException {
        JarURLConnection jarURLConnection = (JarURLConnection) playlistDirectoryUrl.openConnection();
        String rootEntryName = jarURLConnection.getEntryName();
        if (rootEntryName == null || rootEntryName.isBlank()) {
            LOGGER.log(Level.WARNING, "Audio playlist entry name is missing for {0}", PLAYLIST_RESOURCE_DIRECTORY);
            return;
        }

        String normalizedRootEntryName = URLDecoder.decode(rootEntryName, StandardCharsets.UTF_8);
        if (!normalizedRootEntryName.endsWith("/")) {
            normalizedRootEntryName = normalizedRootEntryName + "/";
        }

        final String playlistRootEntryName = normalizedRootEntryName;

        try (JarFile jarFile = jarURLConnection.getJarFile()) {
            jarFile.stream()
                    .map(JarEntry::getName)
                    .filter(entryName -> entryName.startsWith(playlistRootEntryName))
                    .filter(entryName -> entryName.length() > playlistRootEntryName.length())
                    .filter(entryName -> entryName.indexOf('/', playlistRootEntryName.length()) < 0)
                    .filter(entryName -> entryName.toLowerCase(Locale.ROOT).endsWith(AUDIO_EXTENSION))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .forEach(playlistResources::add);
        }
    }

    private boolean startTrack(int trackIndex) {
        if (trackIndex < 0 || trackIndex >= playlistResources.size()) {
            return false;
        }

        String resourcePath = playlistResources.get(trackIndex);
        URL resourceUrl = classLoader.getResource(resourcePath);
        if (resourceUrl == null) {
            LOGGER.log(Level.WARNING, "Audio track not found: {0}", resourcePath);
            return false;
        }

        final MediaPlayer nextPlayer;
        try {
            Media media = new Media(resourceUrl.toExternalForm());
            nextPlayer = new MediaPlayer(media);
        } catch (RuntimeException exception) {
            LOGGER.log(Level.WARNING, "Unable to prepare audio track: " + resourcePath, exception);
            return false;
        }

        nextPlayer.setCycleCount(1);
        nextPlayer.setOnEndOfMedia(this::playNext);
        nextPlayer.setOnError(() -> handlePlaybackError(nextPlayer, resourcePath));

        disposeCurrentPlayer();
        currentPlayer = nextPlayer;

        try {
            currentPlayer.play();
            return true;
        } catch (RuntimeException exception) {
            LOGGER.log(Level.WARNING, "Unable to start audio track: " + resourcePath, exception);
            if (currentPlayer == nextPlayer) {
                currentPlayer = null;
            }
            nextPlayer.dispose();
            return false;
        }
    }

    private void handlePlaybackError(MediaPlayer failedPlayer, String resourcePath) {
        synchronized (this) {
            if (shutdownRequested) {
                if (failedPlayer != null) {
                    failedPlayer.dispose();
                }
                return;
            }

            if (currentPlayer == failedPlayer) {
                currentPlayer = null;
            }

            if (failedPlayer != null) {
                failedPlayer.dispose();
            }

            failureStreak++;
            LOGGER.log(Level.WARNING, "Audio track failed and will be skipped: {0}", resourcePath);

            if (failureStreak >= playlistResources.size()) {
                LOGGER.log(Level.WARNING, "No playable audio tracks are available in {0}.", PLAYLIST_RESOURCE_DIRECTORY);
                return;
            }

            playNext();
        }
    }

    private void disposeCurrentPlayer() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
        }
    }
}
