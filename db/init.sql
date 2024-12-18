-- Create the quiz database
CREATE DATABASE IF NOT EXISTS QuizSystem;

-- Use the quiz database
USE quiz_system;

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'user') NOT NULL
);

-- Create the questions table
CREATE TABLE IF NOT EXISTS questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quiz_id VARCHAR(50),
    question TEXT,
    answer TEXT
);

-- Create the results table
CREATE TABLE IF NOT EXISTS results (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    quiz_id VARCHAR(50),
    score INT,
    date_taken DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert some initial data
INSERT INTO users (username, password, role) VALUES
('admin', 'admin_password', 'admin'),
('user1', 'password1', 'user'),
('user2', 'password2', 'user');
