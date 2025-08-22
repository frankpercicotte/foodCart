-- Log start of script execution
SELECT '=== Starting data initialization ===' AS debug;

SELECT 'Current categories (before):' AS debug;
SELECT * FROM categories;

SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE IF EXISTS products;
TRUNCATE TABLE IF EXISTS categories;
SET REFERENTIAL_INTEGRITY TRUE;

ALTER TABLE categories ALTER COLUMN id RESTART WITH 1;

SELECT 'Categories after truncate:' AS debug;
SELECT * FROM categories;

INSERT INTO categories (id, name, profit_margin, max_discount, is_active) VALUES
(1, 'Pizza', 25.00, 10.00, true);

-- Verify the category was inserted
SELECT 'Categories after insertion:' AS debug;
SELECT * FROM categories;

-- Count categories (should be 1)
SELECT 'Category count:' AS debug, COUNT(*) AS count FROM categories;