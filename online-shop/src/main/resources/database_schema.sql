-- Создание таблицы ролей
CREATE TABLE IF NOT EXISTS roles (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE
);

-- Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  role_id INT NOT NULL,
  login VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  last_name VARCHAR(100),
  first_name VARCHAR(100),
  patronymic VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  phone VARCHAR(20),
  status VARCHAR(50),
  discount DECIMAL(5, 2) DEFAULT 0.00,
  total_purchases DOUBLE DEFAULT 0.0,
  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Создание таблицы товаров
CREATE TABLE IF NOT EXISTS products (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price DECIMAL(10, 2) NOT NULL,
  unit VARCHAR(20) NOT NULL,
  stock_quantity INT NOT NULL
);

-- Создание таблицы заказов
CREATE TABLE IF NOT EXISTS orders (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  delivery_date DATE,
  total_cost DECIMAL(10, 2) NOT NULL,
  status VARCHAR(50) DEFAULT 'новый',
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Создание таблицы элементов заказа
CREATE TABLE IF NOT EXISTS order_items (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT NOT NULL,
  product_id INT NOT NULL,
  quantity INT NOT NULL,
  price_per_unit DECIMAL(10, 2) NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Вставка начальных данных для ролей
INSERT IGNORE INTO roles (id, name) VALUES
(1, 'Администратор'),
(2, 'Клиент');

-- Вставка тестового администратора (пароль: admin123)
INSERT IGNORE INTO users (id, role_id, login, password_hash, last_name, first_name, email, status)
VALUES (1, 1, 'admin', 'admin123', 'Админов', 'Админ', 'admin@example.com', 'активен');

-- Вставка тестового клиента (пароль: user123)
INSERT IGNORE INTO users (id, role_id, login, password_hash, last_name, first_name, email, status)
VALUES (2, 2, 'user', 'user123', 'Иванов', 'Иван', 'user@example.com', 'активен');

-- Вставка тестовых товаров
INSERT IGNORE INTO products (name, description, price, unit, stock_quantity) VALUES
('Смартфон XYZ', 'Мощный смартфон с большим экраном и отличной камерой', 29999.99, 'шт.', 25),
('Ноутбук ABC', 'Легкий и производительный ноутбук для работы и учебы', 54999.99, 'шт.', 10),
('Планшет 123', 'Компактный планшет с ярким дисплеем и долгой автономной работой', 18999.99, 'шт.', 15),
('Умные часы Smart', 'Умные часы с функцией измерения пульса и отслеживанием активности', 12999.99, 'шт.', 30),
('Беспроводные наушники', 'Беспроводные наушники с шумоподавлением и отличным звуком', 8999.99, 'шт.', 50); 