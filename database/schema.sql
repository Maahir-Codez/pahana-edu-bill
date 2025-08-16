-- Drop table if it exists to start fresh
DROP TABLE IF EXISTS users;

-- Create the users table
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       role ENUM('ADMIN', 'STAFF') NOT NULL,
                       is_active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       last_login DATETIME NULL
);

INSERT INTO users (username, password_hash, full_name, email, role)
VALUES ('admin', 'Ll1XDUvjo03U6z27XHQujsD6tBF0vbMIMC/vIaOLTvFfj0EljCKC9L5wrjfYlpZH', 'Administrator', 'admin@pahanaedu.com', 'ADMIN');