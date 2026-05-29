# Mentix

Mentix is an educational arcade game project developed for the Programming Paradigms course at Politécnico Grancolombiano.

The project is being built using a modular MVC hybrid architecture with a strong focus on separating educational content from game logic.

At the current stage, the project is focused on backend and terminal-based game flow validation before integrating JavaFX interfaces.

---

# Current Development Status

Current implemented features:

* Maven project setup
* Java 17 configuration
* JavaFX integration
* Modular question system
* JSON-based question loading
* Jackson deserialization
* Question service layer
* Backend modular architecture

Current execution mode:

* Terminal/backend validation
* No graphical interface implemented yet

---

# Tech Stack

* Java 17.0.19
* Eclipse Temurin JDK
* JavaFX 21
* Maven Wrapper 3.9.16
* Jackson
* JUnit 5

---

# Recommended IDE

Recommended IDE:

* IntelliJ IDEA Community or Ultimate Edition

The project was configured and tested primarily using IntelliJ IDEA.

---

# Requirements

Before running the project, make sure you have installed:

## Required

* Java JDK 17
* Git

## Optional but Recommended

* IntelliJ IDEA

---

# Verify Installed Versions

## Check Java Version

```bash
java -version
```

Expected output example:

```text
openjdk version "17.0.19"
```

---

## Check Maven Wrapper Version

Windows CMD or PowerShell:

```bash
mvnw.cmd -version
```

Git Bash:

```bash
./mvnw -version
```

Expected output example:

```text
Apache Maven 3.9.16
```

---

# Clone the Repository

```bash
git clone https://github.com/cescobar852/arcade-educational-game.git
```

Then enter the project folder:

```bash
cd arcade-educational-game
```

---

# Official Build Tool

This project uses Maven Wrapper as the official build and dependency management tool.

Do not rely on globally installed Maven versions.

Use:

Windows CMD or PowerShell:

```bash
mvnw.cmd
```

Git Bash:

```bash
./mvnw
```

The wrapper guarantees that all team members use the same Maven environment and dependency versions.

---

# Download Dependencies and Compile

The first execution may take several minutes because Maven Wrapper will automatically download all required dependencies.

To download dependencies and compile the project:

Windows CMD or PowerShell:

```bash
mvnw.cmd clean compile
```

Git Bash:

```bash
./mvnw clean compile
```

This process automatically downloads:

* JavaFX
* Jackson
* JUnit
* Maven plugins

---

# Run the Project

The project currently runs in backend/terminal validation mode.

Windows CMD or PowerShell:

```bash
mvnw.cmd javafx:run
```

Git Bash:

```bash
./mvnw javafx:run
```

---

# Current Backend Validation

The current backend implementation validates:

* External JSON question loading
* Question deserialization
* Modular question architecture
* Question service layer
* Backend-only game foundation

The current focus is validating backend logic before implementing graphical interfaces.

---

# Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── com/arcade/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── service/
│   │       ├── util/
│   │       └── view/
│   └── resources/
│       └── questions.json
```

---

# Architecture Principles

The project follows these architectural principles:

* MVC hybrid architecture
* Complete separation between educational content and game logic
* Backend-first development
* Terminal-first validation before JavaFX integration
* Simple and maintainable architecture
* Modular educational content
* No unnecessary enterprise patterns

---

# Git Workflow

Development is currently handled using feature branches.

Avoid pushing directly to the main branch.

---

# Notes

* The project is currently under active development.
* Gameplay systems are still being implemented.
* JavaFX visual integration will be added after backend validation is completed.
* Educational content is fully externalized through JSON files.
* If using operating systems other than Windows, equivalent shell commands may vary.