# 🎬 Book My Ticket — Movie & Show Ticket Booking System

A backend RESTful application built with **Spring Boot**, **Spring Data JPA (Hibernate)**, and **PostgreSQL** that allows users to browse movies/shows, check seat availability, book tickets, and manage bookings.

---

## 🛠 Technology Stack

| Layer              | Technology                     |
|--------------------|--------------------------------|
| Backend            | Spring Boot 2.7.18 (Java 8)   |
| Build Tool         | Gradle (Groovy DSL)            |
| ORM                | Spring Data JPA (Hibernate)    |
| Database           | PostgreSQL                     |
| API Style          | RESTful APIs (JSON)            |
| Validation         | Hibernate Validator            |
| API Documentation  | Springdoc OpenAPI (Swagger UI) |
| Exception Handling | @RestControllerAdvice          |

---

## 📁 Project Structure

```
publicissapient/                    ← Parent project (IDE workspace root)
├── build.gradle                    ← Parent build (declares plugin versions)
├── settings.gradle                 ← Includes book-my-ticket module
├── README.md
└── book-my-ticket/                 ← Spring Boot module
    ├── build.gradle
    ├── gradlew / gradlew.bat
    ├── README.md
    └── src/main/
        ├── java/com/ticketbooking/
        │   ├── BookMyTicketApplication.java
        │   ├── config/
        │   │   └── OpenApiConfig.java
        │   ├── controller/
        │   │   ├── UserController.java
        │   │   ├── MovieController.java
        │   │   ├── TheaterController.java
        │   │   ├── ShowController.java
        │   │   ├── BookingController.java
        │   │   └── PaymentController.java
        │   ├── service/
        │   │   ├── UserService.java
        │   │   ├── MovieService.java
        │   │   ├── TheaterService.java
        │   │   ├── ShowService.java
        │   │   ├── BookingService.java
        │   │   └── PaymentService.java
        │   ├── repository/
        │   ├── model/
        │   │   ├── enums/
        │   │   ├── User.java, Movie.java, Theater.java
        │   │   ├── Show.java, Seat.java, Booking.java
        │   │   └── Payment.java
        │   ├── dto/
        │   └── exception/
        └── resources/
            ├── application.yml
            └── db/
                ├── init.sql
                └── sample-data.sql
```

---

## 🗄 Database Design

### ER Overview

```
Users ──< Bookings >── Shows >── Movies
                |          |
          booking_seats  Theaters
                |
              Seats
                
Payments ──── Bookings
```

### Tables

| Table          | Description                          |
|----------------|--------------------------------------|
| users          | Registered users (USER / ADMIN)      |
| movies         | Movie catalog                        |
| theaters       | Theater locations                    |
| shows          | Movie screenings at theaters         |
| seats          | Seats per show (AVAILABLE / BOOKED)  |
| bookings       | User ticket bookings                 |
| booking_seats  | Many-to-many: bookings ↔ seats       |
| payments       | Payment records per booking          |

---

## 📜 SQL Scripts

### 1. Create Database

```sql
CREATE DATABASE book_my_ticket;
```

### 2. Create Tables (`src/main/resources/db/init.sql`)

```sql
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    duration INTEGER,
    language VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS theaters (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS shows (
    id BIGSERIAL PRIMARY KEY,
    movie_id BIGINT NOT NULL REFERENCES movies(id),
    theater_id BIGINT NOT NULL REFERENCES theaters(id),
    show_time TIMESTAMP NOT NULL,
    price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS seats (
    id BIGSERIAL PRIMARY KEY,
    seat_number VARCHAR(10) NOT NULL,
    show_id BIGINT NOT NULL REFERENCES shows(id),
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    show_id BIGINT NOT NULL REFERENCES shows(id),
    total_amount NUMERIC(10, 2) NOT NULL,
    booking_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE IF NOT EXISTS booking_seats (
    booking_id BIGINT NOT NULL REFERENCES bookings(id),
    seat_id BIGINT NOT NULL REFERENCES seats(id),
    PRIMARY KEY (booking_id, seat_id)
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE REFERENCES bookings(id),
    payment_mode VARCHAR(50),
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_shows_movie_id ON shows(movie_id);
CREATE INDEX IF NOT EXISTS idx_shows_theater_id ON shows(theater_id);
CREATE INDEX IF NOT EXISTS idx_seats_show_id ON seats(show_id);
CREATE INDEX IF NOT EXISTS idx_seats_status ON seats(status);
CREATE INDEX IF NOT EXISTS idx_bookings_user_id ON bookings(user_id);
CREATE INDEX IF NOT EXISTS idx_bookings_show_id ON bookings(show_id);
```

