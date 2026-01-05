-- SQL script to initialize the database schema for the order inventory service

-- Create the orders table
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the inventory table
CREATE TABLE inventory (
    product_id INT PRIMARY KEY,
    available INT NOT NULL,
    reserved INT NOT NULL
);

-- Add a sample order
INSERT INTO orders (customer_id, status) VALUES (1, 'CREATED');

-- Add sample inventory data
INSERT INTO inventory (product_id, available, reserved) VALUES (1, 100, 0);