-- Insert sample rate plans
INSERT INTO rate_plans (name, description, base_price) VALUES
('Basic Plan', 'Basic plan with essential services', 50.00),
('Standard Plan', 'Standard plan with moderate services', 75.00),
('Premium Plan', 'Premium plan with advanced services', 100.00),
('Business Plan', 'Business plan with enterprise services', 150.00);

-- Insert sample service packages
INSERT INTO service_packages (name, description, price, rate_plan_id, is_recurring, free_units) VALUES
('Voice Basic', 'Basic voice package', 30.00, 1, true, 100),
('Voice Premium', 'Premium voice package', 50.00, 2, true, 300),
('Data Basic', 'Basic data package', 40.00, 1, true, 1024),
('Data Premium', 'Premium data package', 70.00, 2, true, 5120),
('SMS Basic', 'Basic SMS package', 20.00, 1, true, 50),
('SMS Premium', 'Premium SMS package', 35.00, 2, true, 200);

-- Insert sample services
INSERT INTO services (name, description, type, unit_price, unit_type, service_package_id) VALUES
('Local Calls', 'Local voice calls', 'VOICE', 0.50, 'Minutes', 1),
('International Calls', 'International voice calls', 'VOICE', 2.00, 'Minutes', 2),
('4G Data', '4G data service', 'DATA', 0.10, 'MB', 3),
('5G Data', '5G data service', 'DATA', 0.15, 'MB', 4),
('Local SMS', 'Local text messages', 'SMS', 0.20, 'Messages', 5),
('International SMS', 'International text messages', 'SMS', 0.50, 'Messages', 6);

-- Insert sample customers
INSERT INTO customers (name, phone_number, email, address, rate_plan_id) VALUES
('John Doe', '00201221234567', 'john@example.com', '123 Main St', 1),
('Jane Smith', '00201221234568', 'jane@example.com', '456 Oak St', 2),
('Bob Johnson', '00201221234569', 'bob@example.com', '789 Pine St', 3);

-- Insert sample service subscriptions
INSERT INTO service_subscriptions (customer_id, service_package_id, start_date, active, remaining_free_units) VALUES
(1, 1, NOW(), true, 100),
(1, 3, NOW(), true, 1024),
(2, 2, NOW(), true, 300),
(2, 4, NOW(), true, 5120); 