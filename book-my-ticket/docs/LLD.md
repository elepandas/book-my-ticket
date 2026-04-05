# 🔧 Low-Level Design (LLD) — Book My Ticket

## 1. Project Structure

```
publicissapient/                         ← Parent Gradle project
├── build.gradle                         ← Plugin version declarations
├── settings.gradle                      ← include 'book-my-ticket'
└── book-my-ticket/                      ← Spring Boot submodule
    ├── build.gradle                     ← Dependencies & plugin application
    ├── gradlew / gradlew.bat
    └── src/main/
        ├── java/com/ticketbooking/
        │   ├── BookMyTicketApplication.java
        │   ├── config/
        │   │   └── OpenApiConfig.java
        │   ├── controller/              ← REST endpoints
        │   ├── service/                 ← Business logic
        │   ├── repository/              ← Data access (JPA)
        │   ├── model/                   ← JPA entities
        │   │   └── enums/               ← Status enums
        │   ├── dto/                     ← Request/Response DTOs
        │   └── exception/               ← Custom exceptions & handler
        └── resources/
            ├── application.yml
            └── db/
                ├── init.sql
                └── sample-data.sql
```

---

## 2. Class Diagrams

### 2.1 Entity (Model) Classes

```
┌─────────────────────────────┐
│         User                │
│─────────────────────────────│
│ - id: Long                  │
│ - name: String              │
│ - email: String (unique)    │
│ - password: String          │
│ - role: Role (enum)         │
│─────────────────────────────│
│ + getters/setters           │
└─────────────────────────────┘

┌─────────────────────────────┐
│         Movie               │
│─────────────────────────────│
│ - id: Long                  │
│ - title: String             │
│ - genre: String             │
│ - duration: Integer         │
│ - language: String          │
│─────────────────────────────│
│ + getters/setters           │
└─────────────────────────────┘

┌─────────────────────────────┐
│        Theater              │
│─────────────────────────────│
│ - id: Long                  │
│ - name: String              │
│ - location: String          │
│─────────────────────────────│
│ + getters/setters           │
└─────────────────────────────┘

┌──────────────────────────────────┐
│            Show                  │
│──────────────────────────────────│
│ - id: Long                       │
│ - movie: Movie (ManyToOne)       │
│ - theater: Theater (ManyToOne)   │
│ - showTime: LocalDateTime        │
│ - price: BigDecimal              │
│──────────────────────────────────│
│ + getters/setters                │
└──────────────────────────────────┘

┌──────────────────────────────────┐
│            Seat                  │
│──────────────────────────────────│
│ - id: Long                       │
│ - seatNumber: String             │
│ - show: Show (ManyToOne)         │
│ - status: SeatStatus (enum)      │
│──────────────────────────────────│
│ + getters/setters                │
└──────────────────────────────────┘

┌──────────────────────────────────────┐
│           Booking                    │
│──────────────────────────────────────│
│ - id: Long                           │
│ - user: User (ManyToOne)             │
│ - show: Show (ManyToOne)             │
│ - seats: List<Seat> (ManyToMany)     │
│ - totalAmount: BigDecimal            │
│ - bookingTime: LocalDateTime         │
│ - status: BookingStatus (enum)       │
│──────────────────────────────────────│
│ + getters/setters                    │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│           Payment                    │
│──────────────────────────────────────│
│ - id: Long                           │
│ - booking: Booking (OneToOne)        │
│ - paymentMode: String                │
│ - paymentStatus: PaymentStatus (enum)│
│──────────────────────────────────────│
│ + getters/setters                    │
└──────────────────────────────────────┘
```

### 2.2 Enums

```
┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│    Role      │  │  SeatStatus  │  │BookingStatus │  │PaymentStatus │
│──────────────│  │──────────────│  │──────────────│  │──────────────│
│ USER         │  │ AVAILABLE    │  │ PENDING      │  │ PENDING      │
│ ADMIN        │  │ BOOKED       │  │ CONFIRMED    │  │ SUCCESS      │
└──────────────┘  └──────────────┘  │ CANCELLED    │  │ FAILED       │
                                    └──────────────┘  └──────────────┘
```

