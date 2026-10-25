# Academic Glow

Academic Glow is a lightweight Java event management application designed for college or academic fest event tracking. It lets you manage events, view participants, and register students for sessions without any external database or framework.

## Features

- Event listing with name, category, date, time, venue, and status
- Participant registration by register number
- Duplicate-registration prevention for the same event
- In-memory data storage with seeded sample records
- Simple REST-style JSON APIs
- Static HTML front-end served by a Java HTTP server

## Tech Stack

- Java
- Java SE HTTP Server (`com.sun.net.httpserver.HttpServer`)
- HTML/CSS/JavaScript for the UI

## Project Structure

- `Main.java` – starts the HTTP server and defines API routes
- `DataStore.java` – singleton in-memory store and seeded demo data
- `Event.java` – event model
- `Participant.java` – participant model
- `Registration.java` – event-participant linking model
- `index.html` – frontend interface

## Getting Started

### Prerequisites

- Java JDK 8 or later

### Run the project

From the project root, compile and run:

```bash
javac *.java
java Main
```

Then open:

```text
http://localhost:8080
```

## API Endpoints

### Get all events

```http
GET /api/events
```

Returns a JSON array of events with participant counts.

### Get participants for an event

```http
GET /api/participants?eventId=1
```

Returns the participants registered for the specified event.

### Register a participant

```http
POST /api/register
```

Form data fields:

- `name`
- `registerNumber`
- `department`
- `semester`
- `eventId`

Example:

```bash
curl -X POST http://localhost:8080/api/register \
  -d "name=Aysha&registerNumber=36&department=CSE&semester=S3&eventId=1"
```

## Demo Data

The application is preloaded with sample events and participants for testing, including:

- Oppana
- Kolkali
- Group Song Girls
- Group Song Boys
- Drama

## Notes

This project is a simple academic demonstration and uses in-memory storage only. It is ideal for learning Java server programming, event registration logic, and full-stack basics without a database.

## License

This project is for educational/demo purposes.
