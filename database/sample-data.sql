-- ========================================================
-- Sports Complex Sample Seed Data
-- ========================================================

USE sports_complex_db;

-- 1. Demo Student Accounts
-- Passwords below are BCrypt hashes of: "Password123!"
INSERT INTO students (email, password, full_name, role) VALUES
('akhil@anurag.edu.in', '$2a$10$7ZkQxGk5jKj3G8g2Q0/GFeM4N9Q8xU/1J6K2O5P8M2K6Q2O5P8M2K', 'Akhil Sharma', 'STUDENT'),
('srijanya@anurag.edu.in', '$2a$10$7ZkQxGk5jKj3G8g2Q0/GFeM4N9Q8xU/1J6K2O5P8M2K6Q2O5P8M2K', 'Srijanya Seerapu', 'STUDENT'),
('admin@anurag.edu.in', '$2a$10$7ZkQxGk5jKj3G8g2Q0/GFeM4N9Q8xU/1J6K2O5P8M2K6Q2O5P8M2K', 'Sports Admin', 'ADMIN')
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

-- 2. Complete Courts & Facilities Lineup across 9 sports
INSERT INTO courts (name, sport, location, active) VALUES
-- Badminton
('Badminton Court 1', 'Badminton', 'Sports Complex - Indoor Hall A', TRUE),
('Badminton Court 2', 'Badminton', 'Sports Complex - Indoor Hall A', TRUE),
('Badminton Court 3', 'Badminton', 'Sports Complex - Indoor Hall B', TRUE),

-- Basketball
('Basketball Court 1', 'Basketball', 'Outdoor Court Complex', TRUE),
('Basketball Court 2', 'Basketball', 'Outdoor Court Complex', TRUE),

-- Cricket
('Cricket Ground', 'Cricket', 'Main Campus Athletics Ground', TRUE),
('Cricket Practice Nets', 'Cricket', 'Athletics Ground East', TRUE),

-- Football
('Football Ground', 'Football', 'Main Campus Athletics Ground', TRUE),
('Futsal Turf', 'Football', 'Sports Complex Turf', TRUE),

-- Tennis
('Tennis Court 1', 'Tennis', 'Sports Complex West', TRUE),
('Tennis Court 2', 'Tennis', 'Sports Complex West', TRUE),

-- Volleyball
('Volleyball Court 1', 'Volleyball', 'Sports Complex Sand Court', TRUE),
('Volleyball Court 2', 'Volleyball', 'Sports Complex Hard Court', TRUE),

-- Table Tennis
('Table Tennis Court 1', 'Table Tennis', 'Indoor Recreation Hall', TRUE),
('Table Tennis Court 2', 'Table Tennis', 'Indoor Recreation Hall', TRUE),

-- Carrom
('Carrom Board 1', 'Carrom', 'Indoor Recreation Room', TRUE),
('Carrom Board 2', 'Carrom', 'Indoor Recreation Room', TRUE),
('Carrom Board 3', 'Carrom', 'Indoor Recreation Room', TRUE),

-- Chess
('Chess Board 1', 'Chess', 'Indoor Recreation Room', TRUE),
('Chess Board 2', 'Chess', 'Indoor Recreation Room', TRUE)
ON DUPLICATE KEY UPDATE location = VALUES(location);
