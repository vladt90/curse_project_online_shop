# Интернет-магазин

Курсовой проект "Интернет-магазин" на Java с использованием JavaFX и MySQL.

## Описание проекта

Интернет-магазин предоставляет следующие возможности:
- Регистрация и авторизация пользователей
- Просмотр каталога товаров с возможностью поиска
- Добавление товаров в корзину и оформление заказов
- Панель администратора для управления товарами, заказами и пользователями

## Запуск проекта

### Предварительные требования
- JDK 11 или выше
- MySQL 5.7 или выше
- Maven 3.6 или выше

### Настройка базы данных
1. Создайте базу данных и выполните скрипт `database_schema.sql` для создания структуры базы данных:
   ```bash
   mysql -u root -p < database_schema.sql
   ```

2. Настройте файл подключения к базе данных `src/main/resources/database.properties`:
   ```properties
   db.url=jdbc:mysql://localhost:3306/online_shop?useSSL=false&serverTimezone=UTC
   db.user=root
   db.password=your_password
   ```

### Запуск приложения
1. Соберите проект с помощью Maven:
   ```bash
   mvn clean package
   ```

2. Запустите приложение с графическим интерфейсом:
   ```bash
   mvn javafx:run
   ```

3. Или запустите консольную версию:
   ```bash
   java -jar target/online-shop-1.0-SNAPSHOT.jar --console
   ```

## Учётные записи для тестирования
- Администратор: login `admin`, password `admin123`
- Пользователь: login `user`, password `user123`

## Структура проекта

Проект разработан с использованием архитектуры MVC:

- **Model**: классы в пакете `com.example.onlineshop.model`
- **View**: FXML-файлы в директории `resources/fxml`
- **Controller**: классы в пакете `com.example.onlineshop.controller`

### Дополнительные пакеты
- **DAO**: классы для работы с базой данных
- **Service**: бизнес-логика приложения
- **Util**: вспомогательные классы

## Технологии
- Java 11
- JavaFX 17
- MySQL
- Maven 