-- ========================================================
-- Sports Complex Availability & Booking System Schema
-- Dialect: MySQL 8.0+ / TiDB Cloud compatible
-- ========================================================

CREATE DATABASE IF NOT EXISTS sports_complex_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE sports_complex_db;

-- 1. Students / Users Table
CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Courts & Sports Facilities Table
CREATE TABLE IF NOT EXISTS courts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    sport VARCHAR(40) NOT NULL,
    location VARCHAR(120) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_code VARCHAR(40) NOT NULL UNIQUE,
    court_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    duration_minutes INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bookings_court FOREIGN KEY (court_id) REFERENCES courts (id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT chk_duration CHECK (duration_minutes BETWEEN 15 AND 60)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================================
-- Performance & Concurrency Composite Indexes
-- ========================================================
CREATE INDEX idx_bookings_court_status_time ON bookings (court_id, status, end_time);
CREATE INDEX idx_bookings_student_status_time ON bookings (student_id, status, end_time);
CREATE INDEX idx_bookings_student_start_desc ON bookings (student_id, start_time DESC);
CREATE INDEX idx_courts_sport_active ON courts (sport, active);
