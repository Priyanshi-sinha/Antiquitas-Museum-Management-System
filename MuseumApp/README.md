# Antiquitas Museum – JavaFX Application

A full-featured museum management system built with JavaFX 21, faithfully converted from the original React/TypeScript design.

## Features

| Screen | Access |
|---|---|
| **Home** – hero banner, stats, featured artifacts, category navigation | Everyone |
| **Browse** – search & filter all items (Artifacts, Civilizations, Periods, Persons, etc.) | Everyone |
| **Item Detail** – full artifact view with image, origin, attributes, connections | Everyone |
| **ChatBot** – AI-style guide that answers questions about the collection | Everyone |
| **Login** – staff authentication with demo credential quick-fill | Everyone |
| **Add / Edit Artifact** – full form with dynamic attributes | Curator, Admin |
| **Logs** – change history with field-level diffs, filterable | Curator, Admin |
| **Staff** – expandable staff cards, search, delete | Curator, Admin |
| **Register** – add new Curator / Admin accounts | Admin only |

## Demo Credentials

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `admin123` |
| Curator | `curator1` | `curator123` |
| Guest | `guest` | `guest123` |

## Requirements

- Java 17 or later
- Maven 3.8+
- Internet connection (artifact images load from Unsplash URLs)

## Running

```bash
# Run directly
mvn javafx:run

# Or build a fat JAR then run
mvn package
java -jar target/MuseumApp-1.0-SNAPSHOT.jar
```

## Project Structure

```
src/main/java/com/museum/
├── Main.java                      # Application entry point
├── model/                         # Data models
│   ├── Artifact.java
│   ├── Period.java
│   ├── Civilization.java
│   ├── Person.java
│   ├── Organization.java
│   ├── Collection.java
│   ├── User.java
│   └── Log.java
├── data/
│   └── MockData.java              # All seed data (8 artifacts, users, etc.)
├── context/
│   └── AppState.java             # Global state singleton (auth + CRUD)
├── util/
│   ├── Theme.java                 # Color constants
│   ├── UI.java                    # Widget factory helpers
│   └── Navigator.java             # Page navigation
└── controller/                    # All UI pages
    ├── NavBar.java
    ├── HomePage.java
    ├── BrowsePage.java
    ├── ItemDisplayPage.java
    ├── ItemAddEditPage.java
    ├── LoginPage.java
    ├── RegisterPage.java
    ├── StaffPage.java
    ├── LogPage.java
    └── ChatBotPage.java

src/main/resources/com/museum/
└── style.css                      # Dark theme stylesheet
```
