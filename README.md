# Viecoaching — JavaFX CRUD Application

> A JavaFX desktop application for managing coaching sessions and users, backed by a MySQL database.

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-19-1F8AC0?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

## Overview

**Viecoaching** is a student desktop project built with JavaFX and Maven. It provides a graphical interface to manage **coaching sessions** (`Seance`) and **users** (`Utilisateur`), including user registration and login. Data is persisted in a MySQL database through a plain JDBC service layer.

The application follows a simple layered structure: FXML views and JavaFX controllers on top, a service layer implementing CRUD operations, entity classes mapping the database tables, and a singleton database connection utility.

## Features

### Session management (`Seance`)
Full CRUD over coaching sessions through `ServiceSeance`:
- **Create** — add a new session (title, duration, link, password, type, owner user, image)
- **Read** — list all sessions and fetch a single session by ID
- **Update** — edit an existing session
- **Delete** — remove a session by ID

A session carries: `title`, `duree` (duration), `lien` (link), `password`, `user_id` (owner), `type_id` (session type), and an `image`.

### User management (`Utilisateur`)
CRUD over users through `ServiceUtilisateur`:
- **Create** — add a user
- **Read** — list all users and find a user by ID
- **Update** — update a user's email
- **Delete** — remove a user by ID

A user carries: `nom`, `prenom`, `email`, `tel`, `mdp` (password), `genre`, `ville`, `active` flag, and a `role_id`.

### Authentication
- **Registration** (`ServiceRegistre`) — field validation (required fields, email format, duplicate-email check, password length and confirmation, phone number) and password hashing with BCrypt before insert.
- **Login** (`ServiceAuthentication`) — email format validation, user lookup by email, and BCrypt password verification (with `$2y$`/`$2a$` prefix compatibility).

### Supporting entities
- `Role` — user roles (id, name) with a helper collection of users.
- `TypeSeance` — session types (id, type).

> *Note: `Reservation` and `ServiceRole` exist as placeholder classes and are not yet implemented.*

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| UI | JavaFX 19 (FXML), ControlsFX, JFoenix, FontAwesomeFX, AnimateFX |
| Build | Maven (`javafx-maven-plugin`) |
| Database | MySQL (`mysql-connector-java`) via JDBC |
| Security | jBCrypt (password hashing) |
| Other | iTextPDF, JAXB |

## Project Structure

```
javafx-crud/
├── pom.xml
├── dependencies/                  # bundled JARs (mail, mysql connector, iText, etc.)
├── images/                        # screenshots
└── src/main/
    ├── java/
    │   ├── tests/Main.java        # JavaFX Application entry point
    │   ├── entities/              # Utilisateur, Seance, Role, TypeSeance, Reservation
    │   ├── services/              # ServiceSeance, ServiceUtilisateur,
    │   │                          #   ServiceAuthentication, ServiceRegistre, IService
    │   ├── iservices/IService.java
    │   ├── controllers/           # JavaFX controllers (Login, Registre, Add, Modify, ...)
    │   ├── utils/                 # MyDatabase (connection), PasswordHasher
    │   ├── animations/            # UI animations
    │   ├── mask/                  # input field masks & validators
    │   └── public/                # CSS, images
    └── resources/
        ├── fxml/                  # login, registre, add, modifier, utilisateur, OrderedEventList
        └── css/
```

## Getting Started

### Prerequisites
- JDK 17
- Maven
- A running MySQL server with a database named `viecoaching`

### Database
The application connects to a MySQL database named `viecoaching` on `localhost:3306`. Create the database and the required tables (`utilisateur`, `seance`, and supporting tables) before running. Connection settings live in `src/main/java/utils/MyDatabase.java` — adjust the URL, username, and password to match your local MySQL setup.

### Run
```bash
mvn clean javafx:run
```
The main class is `tests.Main`, which loads the JavaFX UI.

## Notes

- This is a **student project** and is presented as-is.
- The session-management service (`ServiceSeance`) uses string-concatenated SQL; the user and authentication services use prepared statements. Prepared statements throughout would be the natural next improvement.
- `Reservation` and `ServiceRole` are empty placeholders left for future work.
- Comments and console messages in the source are in French; entity/field names follow French naming (`nom`, `prenom`, `ville`, etc.).
- Database credentials in `MyDatabase.java` are local development defaults — change them for your own environment and never commit real credentials.

---
<p align="center">Built by <b>Mohamed Habib Khattat</b> — <a href="https://github.com/MuhamedHabib">GitHub (@MuhamedHabib)</a> · <a href="https://www.linkedin.com/in/mohamed-habib-khattat-2b206a173">LinkedIn</a></p>
