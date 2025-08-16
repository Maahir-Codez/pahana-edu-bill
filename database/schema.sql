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

-- Drop table if it exists
DROP TABLE IF EXISTS customers;

-- Create the customers table
CREATE TABLE customers (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           account_number VARCHAR(20) NOT NULL UNIQUE,
                           full_name VARCHAR(100) NOT NULL,
                           address VARCHAR(255),
                           city VARCHAR(50),
                           postal_code VARCHAR(20),
                           phone_number VARCHAR(20),
                           email VARCHAR(100) NOT NULL UNIQUE,
                           date_registered DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           is_active BOOLEAN NOT NULL DEFAULT TRUE,
                           units_consumed DOUBLE NOT NULL DEFAULT 0.0
);

-- Insert some sample data for testing
INSERT INTO customers (account_number, full_name, email, phone_number, address, city, postal_code) VALUES
                                                                                                       ('CUST-001', 'John Doe', 'john.doe@example.com', '555-0101', '123 Maple Street', 'Springfield', '12345'),
                                                                                                       ('CUST-002', 'Jane Smith', 'jane.smith@example.com', '555-0102', '456 Oak Avenue', 'Shelbyville', '67890');