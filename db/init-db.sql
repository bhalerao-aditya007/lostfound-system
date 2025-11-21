-- Database Initialization Script
CREATE DATABASE IF NOT EXISTS lostfound_db;

USE lostfound_db;

-- Drop existing tables
DROP TABLE IF EXISTS MATCHES;
DROP TABLE IF EXISTS FOUND_ITEMS;
DROP TABLE IF EXISTS LOST_ITEMS;

-- Create LOST_ITEMS table
CREATE TABLE LOST_ITEMS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    color VARCHAR(50),
    location VARCHAR(200),
    description TEXT,
    contact_info VARCHAR(200) NOT NULL,
    reported_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_item_name (item_name),
    INDEX idx_location (location),
    INDEX idx_reported_date (reported_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create FOUND_ITEMS table
CREATE TABLE FOUND_ITEMS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    color VARCHAR(50),
    location VARCHAR(200),
    description TEXT,
    finder_info VARCHAR(200) NOT NULL,
    found_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_item_name (item_name),
    INDEX idx_location (location),
    INDEX idx_found_date (found_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create MATCHES table
CREATE TABLE MATCHES (
    match_id INT AUTO_INCREMENT PRIMARY KEY,
    lost_item_id INT NOT NULL,
    found_item_id INT NOT NULL,
    score INT NOT NULL CHECK (score >= 0 AND score <= 100),
    match_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'CONFIRMED', 'REJECTED') DEFAULT 'PENDING',
    FOREIGN KEY (lost_item_id) REFERENCES LOST_ITEMS(id) ON DELETE CASCADE,
    FOREIGN KEY (found_item_id) REFERENCES FOUND_ITEMS(id) ON DELETE CASCADE,
    INDEX idx_lost_item (lost_item_id),
    INDEX idx_found_item (found_item_id),
    INDEX idx_status (status),
    INDEX idx_score (score)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT 'Database created successfully!' AS Status;