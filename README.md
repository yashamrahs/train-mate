# TrainMate

**TrainMate** is a command-line based train ticket booking system written in Java. It simulates a mini version of an IRCTC-like platform, offering functionalities such as user signup/login, train search, ticket booking, viewing bookings, and cancellation support (WIP). Designed for learning, demo, and small-scale usage.

---

## Features

-  **User Authentication**
    - Secure Sign-Up & Login
    - Password hashing for safe storage

-  **Train Search**
    - Search available trains by source and destination
    - View route details with station arrival times

-  **Ticket Booking**
    - Interactive seat selection by row & column
    - View seat layout before booking

-  **Booking Management**
    - Fetch all bookings for the logged-in user
    - Cancel ticket

---

## Architecture

- **Java 8**
- **Modular OOP Design**
- Entities: `User`, `Train`, `Ticket`
- Services: `UserBookingService`, `TrainService`
- Util Classes: `UserServiceUtil`
- CLI Interface: `App.java`

---

## Getting Started

### Prerequisites

- Java 8+
- Gradle (or use wrapper)
- Terminal (CLI) access

### Run the App

```bash
git clone https://github.com/yashamrahs/train-mate.git
cd train-mate
./gradlew run
```

### Folder Structure
```
├── app
│   └── src/main/java/ticket/booking/
│       ├── App.java                # CLI Main Application
│       ├── entities/               # POJOs: Train, User, Ticket
│       ├── services/               # Booking logic
│       └── util/                   # Utility (password hashing, etc.)
```