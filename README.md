# Mentix
Academic Java arcade game developed with Java and JavaFX.

## Project Description
Mentix is a feature-complete arcade quiz game that started as a console prototype and was completed as a JavaFX graphical application. The player selects a category, answers one question at a time, and tries to finish the session with the highest possible score before losing all lives.

The gameplay is organized around:

- category selection from the available JSON-loaded content
- one question on screen at a time
- four answer options per question
- a lives system with 3 starting lives
- difficulty-based scoring
- a historical ranking stored with each completed match

Scoring is based on question difficulty:

- Easy: 100 points
- Medium: 200 points
- Hard: 300 points

Each game session is composed of 12 questions:

- 4 easy questions
- 4 medium questions
- 4 hard questions

Final user flow:

```text
Main Menu
-> Category Selection
-> Gameplay
-> Result
-> Ranking
```

The current question bank includes the `Programming` and `Mathematics` categories, each organized by difficulty. The ranking stores the player name, final score, and play timestamp so previous results can be reviewed as a historical record.

## Architecture Overview
Mentix uses a clear separation between backend and frontend concerns.

```text
Backend
-> Frontend
```

Backend responsibilities:

- game rules
- score calculation
- lives management
- question flow
- ranking persistence

Frontend responsibilities:

- rendering
- player interaction
- visual assets
- JavaFX views

This separation reduced implementation friction because the game logic remained stable while the interface, layout, and assets evolved independently. Controllers connect both layers, but the business rules stay in the backend services.

## Technologies Used
- Java 17 - core language and runtime.
- JavaFX 21 - graphical interface, screen navigation, and media playback.
- Maven Wrapper with Apache Maven 3.9.16 - repeatable builds and dependency management.
- Maven Compiler Plugin 3.11.0 - compiles the project against Java 17.
- JavaFX Maven Plugin 0.0.8 - runs the JavaFX entry point from Maven.
- Jackson 2.17.2 - JSON parsing and serialization for questions and ranking data.
- JSON - external storage format for the question bank and ranking file.
- JUnit 5.10.2 - automated service tests.

## Project Structure
```text
src/
  main/
    java/
      com/arcade/
        MainApplication.java
        controller/
        model/
        service/
        util/
        view/
    resources/
      audio/
        8-Bit jingles/
      images/
        backgrounds/
        cards/
        icons/
      questions.json
      ranking.json
  test/
    java/
      com/arcade/service/
```

- `controller/` - screen navigation and JavaFX flow coordination.
- `model/` - question, session, and ranking data objects.
- `service/` - game logic, question loading, ranking persistence, and audio playlist handling.
- `util/` - JSON helper utilities.
- `view/` - reusable JavaFX UI components and screen composition.
- `resources/` - backgrounds, spritesheets, icons, audio, and persistent JSON data.
- `src/test/java/` - automated service tests.

## Installation
Prerequisites:

- Java JDK 17
- Git
- An IDE such as IntelliJ IDEA, if you want to run from the editor

Clone the repository:

```bash
git clone https://github.com/cescobar852/arcade-educational-game.git
cd arcade-educational-game
```

Build the project and download dependencies:

```bash
./mvnw.cmd clean compile
```

Or, on Git Bash and Unix-like shells:

```bash
./mvnw clean compile
```

Run the test suite:

```bash
./mvnw.cmd test
```

Or:

```bash
./mvnw test
```

Refresh Maven dependencies when needed:

```bash
./mvnw.cmd -U clean compile
```

Or:

```bash
./mvnw -U clean compile
```

The first execution downloads JavaFX, Jackson, JUnit, and the Maven plugins automatically. No separate Maven installation is required.

## Running The Project
Run from the IDE:

- Open the project as a Maven project.
- Run `com.arcade.MainApplication`.

Run from Maven:

```bash
./mvnw.cmd javafx:run
```

Or:

```bash
./mvnw javafx:run
```

Expected startup flow:

- the JavaFX window opens at a fixed 1280x720 size
- the main menu appears
- the audio playlist starts
- the player selects a category
- gameplay begins

Requirements:

- Java 17 installed and available on the path
- JavaFX resolved through Maven
- the project root as the working directory when using Maven commands

## Assets And Audio
Static visual assets are stored under `src/main/resources/images/` and include:

- `backgrounds/` for the menu and game screens
- `cards/` for the answer card spritesheet
- `icons/` for UI indicators such as lives and play actions

Audio assets are stored under `src/main/resources/audio/8-Bit jingles/` and are loaded as a playlist through `javafx-media`. The final implementation keeps the soundtrack in resource folders and plays MP3 files through the JavaFX media layer, which is the most reliable way to keep playback consistent in both IDE runs and packaged execution.

## Technical Challenges

### Visual Composition
- Maintaining a coherent layout across menu, category selection, gameplay, result, and ranking screens required careful JavaFX spacing and layering.
- The interface had to remain consistent inside a fixed window size without breaking card rendering or overlay placement.
- Backgrounds, cards, and HUD elements needed to stack cleanly without visual conflicts.

### Audio Integration
- JavaFX media playback depends on formats supported by the runtime, so the soundtrack was standardized on MP3 resources.
- The audio system was implemented as a dedicated playlist service so track loading, sequencing, and error handling stayed isolated from gameplay logic.
- Keeping the files in the application resources made the playback path stable across development and distribution.

## Key Decisions And Lessons Learned
- Early planning stabilized the game rules, scoring model, and ranking format before the frontend was completed.
- The backend/frontend split reduced rework because user interface changes did not require changes to the core rules.
- Backend logic remained largely untouched while the JavaFX layer iterated on layout, navigation, and asset integration.
- Incremental delivery made it easier to validate gameplay, ranking persistence, and audio behavior step by step.
- Strong architectural separation is what kept the business logic stable through the final implementation phase.

## Authors
- Team 5 - Group B02