### 2.3 DTO Classes

```
┌─────────────────────────────────┐
│           UserDto               │
│─────────────────────────────────│
│ - id: Long                      │
│ - name: String (@NotBlank)      │
│ - email: String (@Email)        │
│ - password: String (@NotBlank)  │
│ - role: String                  │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│          LoginDto               │
│─────────────────────────────────│
│ - email: String (@Email)        │
│ - password: String (@NotBlank)  │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│          MovieDto               │
│─────────────────────────────────│
│ - id: Long                      │
│ - title: String (@NotBlank)     │
│ - genre: String                 │
│ - duration: Integer             │
│ - language: String              │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│         TheaterDto              │
│─────────────────────────────────│
│ - id: Long                      │
│ - name: String (@NotBlank)      │
│ - location: String              │
└─────────────────────────────────┘

┌─────────────────────────────────────┐
│            ShowDto                  │
│─────────────────────────────────────│
│ - id: Long                          │
│ - movieId: Long (@NotNull)          │
│ - theaterId: Long (@NotNull)        │
│ - showTime: LocalDateTime (@NotNull)│
│ - price: BigDecimal (@NotNull)      │
│ - movieTitle: String                │
│ - theaterName: String               │
└─────────────────────────────────────┘

┌─────────────────────────────────┐
│           SeatDto               │
│─────────────────────────────────│
│ - id: Long                      │
│ - seatNumber: String            │
│ - showId: Long                  │
│ - status: String                │
└─────────────────────────────────┘

┌──────────────────────────────────────┐
│          BookingDto                  │
│──────────────────────────────────────│
│ - id: Long                           │
│ - userId: Long (@NotNull)            │
│ - showId: Long (@NotNull)            │
│ - seatIds: List<Long> (@NotEmpty)    │
│ - totalAmount: BigDecimal            │
│ - bookingTime: LocalDateTime         │
│ - status: String                     │
└──────────────────────────────────────┘

┌─────────────────────────────────────┐
│          PaymentDto                 │
│─────────────────────────────────────│
│ - id: Long                          │
│ - bookingId: Long (@NotNull)        │
│ - paymentMode: String (@NotBlank)   │
│ - paymentStatus: String             │
└─────────────────────────────────────┘
```

---

## 3. Layer-wise Detailed Design

### 3.1 Controller Layer

Each controller is a `@RestController` with constructor-based dependency injection.

#### UserController (`/api/users`)

| Method | Endpoint              | HTTP | Input          | Output         | Status |
|--------|-----------------------|------|----------------|----------------|--------|
| register | `/register`         | POST | @Valid UserDto | UserDto        | 201    |
| login    | `/login`            | POST | @Valid LoginDto| UserDto        | 200    |
| getUser  | `/{userId}`         | GET  | @PathVariable  | UserDto        | 200    |

#### MovieController (`/api/movies`)

| Method      | Endpoint        | HTTP   | Input          | Output         | Status |
|-------------|-----------------|--------|----------------|----------------|--------|
| getAllMovies | `/`            | GET    | —              | List\<MovieDto\>| 200   |
| getMovie    | `/{movieId}`   | GET    | @PathVariable  | MovieDto       | 200    |
| createMovie | `/`            | POST   | @Valid MovieDto | MovieDto       | 201    |
| updateMovie | `/{movieId}`   | PUT    | @PathVariable, @Valid MovieDto | MovieDto | 200 |
| deleteMovie | `/{movieId}`   | DELETE | @PathVariable  | void           | 204    |

#### TheaterController (`/api/theaters`)

| Method        | Endpoint | HTTP | Input             | Output            | Status |
|---------------|----------|------|-------------------|-------------------|--------|
| getAllTheaters | `/`      | GET  | —                 | List\<TheaterDto\>| 200    |
| createTheater | `/`      | POST | @Valid TheaterDto | TheaterDto        | 201    |

