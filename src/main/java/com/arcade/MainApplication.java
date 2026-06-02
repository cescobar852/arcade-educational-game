package com.arcade;

import com.arcade.model.Question;
import com.arcade.model.RankingEntry;
import com.arcade.service.GameService;
import com.arcade.service.QuestionService;
import com.arcade.service.RankingService;

import java.util.List;
import java.util.Scanner;

/**
 * Console entry point for the arcade educational game.
 */
public class MainApplication {

    private static final String SEPARATOR = "================================";
    private static final int QUESTIONS_PER_SESSION = 12;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        QuestionService questionService;
        try {
            questionService = new QuestionService();
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());
            return;
        }

        List<String> availableCategories = questionService.getAvailableCategories();
        if (availableCategories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }

        displayAvailableCategories(availableCategories);
        String selectedCategory = readSelectedCategory(scanner, availableCategories);
        System.out.println("Selected Category: " + selectedCategory);

        GameService gameService = new GameService(questionService);
        gameService.startGame(selectedCategory);

        if (gameService.getLoadedQuestions().size() != QUESTIONS_PER_SESSION) {
            System.out.println("Selected category does not contain enough questions.");
            return;
        }

        while (!gameService.isGameOver()) {
            Question currentQuestion = gameService.getCurrentQuestion();
            if (currentQuestion == null) {
                break;
            }

            displayStatus(gameService, currentQuestion);
            displayQuestion(gameService, currentQuestion);

            int selectedOptionIndex = readValidOptionIndex(scanner, currentQuestion.getOptions().size());
            String selectedOption = currentQuestion.getOptions().get(selectedOptionIndex - 1);
            boolean correct = gameService.submitAnswer(selectedOption);

            displayAnswerFeedback(correct, gameService);
        }

        displayGameOver(gameService);
        handleRanking(gameService, scanner);
    }

    private static void displayAvailableCategories(List<String> availableCategories) {
        System.out.println(SEPARATOR);
        System.out.println("AVAILABLE CATEGORIES");
        System.out.println(SEPARATOR);

        for (int index = 0; index < availableCategories.size(); index++) {
            System.out.println((index + 1) + ". " + availableCategories.get(index));
        }

        System.out.println();
    }

    private static String readSelectedCategory(Scanner scanner, List<String> availableCategories) {
        int selectedCategoryIndex = readValidSelectionIndex(scanner, availableCategories.size(),
                "Select a category:\n> ");
        return availableCategories.get(selectedCategoryIndex - 1);
    }

    private static void displayStatus(GameService gameService, Question currentQuestion) {
        System.out.println(SEPARATOR);
        System.out.println("Lives: " + gameService.getRemainingLives());
        System.out.println("Score: " + gameService.getScore());
        System.out.println("Difficulty: " + getDifficultyLabel(currentQuestion));
        System.out.println(SEPARATOR);
    }

    private static void displayQuestion(GameService gameService, Question currentQuestion) {
        System.out.println();
        System.out.println("Question " + gameService.getCurrentQuestionNumber());
        System.out.println();
        System.out.println(currentQuestion.getQuestionText());

        String extraContent = currentQuestion.getExtraContent();
        if (extraContent != null && !extraContent.isBlank()) {
            System.out.println(extraContent);
        }

        List<String> options = currentQuestion.getOptions();
        System.out.println();
        for (int index = 0; index < options.size(); index++) {
            System.out.println((index + 1) + ". " + options.get(index));
        }
    }

    private static int readValidOptionIndex(Scanner scanner, int optionCount) {
        return readValidSelectionIndex(scanner, optionCount, "Select an option (1-" + optionCount + "): ");
    }

    private static int readValidSelectionIndex(Scanner scanner, int selectionCount, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a number between 1 and " + selectionCount + ".");
                continue;
            }

            try {
                int selectedOption = Integer.parseInt(input);
                if (selectedOption < 1 || selectedOption > selectionCount) {
                    System.out.println("Invalid option. Please enter a number between 1 and " + selectionCount + ".");
                    continue;
                }

                return selectedOption;
            } catch (NumberFormatException exception) {
                System.out.println("Invalid input. Please enter a numeric value between 1 and " + selectionCount + ".");
            }
        }
    }

    private static void displayAnswerFeedback(boolean correct, GameService gameService) {
        if (correct) {
            System.out.println("Correct Answer!");
        } else {
            System.out.println("Wrong Answer!");
        }

        System.out.println("Score: " + gameService.getScore());
        System.out.println("Lives: " + gameService.getRemainingLives());
        System.out.println();
    }

    private static void displayGameOver(GameService gameService) {
        System.out.println(SEPARATOR);
        System.out.println("GAME OVER");
        System.out.println(SEPARATOR);
        System.out.println("Final Score: " + gameService.getScore());
        System.out.println(getGameOverReason(gameService));
    }

    private static void handleRanking(GameService gameService, Scanner scanner) {
        RankingService rankingService = new RankingService();

        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        try {
            rankingService.addScore(playerName, gameService.getScore());
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());
        }

        displayTopScores(rankingService.getTopScores(10));
    }

    private static void displayTopScores(List<RankingEntry> topScores) {
        System.out.println();
        System.out.println("=========================");
        System.out.println("TOP SCORES");
        System.out.println("=========================");
        System.out.println("Position | Player | Score");

        if (topScores.isEmpty()) {
            System.out.println("No ranking entries available.");
            return;
        }

        for (int index = 0; index < topScores.size(); index++) {
            RankingEntry rankingEntry = topScores.get(index);
            String playerName = rankingEntry.getPlayerName() == null || rankingEntry.getPlayerName().isBlank()
                    ? "Anonymous"
                    : rankingEntry.getPlayerName().trim();
            System.out.println((index + 1) + ". " + playerName + " - " + rankingEntry.getScore());
        }
    }

    private static String getGameOverReason(GameService gameService) {
        if (gameService.getRemainingLives() <= 0) {
            return "No remaining lives.";
        }

        return "All questions completed.";
    }

    private static String getDifficultyLabel(Question question) {
        if (question == null || question.getDifficulty() == null || question.getDifficulty().isBlank()) {
            return "UNKNOWN";
        }

        return question.getDifficulty();
    }
}
