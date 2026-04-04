-- =============================================
-- Book My Ticket - Database Setup Script
-- =============================================

-- Create database (run separately as superuser)
-- CREATE DATABASE book_my_ticket;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
);

-- Movies table
CREATE TABLE IF NOT EXISTS movies (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    duration INTEGER,
    language VARCHAR(50)
);

-- Theaters table
CREATE TABLE IF NOT EXISTS theaters (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255)
);

-- Shows table
CREATE TABLE IF NOT EXISTS shows (
    id BIGSERIAL PRIMARY KEY,
    movie_id BIGINT NOT NULL REFERENCES movies(id),
    theater_id BIGINT NOT NULL REFERENCES theaters(id),
    show_time TIMESTAMP NOT NULL,
    price NUMERIC(10, 2) NOT NULL
);

-- Seats table
CREATE TABLE IF NOT EXISTS seats (
    id BIGSERIAL PRIMARY KEY,
    seat_number VARCHAR(10) NOT NULL,
    show_id BIGINT NOT NULL REFERENCES shows(id),
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    show_id BIGINT NOT NULL REFERENCES shows(id),
    total_amount NUMERIC(10, 2) NOT NULL,
    booking_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

-- Booking-Seats join table
CREATE TABLE IF NOT EXISTS booking_seats (
    booking_id BIGINT NOT NULL REFERENCES bookings(id),
    seat_id BIGINT NOT NULL REFERENCES seats(id),
    PRIMARY KEY (booking_id, seat_id)
);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE REFERENCES bookings(id),
    payment_mode VARCHAR(50),
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_shows_movie_id ON shows(movie_id);
CREATE INDEX IF NOT EXISTS idx_shows_theater_id ON shows(theater_id);
CREATE INDEX IF NOT EXISTS idx_seats_show_id ON seats(show_id);
CREATE INDEX IF NOT EXISTS idx_seats_status ON seats(status);
CREATE INDEX IF NOT EXISTS idx_bookings_user_id ON bookings(user_id);
CREATE INDEX IF NOT EXISTS idx_bookings_show_id ON bookings(show_id);