### 3. Sample Data (`src/main/resources/db/sample-data.sql`)

Inserts sample users, movies, theaters, shows, and seats for quick testing.

---

## 🌐 REST API Reference

### 👤 User APIs

| Method | Endpoint                | Description         | Request Body       |
|--------|-------------------------|---------------------|--------------------|
| POST   | `/api/users/register`   | Register a new user | `UserDto`          |
| POST   | `/api/users/login`      | User login          | `LoginDto`         |
| GET    | `/api/users/{userId}`   | Get user by ID      | —                  |

**Register — POST** `/api/users/register`
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "pass123",
  "role": "USER"
}
```

**Login — POST** `/api/users/login`
```json
{
  "email": "john@example.com",
  "password": "pass123"
}
```

---

### 🎬 Movie APIs

| Method | Endpoint               | Description        | Request Body |
|--------|------------------------|--------------------|--------------|
| GET    | `/api/movies`          | List all movies    | —            |
| GET    | `/api/movies/{movieId}`| Get movie by ID    | —            |
| POST   | `/api/movies`          | Create a movie     | `MovieDto`   |
| PUT    | `/api/movies/{movieId}`| Update a movie     | `MovieDto`   |
| DELETE | `/api/movies/{movieId}`| Delete a movie     | —            |

**Create Movie — POST** `/api/movies`
```json
{
  "title": "Inception",
  "genre": "Sci-Fi",
  "duration": 148,
  "language": "English"
}
```

---

### 🏛 Theater APIs

| Method | Endpoint          | Description         | Request Body   |
|--------|-------------------|---------------------|----------------|
| GET    | `/api/theaters`   | List all theaters   | —              |
| POST   | `/api/theaters`   | Create a theater    | `TheaterDto`   |

**Create Theater — POST** `/api/theaters`
```json
{
  "name": "PVR Cinemas",
  "location": "Downtown Mall"
}
```

---

### 🎭 Show APIs

| Method | Endpoint                     | Description              | Request Body |
|--------|------------------------------|--------------------------|--------------|
| GET    | `/api/shows/movie/{movieId}` | Get shows for a movie    | —            |
| POST   | `/api/shows`                 | Create a show            | `ShowDto`    |
| GET    | `/api/shows/{showId}/seats`  | Get available seats      | —            |

**Create Show — POST** `/api/shows`
```json
{
  "movieId": 1,
  "theaterId": 1,
  "showTime": "2025-07-15T10:00:00",
  "price": 250.00
}
```

---

### 🎟 Booking APIs

| Method | Endpoint                      | Description             | Request Body  |
|--------|-------------------------------|-------------------------|---------------|
| POST   | `/api/bookings`               | Book tickets            | `BookingDto`  |
| GET    | `/api/bookings/user/{userId}` | Get bookings for a user | —             |

**Create Booking — POST** `/api/bookings`
```json
{
  "userId": 2,
  "showId": 1,
  "seatIds": [1, 2, 3]
}
```

---

### 💳 Payment APIs

| Method | Endpoint         | Description       | Request Body  |
|--------|------------------|-------------------|---------------|
| POST   | `/api/payments`  | Process payment   | `PaymentDto`  |

**Process Payment — POST** `/api/payments`
```json
{
  "bookingId": 1,
  "paymentMode": "CREDIT_CARD"
}
```

> **Note:** Payment is a mock implementation with ~80% success rate. On success, booking status becomes `CONFIRMED`. On failure, it becomes `CANCELLED`.

---

### ❗ Error Response Format

All errors follow a consistent format:

```json
{
  "timestamp": "2025-07-10T18:00:00",
  "message": "Seat A1 is already booked",
  "status": 409
}
```

| Exception                    | HTTP Status |
|------------------------------|-------------|
| ResourceNotFoundException    | 404         |
| SeatAlreadyBookedException   | 409         |
| InvalidBookingException      | 400         |
| PaymentFailedException       | 402         |
| Validation errors            | 400         |
| Unexpected errors            | 500         |

---

## 🚀 Steps to Run the Project

### Prerequisites

- **Java 8** (JDK 1.8)
- **PostgreSQL** (running on localhost:5432)
- **Gradle** (wrapper included, no separate install needed)

### Step 1: Clone / Open the Project

```bash
cd c:\projects\elipanda\book-my-ticket
```

### Step 2: Create the Database

Connect to PostgreSQL and run:

```sql
CREATE DATABASE book_my_ticket;
```

### Step 3: Run SQL Scripts

Execute the scripts in order against the `book_my_ticket` database:

```bash
psql -U postgres -d book_my_ticket -f src/main/resources/db/init.sql
psql -U postgres -d book_my_ticket -f src/main/resources/db/sample-data.sql
```

### Step 4: Update Database Credentials (if needed)

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/book_my_ticket
    username: postgres
    password: postgres
```