#### ShowController (`/api/shows`)

| Method          | Endpoint              | HTTP | Input          | Output           | Status |
|-----------------|-----------------------|------|----------------|------------------|--------|
| getShowsByMovie | `/movie/{movieId}`    | GET  | @PathVariable  | List\<ShowDto\>  | 200    |
| createShow      | `/`                   | POST | @Valid ShowDto | ShowDto          | 201    |
| getAvailableSeats| `/{showId}/seats`    | GET  | @PathVariable  | List\<SeatDto\>  | 200    |

#### BookingController (`/api/bookings`)

| Method           | Endpoint           | HTTP | Input             | Output             | Status |
|------------------|--------------------|------|-------------------|--------------------|--------|
| createBooking    | `/`                | POST | @Valid BookingDto | BookingDto         | 201    |
| getBookingsByUser| `/user/{userId}`   | GET  | @PathVariable     | List\<BookingDto\> | 200    |

#### PaymentController (`/api/payments`)

| Method         | Endpoint | HTTP | Input              | Output     | Status |
|----------------|----------|------|--------------------|------------|--------|
| processPayment | `/`      | POST | @Valid PaymentDto  | PaymentDto | 201    |

---

### 3.2 Service Layer

All services are `@Service` beans with constructor-based injection.

#### UserService

```
+ register(UserDto): UserDto
    → Check email uniqueness via userRepository.existsByEmail()
    → Map DTO → Entity, save, return DTO
    → Throws: InvalidBookingException (email exists)

+ login(LoginDto): UserDto
    → Find user by email
    → Compare plain-text password
    → Throws: ResourceNotFoundException, RuntimeException (bad credentials)

+ getUserById(Long): UserDto
    → Throws: ResourceNotFoundException
```

#### MovieService

```
+ getAllMovies(): List<MovieDto>
+ getMovieById(Long): MovieDto          → Throws: ResourceNotFoundException
+ createMovie(MovieDto): MovieDto
+ updateMovie(Long, MovieDto): MovieDto → Throws: ResourceNotFoundException
+ deleteMovie(Long): void               → Throws: ResourceNotFoundException
```

#### TheaterService

```
+ getAllTheaters(): List<TheaterDto>
+ createTheater(TheaterDto): TheaterDto
+ findById(Long): Theater               → Throws: ResourceNotFoundException
```

#### ShowService

```
+ getShowsByMovie(Long): List<ShowDto>
    → Validates movie exists
    → Throws: ResourceNotFoundException

+ createShow(ShowDto): ShowDto           → @Transactional
    → Validates movie and theater exist
    → Throws: ResourceNotFoundException

+ getAvailableSeats(Long): List<SeatDto>
    → Queries seats with status = AVAILABLE
    → Throws: ResourceNotFoundException
```

#### BookingService

```
+ createBooking(BookingDto): BookingDto  → @Transactional
    → Validate user, show, seats exist
    → Check each seat: status != BOOKED, belongs to correct show
    → Mark seats as BOOKED
    → Calculate totalAmount = price × seat count
    → Create booking with status PENDING
    → Throws: ResourceNotFoundException, InvalidBookingException,
              SeatAlreadyBookedException

+ getBookingsByUser(Long): List<BookingDto>
    → Throws: ResourceNotFoundException (user not found)
```

#### PaymentService

```
+ processPayment(PaymentDto): PaymentDto → @Transactional
    → Find booking by ID
    → Check booking not already CONFIRMED
    → Mock payment: Random(100) < 80 → success
    → On success: payment=SUCCESS, booking=CONFIRMED
    → On failure: payment=FAILED, booking=CANCELLED
    → Throws: ResourceNotFoundException, PaymentFailedException
```

---

### 3.3 Repository Layer

All repositories extend `JpaRepository<Entity, Long>`.

