-- =============================================
-- Sample Data for Testing
-- =============================================

-- Users
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@example.com', 'admin123', 'ADMIN'),
('John Doe', 'john@example.com', 'pass123', 'USER'),
('Jane Smith', 'jane@example.com', 'pass123', 'USER');

-- Movies
INSERT INTO movies (title, genre, duration, language) VALUES
('Inception', 'Sci-Fi', 148, 'English'),
('The Dark Knight', 'Action', 152, 'English'),
('Interstellar', 'Sci-Fi', 169, 'English');

-- Theaters
INSERT INTO theaters (name, location) VALUES
('PVR Cinemas', 'Downtown Mall'),
('INOX', 'City Center');

-- Shows
INSERT INTO shows (movie_id, theater_id, show_time, price) VALUES
(1, 1, '2025-07-15 10:00:00', 250.00),
(1, 1, '2025-07-15 14:00:00', 300.00),
(2, 2, '2025-07-15 18:00:00', 350.00);

-- Seats for Show 1
INSERT INTO seats (seat_number, show_id, status) VALUES
('A1', 1, 'AVAILABLE'), ('A2', 1, 'AVAILABLE'), ('A3', 1, 'AVAILABLE'),
('A4', 1, 'AVAILABLE'), ('A5', 1, 'AVAILABLE'),
('B1', 1, 'AVAILABLE'), ('B2', 1, 'AVAILABLE'), ('B3', 1, 'AVAILABLE'),
('B4', 1, 'AVAILABLE'), ('B5', 1, 'AVAILABLE');

-- Seats for Show 2
INSERT INTO seats (seat_number, show_id, status) VALUES
('A1', 2, 'AVAILABLE'), ('A2', 2, 'AVAILABLE'), ('A3', 2, 'AVAILABLE'),
('A4', 2, 'AVAILABLE'), ('A5', 2, 'AVAILABLE'),
('B1', 2, 'AVAILABLE'), ('B2', 2, 'AVAILABLE'), ('B3', 2, 'AVAILABLE'),
('B4', 2, 'AVAILABLE'), ('B5', 2, 'AVAILABLE');

-- Seats for Show 3
INSERT INTO seats (seat_number, show_id, status) VALUES
('A1', 3, 'AVAILABLE'), ('A2', 3, 'AVAILABLE'), ('A3', 3, 'AVAILABLE'),
('A4', 3, 'AVAILABLE'), ('A5', 3, 'AVAILABLE'),
('B1', 3, 'AVAILABLE'), ('B2', 3, 'AVAILABLE'), ('B3', 3, 'AVAILABLE'),
('B4', 3, 'AVAILABLE'), ('B5', 3, 'AVAILABLE');
