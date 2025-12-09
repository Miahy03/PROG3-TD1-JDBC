INSERT INTO product(name, price, creation_datetime) VALUES
('iPhone 14', 1200, NOW()),
('MacBook Air', 1800, NOW());

INSERT INTO product_category(name, product_id) VALUES
('Smartphone', 1),
('Laptop', 2);