| Repository          | Custom Methods                                          |
|---------------------|---------------------------------------------------------|
| UserRepository      | `findByEmail(String): Optional<User>`                   |
|                     | `existsByEmail(String): boolean`                        |
| MovieRepository     | — (inherits CRUD)                                       |
| TheaterRepository   | — (inherits CRUD)                                       |
| ShowRepository      | `findByMovieId(Long): List<Show>`                       |
| SeatRepository      | `findByShowId(Long): List<Seat>`                        |
|                     | `findByShowIdAndStatus(Long, SeatStatus): List<Seat>`   |
| BookingRepository   | `findByUserId(Long): List<Booking>`                     |
| PaymentRepository   | — (inherits CRUD)                                       |

---

### 3.4 Exception Handling

#### Custom Exceptions

| Exception                  | Extends          | Usage                          |
|----------------------------|------------------|--------------------------------|
| ResourceNotFoundException  | RuntimeException | Entity not found               |
| SeatAlreadyBookedException | RuntimeException | Seat status is BOOKED          |
| InvalidBookingException    | RuntimeException | Invalid booking data/state     |
| PaymentFailedException     | RuntimeException | Mock payment failure           |

#### GlobalExceptionHandler (`@RestControllerAdvice`)

```
@ExceptionHandler(ResourceNotFoundException)    → 404 NOT_FOUND
@ExceptionHandler(SeatAlreadyBookedException)   → 409 CONFLICT
@ExceptionHandler(InvalidBookingException)      → 400 BAD_REQUEST
@ExceptionHandler(PaymentFailedException)       → 402 PAYMENT_REQUIRED
@ExceptionHandler(MethodArgumentNotValidException) → 400 BAD_REQUEST
@ExceptionHandler(Exception)                    → 500 INTERNAL_SERVER_ERROR
```

#### ApiError (Response Body)

```
{
  "timestamp": LocalDateTime,
  "message": String,
  "status": int
}
```

---

## 4. Database Schema (DDL)

### 4.1 Table Definitions

```sql
-- Users
CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20) NOT NULL DEFAULT 'USER'
);

-- Movies
CREATE TABLE movies (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    genre       VARCHAR(100),
    duration    INTEGER,
    language    VARCHAR(50)
);

-- Theaters
CREATE TABLE theaters (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    location    VARCHAR(255)
);

-- Shows
CREATE TABLE shows (
    id          BIGSERIAL PRIMARY KEY,
    movie_id    BIGINT NOT NULL REFERENCES movies(id),
    theater_id  BIGINT NOT NULL REFERENCES theaters(id),
    show_time   TIMESTAMP NOT NULL,
    price       NUMERIC(10,2) NOT NULL
);

-- Seats
CREATE TABLE seats (
    id            BIGSERIAL PRIMARY KEY,
    seat_number   VARCHAR(10) NOT NULL,
    show_id       BIGINT NOT NULL REFERENCES shows(id),
    status        VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
);

-- Bookings
CREATE TABLE bookings (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL REFERENCES users(id),
    show_id       BIGINT NOT NULL REFERENCES shows(id),
    total_amount  NUMERIC(10,2) NOT NULL,
    booking_time  TIMESTAMP NOT NULL,
    status        VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

-- Booking ↔ Seats (Join Table)
CREATE TABLE booking_seats (
    booking_id  BIGINT NOT NULL REFERENCES bookings(id),
    seat_id     BIGINT NOT NULL REFERENCES seats(id),
    PRIMARY KEY (booking_id, seat_id)
);

-- Payments
CREATE TABLE payments (
    id              BIGSERIAL PRIMARY KEY,
    booking_id      BIGINT NOT NULL UNIQUE REFERENCES bookings(id),
    payment_mode    VARCHAR(50),
    payment_status  VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);
```

### 4.2 Indexes

```sql
CREATE INDEX idx_shows_movie_id    ON shows(movie_id);
CREATE INDEX idx_shows_theater_id  ON shows(theater_id);
CREATE INDEX idx_seats_show_id     ON seats(show_id);
CREATE INDEX idx_seats_status      ON seats(status);
CREATE INDEX idx_bookings_user_id  ON bookings(user_id);
CREATE INDEX idx_bookings_show_id  ON bookings(show_id);
```

