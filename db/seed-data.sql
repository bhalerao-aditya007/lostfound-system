USE lostfound_db;

-- Clear existing data
DELETE FROM MATCHES;
DELETE FROM FOUND_ITEMS;
DELETE FROM LOST_ITEMS;

-- Reset auto-increment
ALTER TABLE LOST_ITEMS AUTO_INCREMENT = 1;
ALTER TABLE FOUND_ITEMS AUTO_INCREMENT = 1;
ALTER TABLE MATCHES AUTO_INCREMENT = 1;

-- Insert sample lost items
INSERT INTO LOST_ITEMS (item_name, color, location, description, contact_info) VALUES
('iPhone 13', 'Blue', 'Library', 'Blue iPhone 13 with cracked screen protector', 'john@email.com'),
('Wallet', 'Brown', 'Cafeteria', 'Brown leather wallet with student ID', 'sarah@email.com'),
('Keys', 'Silver', 'Parking Lot A', 'Car keys with Toyota keychain', 'mike@email.com'),
('Backpack', 'Black', 'Gym', 'Black Nike backpack with laptop inside', 'emma@email.com'),
('Glasses', 'Black', 'Classroom 101', 'Black frame prescription glasses', 'david@email.com');

-- Insert sample found items (some matching, some not)
INSERT INTO FOUND_ITEMS (item_name, color, location, description, finder_info) VALUES
('iPhone 13', 'blue', 'Library', 'Found blue iPhone with damaged screen', 'jane@email.com'),
('Wallet', 'Brown', 'Cafeteria', 'Leather wallet found on table', 'tom@email.com'),
('Watch', 'Silver', 'Gym', 'Silver sports watch', 'lisa@email.com'),
('Backpack', 'Black', 'Gym', 'Black backpack with Nike logo', 'alex@email.com'),
('Sunglasses', 'Black', 'Library', 'Black Ray-Ban sunglasses', 'chris@email.com');

-- Display inserted data
SELECT 'Sample data inserted successfully!' AS Status;
SELECT COUNT(*) AS 'Lost Items' FROM LOST_ITEMS;
SELECT COUNT(*) AS 'Found Items' FROM FOUND_ITEMS;

-- Show sample data
SELECT * FROM LOST_ITEMS;
SELECT * FROM FOUND_ITEMS;
