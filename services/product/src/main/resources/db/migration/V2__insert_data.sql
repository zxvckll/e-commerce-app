-- Insert 10 categories with IDs incremented by 50 (starting from 1)
INSERT INTO category (id, name, description)
VALUES (1, 'Electronics', 'Devices and gadgets'),
       (51, 'Books', 'Various kinds of books'),
       (101, 'Clothing', 'Apparel and fashion'),
       (151, 'Home Appliances', 'Appliances for home use'),
       (201, 'Toys', 'Toys for kids and adults'),
       (251, 'Sports', 'Sporting goods and equipment'),
       (301, 'Furniture', 'Home and office furniture'),
       (351, 'Beauty', 'Beauty and skincare products'),
       (401, 'Automotive', 'Car accessories and parts'),
       (451, 'Groceries', 'Daily essential food items');

-- Set the sequence to continue after our manual inserts
ALTER SEQUENCE category_seq RESTART WITH 501;

-- Insert 10 products with IDs incremented by 50 (starting from 1)
INSERT INTO product (id, name, description, available_quantity, price, category_id)
VALUES (1, 'Smartphone', 'Latest model smartphone', 100, 699.99, 1),
       (51, 'Laptop', 'High-performance laptop', 50, 1299.99, 1),
       (101, 'Novel', 'Bestselling fiction book', 200, 15.99, 51),
       (151, 'T-shirt', 'Cotton round-neck T-shirt', 300, 9.99, 101),
       (201, 'Microwave', 'Compact microwave oven', 80, 99.99, 151),
       (251, 'Action Figure', 'Collectible action figure', 150, 29.99, 201),
       (301, 'Football', 'Professional-grade football', 120, 39.99, 251),
       (351, 'Office Chair', 'Ergonomic office chair', 75, 199.99, 301),
       (401, 'Lipstick', 'Matte finish lipstick', 250, 19.99, 351),
       (451, 'Car Tire', 'Durable all-season tire', 60, 89.99, 401);

-- Set the sequence to continue after our manual inserts
ALTER SEQUENCE product_seq RESTART WITH 501;