### 4.3 JPA ↔ Table Mapping

| Entity  | Table          | Strategy              |
|---------|----------------|-----------------------|
| User    | `users`        | IDENTITY generation   |
| Movie   | `movies`       | IDENTITY generation   |
| Theater | `theaters`     | IDENTITY generation   |
| Show    | `shows`        | IDENTITY generation   |
| Seat    | `seats`        | IDENTITY generation   |
| Booking | `bookings`     | IDENTITY generation   |
| Payment | `payments`     | IDENTITY generation   |

---

## 5. Sequence Diagrams

### 5.1 User Registration

```
Client              UserController         UserService          UserRepository
  │                      │                      │                     │
  │ POST /api/users/register                    │                     │
  │ {name,email,password,role}                  │                     │
  │─────────────────────►│                      │                     │
  │                      │  register(UserDto)   │                     │
  │                      │─────────────────────►│                     │
  │                      │                      │ existsByEmail(email) │
  │                      │                      │────────────────────►│
  │                      │                      │◄────────────────────│
  │                      │                      │ [false]             │
  │                      │                      │ save(user)          │
  │                      │                      │────────────────────►│
  │                      │                      │◄────────────────────│
  │                      │◄─────────────────────│ UserDto             │
  │◄─────────────────────│ 201 Created          │                     │
```

### 5.2 Create Booking (Core Flow)

```
Client          BookingController    BookingService     UserRepo  ShowRepo  SeatRepo  BookingRepo
  │                   │                   │               │         │         │          │
  │ POST /api/bookings│                   │               │         │         │          │
  │ {userId,showId,   │                   │               │         │         │          │
  │  seatIds:[1,2]}   │                   │               │         │         │          │
  │──────────────────►│                   │               │         │         │          │
  │                   │ createBooking(dto)│               │         │         │          │
  │                   │──────────────────►│               │         │         │          │
  │                   │                   │ findById(userId)        │         │          │
  │                   │                   │──────────────►│         │         │          │
  │                   │                   │◄──────────────│ User    │         │          │
  │                   │                   │               │         │         │          │
  │                   │                   │ findById(showId)        │         │          │
  │                   │                   │──────────────────────►│         │          │
  │                   │                   │◄──────────────────────│ Show    │          │
  │                   │                   │               │         │         │          │
  │                   │                   │ findAllById(seatIds)   │         │          │
  │                   │                   │────────────────────────────────►│          │
  │                   │                   │◄────────────────────────────────│ Seats    │
  │                   │                   │                                 │          │
  │                   │                   │ ── for each seat ──             │          │
  │                   │                   │   check status != BOOKED        │          │
  │                   │                   │   check seat.show == show       │          │
  │                   │                   │   seat.setStatus(BOOKED)        │          │
  │                   │                   │                                 │          │
  │                   │                   │ saveAll(seats)                  │          │
  │                   │                   │────────────────────────────────►│          │
  │                   │                   │                                 │          │
  │                   │                   │ totalAmount = price × count     │          │
  │                   │                   │ booking.status = PENDING        │          │
  │                   │                   │ save(booking)                   │          │
  │                   │                   │───────────────────────────────────────────►│
  │                   │                   │◄───────────────────────────────────────────│
  │                   │◄──────────────────│ BookingDto                      │          │
  │◄──────────────────│ 201 Created       │                                 │          │
```

### 5.3 Process Payment

