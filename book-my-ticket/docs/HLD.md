# 📐 High-Level Design (HLD) — Book My Ticket

## 1. Introduction

### 1.1 Purpose
Book My Ticket is a backend RESTful application for movie and show ticket booking. It enables users to browse movies, check seat availability, book tickets, and process payments.

### 1.2 Scope
- User registration and authentication
- Movie and theater catalog management
- Show scheduling with seat management
- Ticket booking with seat reservation
- Payment processing (mock)
- API documentation via Swagger/OpenAPI

### 1.3 Assumptions
- Single-server deployment (no distributed architecture)
- Mock payment gateway (~80% success simulation)
- No authentication/authorization framework (plain password comparison)
- PostgreSQL as the sole database

---

## 2. System Architecture

### 2.1 Architecture Style
Monolithic, layered architecture following the MVC pattern.

```
┌─────────────────────────────────────────────────────────┐
│                      Client Layer                       │
│            (Postman / Swagger UI / Frontend)             │
└──────────────────────────┬──────────────────────────────┘
                           │ HTTP (REST/JSON)
                           ▼
┌─────────────────────────────────────────────────────────┐
│                   API Gateway / Server                  │
│               Spring Boot (Embedded Tomcat)              │
│                    Port: 8089                            │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌───────────────────────────────────────────────────┐  │
│  │              Controller Layer (REST)               │  │
│  │  UserController, MovieController, TheaterController│  │
│  │  ShowController, BookingController, PaymentController│ │
│  └──────────────────────┬────────────────────────────┘  │
│                         │                               │
│  ┌──────────────────────▼────────────────────────────┐  │
│  │              Service Layer (Business Logic)        │  │
│  │  UserService, MovieService, TheaterService         │  │
│  │  ShowService, BookingService, PaymentService       │  │
│  └──────────────────────┬────────────────────────────┘  │
│                         │                               │
│  ┌──────────────────────▼────────────────────────────┐  │
│  │            Repository Layer (Data Access)          │  │
│  │         Spring Data JPA (Hibernate ORM)            │  │
│  └──────────────────────┬────────────────────────────┘  │
│                         │                               │
└─────────────────────────┼───────────────────────────────┘
                          │ JDBC
                          ▼
              ┌───────────────────────┐
              │     PostgreSQL DB     │
              │   book_my_ticket      │
              │   Port: 5432          │
              └───────────────────────┘
```

### 2.2 Technology Stack

| Component          | Technology                          |
|--------------------|-------------------------------------|
| Language           | Java 8                              |
| Framework          | Spring Boot 2.7.18                  |
| Build Tool         | Gradle 8.8 (Groovy DSL)            |
| ORM                | Spring Data JPA (Hibernate)         |
| Database           | PostgreSQL                          |
| Validation         | Hibernate Validator (JSR 380)       |
| API Documentation  | Springdoc OpenAPI 1.7.0 (Swagger)   |
| Exception Handling | @RestControllerAdvice               |
| Server             | Embedded Tomcat                     |

---

## 3. Module Overview

### 3.1 Module Decomposition

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│    User      │    │    Movie     │    │   Theater    │
│   Module     │    │   Module     │    │   Module     │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                   │
       │            ┌──────▼───────┐           │
       │            │    Show      │◄──────────┘
       │            │   Module     │
       │            └──────┬───────┘
       │                   │
       │            ┌──────▼───────┐
       └───────────►│   Booking    │
                    │   Module     │
                    └──────┬───────┘
                           │
                    ┌──────▼───────┐
                    │   Payment    │
                    │   Module     │
                    └──────────────┘
```

| Module   | Responsibility                                      |
|----------|-----------------------------------------------------|
| User     | Registration, login, user profile retrieval          |
| Movie    | CRUD operations on movie catalog                    |
| Theater  | Create and list theater locations                   |
| Show     | Schedule shows (movie + theater + time), seat query |
| Booking  | Reserve seats, create bookings, user booking history|
| Payment  | Process payment, update booking status              |

---

## 4. Database Design (High-Level)

### 4.1 Entity Relationship Diagram

```
┌──────────┐       ┌──────────┐       ┌──────────┐
│  users   │       │  movies  │       │ theaters │
│──────────│       │──────────│       │──────────│
│ id (PK)  │       │ id (PK)  │       │ id (PK)  │
│ name     │       │ title    │       │ name     │
│ email    │       │ genre    │       │ location │
│ password │       │ duration │       └────┬─────┘
│ role     │       │ language │            │
└────┬─────┘       └────┬─────┘            │
     │                  │                  │
     │            ┌─────▼──────────────────▼─────┐
     │            │           shows               │
     │            │───────────────────────────────│
     │            │ id (PK)                       │
     │            │ movie_id (FK → movies)        │
     │            │ theater_id (FK → theaters)    │
     │            │ show_time, price              │
     │            └─────┬─────────────────────────┘
     │                  │
     │            ┌─────▼─────┐
     │            │   seats   │
     │            │───────────│
     │            │ id (PK)   │
     │            │ seat_number│
     │            │ show_id(FK)│
     │            │ status    │
     │            └─────┬─────┘
     │                  │
     │    ┌─────────────▼──────────────┐
     │    │       booking_seats        │
     │    │ (booking_id, seat_id) PK   │
     │    └─────────────┬──────────────┘
     │                  │
