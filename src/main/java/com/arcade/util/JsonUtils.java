package com.arcade.util;

import com.arcade.model.Question;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Provides JSON reading and deserialization helpers for application resources.
 */
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();
    private static final String QUESTIONS_RESOURCE_PATH = "/questions.json";

    private JsonUtils() {
    }

    /**
     * Loads all questions from the default questions resource.
     *
     * @return the deserialized list of questions
     */
    public static List<Question> loadQuestions() {
        return readQuestionsFromResource(QUESTIONS_RESOURCE_PATH);
    }

    /**
     * Loads questions from a JSON resource on the application classpath.
     *
     * @param resourcePath the classpath resource path
     * @return the deserialized list of questions
     */
    public static List<Question> readQuestionsFromResource(String resourcePath) {
        String normalizedResourcePath = normalizeResourcePath(resourcePath);

        try (InputStream inputStream = openResourceStream(normalizedResourcePath)) {
            byte[] content = inputStream.readAllBytes();
            if (content.length == 0) {
                throw new IllegalStateException("Questions resource is empty: " + normalizedResourcePath);
            }

            String jsonContent = new String(content, StandardCharsets.UTF_8);
            if (jsonContent.isBlank()) {
                throw new IllegalStateException("Questions resource is empty: " + normalizedResourcePath);
            }

            return OBJECT_MAPPER.readValue(
                    content,
                    new TypeReference<List<Question>>() {
                    }
            );
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to read questions from resource: " + normalizedResourcePath,
                    exception
            );
        }
    }

    /**
     * Creates a new object mapper configured for the application JSON format.
     *
     * @return a new object mapper instance
     */
    public static ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

    private static String normalizeResourcePath(String resourcePath) {
        if (resourcePath == null || resourcePath.isBlank()) {
            throw new IllegalArgumentException("Resource path must not be blank");
        }

        return resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;
    }

    private static InputStream openResourceStream(String resourcePath) {
        InputStream inputStream = JsonUtils.class.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IllegalStateException("Resource not found: " + resourcePath);
        }

        return inputStream;
    }
}