```
Client          PaymentController    PaymentService     BookingRepo    PaymentRepo
  │                   │                   │                  │              │
  │ POST /api/payments│                   │                  │              │
  │ {bookingId,       │                   │                  │              │
  │  paymentMode}     │                   │                  │              │
  │──────────────────►│                   │                  │              │
  │                   │ processPayment(dto)                  │              │
  │                   │──────────────────►│                  │              │
  │                   │                   │ findById(bookingId)             │
  │                   │                   │─────────────────►│              │
  │                   │                   │◄─────────────────│ Booking      │
  │                   │                   │                  │              │
  │                   │                   │ check status != CONFIRMED       │
  │                   │                   │                  │              │
  │                   │                   │ random(100) < 80 │              │
  │                   │                   │                  │              │
  │                   │                   │ ── if SUCCESS ── │              │
  │                   │                   │ booking.status = CONFIRMED      │
  │                   │                   │ payment.status = SUCCESS        │
  │                   │                   │                  │              │
  │                   │                   │ ── if FAILED ──  │              │
  │                   │                   │ booking.status = CANCELLED      │
  │                   │                   │ payment.status = FAILED         │
  │                   │                   │                  │              │
  │                   │                   │ save(booking)    │              │
  │                   │                   │─────────────────►│              │
  │                   │                   │ save(payment)    │              │
  │                   │                   │────────────────────────────────►│
  │                   │                   │◄────────────────────────────────│
  │                   │◄──────────────────│ PaymentDto / PaymentFailedException
  │◄──────────────────│ 201 or 402        │                  │              │
```

---

## 6. Configuration

### 6.1 application.yml

```yaml
server:
  port: 8089

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
```

### 6.2 Gradle Dependencies

| Dependency                              | Purpose                    |
|-----------------------------------------|----------------------------|
| spring-boot-starter-web                 | REST API, embedded Tomcat  |
| spring-boot-starter-data-jpa            | JPA/Hibernate ORM          |
| spring-boot-starter-validation          | Bean validation (JSR 380)  |
| springdoc-openapi-ui:1.7.0             | Swagger UI & OpenAPI docs  |
| postgresql (runtime)                    | PostgreSQL JDBC driver     |
| spring-boot-starter-test (test)         | JUnit 5, Mockito           |

### 6.3 OpenApiConfig

```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI bookMyTicketOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book My Ticket API")
                        .description("Movie & Show Ticket Booking System")
                        .version("1.0.0"));
    }
}
```

### 6.4 API Documentation URLs

| Resource     | URL                                      |
|--------------|------------------------------------------|
| Swagger UI   | http://localhost:8089/swagger-ui.html     |
| OpenAPI JSON | http://localhost:8089/v3/api-docs         |
| OpenAPI YAML | http://localhost:8089/v3/api-docs.yaml    |

---

## 7. Validation Rules

| DTO        | Field       | Constraint                |
|------------|-------------|---------------------------|
| UserDto    | name        | @NotBlank                 |
| UserDto    | email       | @NotBlank, @Email         |
| UserDto    | password    | @NotBlank                 |
| LoginDto   | email       | @NotBlank, @Email         |
| LoginDto   | password    | @NotBlank                 |
| MovieDto   | title       | @NotBlank                 |
| TheaterDto | name        | @NotBlank                 |
| ShowDto    | movieId     | @NotNull                  |
| ShowDto    | theaterId   | @NotNull                  |
| ShowDto    | showTime    | @NotNull                  |
| ShowDto    | price       | @NotNull                  |
| BookingDto | userId      | @NotNull                  |
| BookingDto | showId      | @NotNull                  |
| BookingDto | seatIds     | @NotEmpty                 |
| PaymentDto | bookingId   | @NotNull                  |
| PaymentDto | paymentMode | @NotBlank                 |

---

## 8. Transaction Boundaries

| Method                          | @Transactional | Reason                                    |
|---------------------------------|----------------|-------------------------------------------|
| BookingService.createBooking()  | Yes            | Seat update + booking save must be atomic |
| ShowService.createShow()        | Yes            | Show creation with FK validation          |
| PaymentService.processPayment() | Yes            | Payment + booking status update atomic    |

---

## 9. State Transitions

### 9.1 Seat Status

```
AVAILABLE ──[createBooking]──► BOOKED
```

### 9.2 Booking Status

```
                    ┌──[payment SUCCESS]──► CONFIRMED
PENDING ────────────┤
                    └──[payment FAILED]───► CANCELLED
```

### 9.3 Payment Status

```
PENDING ──[processPayment]──► SUCCESS
                          └──► FAILED
```

---
