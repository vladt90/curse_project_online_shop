-- Создание базы данных для интернет-магазина
CREATE DATABASE IF NOT EXISTS online_shop_db;
USE online_shop_db;

-- Таблица ролей пользователей
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- Таблица пользователей
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Для хранения хешированных паролей
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    is_regular_customer BOOLEAN DEFAULT FALSE, -- Статус постоянного клиента
    discount DECIMAL(5, 2) DEFAULT 0, -- Персональная скидка пользователя в процентах
    total_purchases DECIMAL(10, 2) DEFAULT 0, -- Общая сумма покупок для отслеживания статуса
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE RESTRICT
);

-- Таблица товаров
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    unit VARCHAR(20) NOT NULL, -- Единица измерения (шт, кг, л)
    stock_quantity INT NOT NULL DEFAULT 0 -- Количество на складе
);

-- Таблица заказов
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delivery_date DATE,
    discount_applied DECIMAL(5, 2) DEFAULT 0, -- Применённая скидка в процентах
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT
);

-- Таблица элементов заказа (связь многие-ко-многим между заказами и товарами)
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    item_price DECIMAL(10, 2) NOT NULL, -- Цена на момент заказа
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT
);

-- Вставка начальных данных для ролей
INSERT INTO roles (role_name) VALUES ('admin'), ('client'); 