### Step 5: Run the Application

**Windows:**
```bash
gradlew.bat bootRun
```

**Linux/Mac:**
```bash
./gradlew bootRun
```

The app starts at: **http://localhost:8089**

---

## 📖 Swagger / OpenAPI Documentation

Swagger UI is auto-configured via `springdoc-openapi-ui`. Once the app is running:

| Resource | URL |
|---|---|
| Swagger UI | [http://localhost:8089/swagger-ui.html](http://localhost:8089/swagger-ui.html) |
| OpenAPI JSON | [http://localhost:8089/v3/api-docs](http://localhost:8089/v3/api-docs) |
| OpenAPI YAML | [http://localhost:8089/v3/api-docs.yaml](http://localhost:8089/v3/api-docs.yaml) |

All 6 controllers are automatically documented:
- User APIs (`/api/users/**`)
- Movie APIs (`/api/movies/**`)
- Theater APIs (`/api/theaters/**`)
- Show APIs (`/api/shows/**`)
- Booking APIs (`/api/bookings/**`)
- Payment APIs (`/api/payments/**`)

---

## 📦 Build Executable JAR for Deployment

### Generate the JAR

**Windows:**
```bash
gradlew.bat clean bootJar
```

**Linux/Mac:**
```bash
./gradlew clean bootJar
```

The JAR file will be generated at:

```
build/libs/book-my-ticket-0.0.1-SNAPSHOT.jar
```

### Run the JAR

```bash
java -jar build/libs/book-my-ticket-0.0.1-SNAPSHOT.jar
```

### Run with Custom DB Config (override at runtime)

```bash
java -jar build/libs/book-my-ticket-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:postgresql://prod-host:5432/book_my_ticket \
  --spring.datasource.username=prod_user \
  --spring.datasource.password=prod_pass
```

---

## 🔄 Booking Flow

```
1. User registers / logs in
2. Browse movies → GET /api/movies
3. View shows for a movie → GET /api/shows/movie/{movieId}
4. Check available seats → GET /api/shows/{showId}/seats
5. Book seats → POST /api/bookings
6. Make payment → POST /api/payments
7. Booking confirmed ✅ or cancelled ❌
```

---

## ✅ Future Enhancements

- Redis-based seat locking
- Payment gateway integration (Razorpay / Stripe)
- JWT-based authentication with Spring Security
- Admin dashboard
- Cancel booking API

---
