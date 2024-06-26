CREATE DATABASE ShopApp;
USE ShopApp;
-- Khách hàng khi muốn mua hàng -> phải đăng ký tài khoản -> bảng user
CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(100) NOT NULL DEFAULT '', -- Mật khẩu đã mã hóa
    created_at DATETIME,
    updated_at DATETIME,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0
);
CREATE TABLE roles(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
);
ALTER TABLE users ADD COLUMN role_id INT;
ALTER TABLE users ADD FOREIGN KEY(role_id) REFERENCES roles(id);
CREATE TABLE tokens(
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,
    revoked TINYINT(1) NOT NULL,
    expired TINYINT(1) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
-- Hỗ trợ đăng nhập từ Facebook và Google
CREATE TABLE social_accounts(
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `provider` VARCHAR(20) NOT NULL COMMENT 'Tên nhà social network',
    `provider_id` VARCHAR(50) NOT NULL,
    `email` VARCHAR(150) NOT NULL COMMENT 'Email tài khoản',
    `name` VARCHAR(100) NOT NULL COMMENT 'Tên người dùng',
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
-- Bảng danh mục sản phẩm (Category)
CREATE TABLE categories(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'Tên danh mục'
);
-- Bảng chứa sản phẩm (Product)
CREATE TABLE products(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(350) COMMENT 'Tên sản phẩm',
    price FLOAT NOT NULL CHECK(price >= 0),
    thumbnail VARCHAR(300) DEFAULT '',
    description LONGTEXT DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
ALTER TABLE products AUTO_INCREMENT = 1;
-- Bảng ảnh sản phẩm id
CREATE TABLE product_images(
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    CONSTRAINT fk_product_images_product_id
    FOREIGN KEY (product_id) 
    REFERENCES products(id) ON DELETE CASCADE,
    image_url VARCHAR(300),
);
-- Đặt hàng - orders
CREATE TABLE orders(
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id int,
    FOREIGN KEY(user_id) REFERENCES users(id),
    fullname VARCHAR(100) DEFAULT '',
    email VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL, -- Địa chỉ gửi
    note VARCHAR(100) DEFAULT '',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP, -- Ngày đặt
    status VARCHAR(20),
    total_money FLOAT CHECK(total_money >= 0)
);
ALTER TABLE orders ADD COLUMN `shipping_method` VARCHAR(100); -- Phương thức vận chuyển
ALTER TABLE orders ADD COLUMN `shipping_address` VARCHAR(200); -- Địa chỉ nhận
ALTER TABLE orders ADD COLUMN `shipping_date` DATE; -- Ngày gửi đến
ALTER TABLE orders ADD COLUMN `tracking_number` VARCHAR(100); -- số vận đơn
ALTER TABLE orders ADD COLUMN `payment_method` VARCHAR(100);
-- Xóa 1 đơn hàng => Xóa mềm => Thêm trường active
ALTER TABLE orders ADD COLUMN `active` TINYINT(1);
-- status phải nhận các giá trị trong list có sẵn
ALTER TABLE orders
MODIFY COLUMN status ENUM('pending','processing','shipped','delivered','cancelled')
DEFAULT 'pending' COMMENT 'Trạng thái đơn hàng';
CREATE TABLE order_details(
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    price FLOAT CHECK(price >= 0), -- Giá 1 sản phẩm
    number_of_products INT CHECK(number_of_products > 0), -- Số sản phẩm
    total_money FLOAT CHECK(total_money >= 0),
    color VARCHAR(20) DEFAULT ''
);
/*
 UPDATE products SET price = ROUND(price / 1000000, 1) WHERE price > 1000000;
*/
/*
INSERT INTO coupons(id, code) VALUES (1,'HEAVEN');
INSERT INTO coupons(id, code) VALUES (2,'DISCOUNT20');

INSERT INTO coupon_conditions(id, coupon_id, attribute, operator, value, discount_amount)
VALUES (1,1,'minimum_amount','>','100',10);

INSERT INTO coupon_conditions(id, coupon_id, attribute, operator, value, discount_amount)
VALUES (2,1,'applicable_date','BETWEEN','2024-12-28',5);

INSERT INTO coupon_conditions(id, coupon_id, attribute, operator, value, discount_amount)
VALUES (3,1,'minimum_amount','>','200',20);

Nếu đơn hàng có tổng giá trị là 120 dollar và áp dụng coupon 1
Giá trị đơn hàng sau khi áp dụng giảm 10%:
120 * (1 - 10/100) = 108 dollar

Giá trị sau khi áp dụng giảm giá 5%:
108 * 0.95 = 102.6 dollar
...
*/