┌────▼──────────────────▼─────┐
│          bookings           │
│─────────────────────────────│
│ id (PK)                     │
│ user_id (FK → users)        │
│ show_id (FK → shows)        │
│ total_amount, booking_time  │
│ status                      │
└─────────────┬───────────────┘
              │
       ┌──────▼──────┐
       │  payments   │
       │─────────────│
       │ id (PK)     │
       │ booking_id  │
       │ (FK, UNIQUE)│
       │ payment_mode│
       │payment_status│
       └─────────────┘
```

### 4.2 Key Relationships

| Relationship              | Type        | Description                          |
|---------------------------|-------------|--------------------------------------|
| users → bookings          | One-to-Many | A user can have multiple bookings    |
| movies → shows            | One-to-Many | A movie can have multiple shows      |
| theaters → shows          | One-to-Many | A theater can host multiple shows    |
| shows → seats             | One-to-Many | A show has multiple seats            |
| bookings ↔ seats          | Many-to-Many| Via `booking_seats` join table       |
| bookings → payments       | One-to-One  | Each booking has one payment record  |

---

## 5. API Design (High-Level)

### 5.1 API Summary

| Module   | Base Path        | Endpoints |
|----------|------------------|-----------|
| User     | `/api/users`     | 3         |
| Movie    | `/api/movies`    | 5         |
| Theater  | `/api/theaters`  | 2         |
| Show     | `/api/shows`     | 3         |
| Booking  | `/api/bookings`  | 2         |
| Payment  | `/api/payments`  | 1         |

### 5.2 Core Booking Flow

```
  Client                    Server                      Database
    │                         │                            │
    │  POST /api/users/register                            │
    │────────────────────────►│  Create user               │
    │                         │───────────────────────────►│
    │                         │                            │
    │  GET /api/movies        │                            │
    │────────────────────────►│  Fetch movies              │
    │                         │───────────────────────────►│
    │                         │                            │
    │  GET /api/shows/movie/1 │                            │
    │────────────────────────►│  Fetch shows for movie     │
    │                         │───────────────────────────►│
    │                         │                            │
    │  GET /api/shows/1/seats │                            │
    │────────────────────────►│  Fetch available seats     │
    │                         │───────────────────────────►│
    │                         │                            │
    │  POST /api/bookings     │                            │
    │────────────────────────►│  Validate seats            │
    │                         │  Mark seats BOOKED         │
    │                         │  Create booking (PENDING)  │
    │                         │───────────────────────────►│
    │                         │                            │
    │  POST /api/payments     │                            │
    │────────────────────────►│  Mock payment processing   │
    │                         │  Update booking status     │
    │                         │  (CONFIRMED / CANCELLED)   │
    │                         │───────────────────────────►│
    │◄────────────────────────│                            │
```

---

## 6. Error Handling Strategy

All exceptions are handled centrally via `@RestControllerAdvice` (GlobalExceptionHandler).

| Exception                  | HTTP Status | Scenario                        |
|----------------------------|-------------|---------------------------------|
| ResourceNotFoundException  | 404         | Entity not found by ID          |
| SeatAlreadyBookedException | 409         | Seat already reserved           |
| InvalidBookingException    | 400         | Invalid booking data            |
| PaymentFailedException     | 402         | Payment processing failure      |
| MethodArgumentNotValid     | 400         | Bean validation failure         |
| Exception (generic)        | 500         | Unexpected server error         |

Consistent error response format:
```json
{
  "timestamp": "2025-07-10T18:00:00",
  "message": "Error description",
  "status": 404
}
```

---

## 7. Non-Functional Requirements

| Aspect          | Current State                                    |
|-----------------|--------------------------------------------------|
| Scalability     | Single instance; horizontal scaling possible via stateless design |
| Security        | Plain-text passwords; no JWT/OAuth (future scope)|
| Performance     | Database indexes on FK columns for query optimization |
| Availability    | Single point of failure (monolith)               |
| Observability   | SQL logging enabled (`show-sql: true`)           |
| Documentation   | Swagger UI at `/swagger-ui.html`                 |

---

## 8. Deployment Architecture

```
┌─────────────────────────────────────────┐
│            Developer Machine            │
│                                         │
│  ┌───────────────────────────────────┐  │
│  │  JDK 1.8 + Gradle 8.8            │  │
│  │  gradlew.bat :book-my-ticket:bootRun│ │
│  └───────────────┬───────────────────┘  │
│                  │                      │
│  ┌───────────────▼───────────────────┐  │
│  │  Spring Boot App (Embedded Tomcat)│  │
│  │  Port: 8089                       │  │
│  └───────────────┬───────────────────┘  │
│                  │                      │
│  ┌───────────────▼───────────────────┐  │
│  │  PostgreSQL                       │  │
│  │  Port: 5432                       │  │
│  │  DB: book_my_ticket               │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
```

### Build & Run

```bash
# Build JAR
gradlew.bat :book-my-ticket:clean :book-my-ticket:bootJar

# Run JAR
java -jar book-my-ticket/build/libs/book-my-ticket-0.0.1-SNAPSHOT.jar
```

---

## 9. Future Enhancements

| Enhancement                    | Impact                                  |
|--------------------------------|-----------------------------------------|
| JWT Authentication             | Secure API endpoints                    |
| Redis Seat Locking             | Prevent race conditions during booking  |
| Payment Gateway (Stripe/Razorpay)| Real payment processing               |
| Cancel Booking API             | Allow users to cancel and release seats |
| Admin Dashboard                | Movie/show/theater management UI        |
| Caching (Redis/Caffeine)       | Improve read performance                |
| Containerization (Docker)      | Simplified deployment                   |

---
