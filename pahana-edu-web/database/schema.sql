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

-- Drop table if it exists
DROP TABLE IF EXISTS items;

-- Create the items table
CREATE TABLE items (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       sku VARCHAR(50) NOT NULL UNIQUE,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       category VARCHAR(100),
                       author VARCHAR(100),
                       price DECIMAL(10, 2) NOT NULL,
                       stock_quantity INT NOT NULL DEFAULT 0,
                       is_active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Optional: Insert some sample data for testing
INSERT INTO items (sku, name, category, author, price, stock_quantity) VALUES
                                                                           ('BOOK-HC-001', 'The Midnight Library', 'Fiction', 'Matt Haig', 15.50, 50),
                                                                           ('BOOK-PB-002', 'Atomic Habits', 'Self-Help', 'James Clear', 12.99, 75),
                                                                           ('STAT-PEN-001', 'Gel Pens - 12 Pack', 'Stationery', NULL, 8.75, 120);


-- Drop tables if they exist (in reverse order of creation due to foreign keys)
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;

-- Create the orders table
-- This table holds the main information for each order.
CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        customer_id BIGINT NOT NULL,
                        order_date DATETIME NOT NULL,
                        status ENUM('PENDING', 'PAID', 'CANCELLED', 'COMPLETED') NOT NULL,
                        subtotal DECIMAL(10, 2) NOT NULL,
                        discount_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
                        tax_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
                        total_amount DECIMAL(10, 2) NOT NULL,
    -- Foreign key constraint to link with the customers table
                        CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Create the order_items table
-- This is a "join table" that links items to orders and stores quantity/price info.
CREATE TABLE order_items (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             item_id BIGINT NOT NULL,
                             quantity INT NOT NULL,
                             price_at_purchase DECIMAL(10, 2) NOT NULL,
                             line_total DECIMAL(10, 2) NOT NULL,
    -- Foreign key constraint to link with the orders table
                             CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    -- Foreign key constraint to link with the items table
                             CONSTRAINT fk_order_items_item FOREIGN KEY (item_id) REFERENCES items(id)
);