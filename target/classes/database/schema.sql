-- Create database if not exists
CREATE DATABASE IF NOT EXISTS billing_system;
USE billing_system;

-- Profiles table
CREATE TABLE IF NOT EXISTS rate_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    base_price DECIMAL(10,2) NOT NULL
);

-- Service Packages table
CREATE TABLE IF NOT EXISTS service_packages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    rate_plan_id BIGINT NOT NULL,
    is_recurring BOOLEAN NOT NULL,
    free_units INT,
    FOREIGN KEY (rate_plan_id) REFERENCES rate_plans(id)
);

-- Services table
CREATE TABLE IF NOT EXISTS services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    type VARCHAR(20) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    unit_type VARCHAR(20) NOT NULL,
    service_package_id BIGINT,
    FOREIGN KEY (service_package_id) REFERENCES service_packages(id)
);

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    rate_plan_id BIGINT,
    FOREIGN KEY (rate_plan_id) REFERENCES rate_plans(id)
);

-- Service Subscriptions table
CREATE TABLE IF NOT EXISTS service_subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    service_package_id BIGINT NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME,
    active BOOLEAN NOT NULL,
    remaining_free_units INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (service_package_id) REFERENCES service_packages(id)
);

-- CDRs table
CREATE TABLE IF NOT EXISTS cdrs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    usage_amount BIGINT NOT NULL,
    external_charges DECIMAL(10,2),
    rated_amount DECIMAL(10,2),
    invoice_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (service_id) REFERENCES services(id),
    FOREIGN KEY (invoice_id) REFERENCES invoices(id)
);

-- Invoices table
CREATE TABLE IF NOT EXISTS invoices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    invoice_number VARCHAR(100) NOT NULL UNIQUE,
    issue_date DATETIME NOT NULL,
    due_date DATETIME NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    tax_amount DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    pdf_path VARCHAR(255),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
); 