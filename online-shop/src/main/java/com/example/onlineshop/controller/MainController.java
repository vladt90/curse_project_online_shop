package com.example.onlineshop.controller;

import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.OrderItem;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.User;
import com.example.onlineshop.service.OrderService;
import com.example.onlineshop.service.ProductService;
import com.example.onlineshop.service.UserService;
import com.example.onlineshop.service.impl.OrderServiceImpl;
import com.example.onlineshop.service.impl.ProductServiceImpl;
import com.example.onlineshop.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MainController {
    
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final Scanner scanner;
    private User currentUser;
    
    public MainController() {
        this.userService = new UserServiceImpl();
        this.productService = new ProductServiceImpl();
        this.orderService = new OrderServiceImpl();
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }
    
    public void start() {
        boolean running = true;
        
        while (running) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                if (currentUser.getRoleId() == 1) {
                    showAdminMenu();
                } else {
                    showClientMenu();
                }
            }
            
            System.out.print("Выберите действие: ");
            int choice = readInt();
            
            if (currentUser == null) {
                running = processLoginMenuChoice(choice);
            } else {
                if (currentUser.getRoleId() == 1) {
                    running = processAdminMenuChoice(choice);
                } else {
                    running = processClientMenuChoice(choice);
                }
            }
        }
        
        System.out.println("Спасибо за использование программы!");
    }
    
    private void showLoginMenu() {
        System.out.println("\n=== МЕНЮ АВТОРИЗАЦИИ ===");
        System.out.println("1. Вход в систему");
        System.out.println("2. Регистрация");
        System.out.println("0. Выход");
    }
    
    private void showAdminMenu() {
        System.out.println("\n=== МЕНЮ АДМИНИСТРАТОРА ===");
        System.out.println("1. Управление товарами");
        System.out.println("2. Управление пользователями");
        System.out.println("3. Управление заказами");
        System.out.println("4. Статистика");
        System.out.println("9. Выход из аккаунта");
        System.out.println("0. Выход из программы");
    }
    
    private void showClientMenu() {
        System.out.println("\n=== МЕНЮ ПОЛЬЗОВАТЕЛЯ ===");
        System.out.println("1. Просмотр каталога товаров");
        System.out.println("2. Поиск товаров");
        System.out.println("3. Корзина");
        System.out.println("4. Мои заказы");
        System.out.println("5. Личный кабинет");
        System.out.println("9. Выход из аккаунта");
        System.out.println("0. Выход из программы");
    }
    
    private boolean processLoginMenuChoice(int choice) {
        switch (choice) {
            case 1:
                login();
                return true;
            case 2:
                register();
                return true;
            case 0:
                return false;
            default:
                System.out.println("Неверный выбор. Попробуйте еще раз.");
                return true;
        }
    }
    
    private boolean processAdminMenuChoice(int choice) {
        switch (choice) {
            case 1:
                showProductManagementMenu();
                return true;
            case 2:
                showUserManagementMenu();
                return true;
            case 3:
                showOrderManagementMenu();
                return true;
            case 4:
                showStatisticsMenu();
                return true;
            case 9:
                logout();
                return true;
            case 0:
                return false;
            default:
                System.out.println("Неверный выбор. Попробуйте еще раз.");
                return true;
        }
    }
    
    private boolean processClientMenuChoice(int choice) {
        switch (choice) {
            case 1:
                showCatalog();
                return true;
            case 2:
                searchProducts();
                return true;
            case 3:
                showCartMenu();
                return true;
            case 4:
                showMyOrders();
                return true;
            case 5:
                showUserProfile();
                return true;
            case 9:
                logout();
                return true;
            case 0:
                return false;
            default:
                System.out.println("Неверный выбор. Попробуйте еще раз.");
                return true;
        }
    }
    
    private void login() {
        System.out.println("\n=== ВХОД В СИСТЕМУ ===");
        System.out.print("Введите логин: ");
        String login = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        
        if (userService.authenticate(login, password)) {
            currentUser = userService.findByLogin(login);
            System.out.println("Добро пожаловать, " + currentUser.getFullName() + "!");
        } else {
            System.out.println("Ошибка входа. Проверьте логин и пароль.");
        }
    }
    
    private void register() {
        System.out.println("\n=== РЕГИСТРАЦИЯ ===");
        System.out.print("Введите имя: ");
        String firstName = scanner.nextLine();
        System.out.print("Введите фамилию: ");
        String lastName = scanner.nextLine();
        System.out.print("Введите отчество (если нет, оставьте пустым): ");
        String middleName = scanner.nextLine();
        if (middleName.isEmpty()) {
            middleName = null;
        }
        
        System.out.print("Введите логин: ");
        String login = scanner.nextLine();
        
        if (userService.findByLogin(login) != null) {
            System.out.println("Пользователь с таким логином уже существует.");
            return;
        }
        
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        
        if (userService.findByEmail(email) != null) {
            System.out.println("Пользователь с таким email уже существует.");
            return;
        }
        
        System.out.print("Введите телефон: ");
        String phone = scanner.nextLine();
        
        User user = new User(2, firstName, lastName, middleName, login, password, email, phone);
        user = userService.registerUser(user);
        
        if (user != null) {
            System.out.println("Регистрация успешна! Теперь вы можете войти в систему.");
        } else {
            System.out.println("Ошибка при регистрации пользователя.");
        }
    }
    
    private void logout() {
        currentUser = null;
        System.out.println("Вы вышли из аккаунта.");
    }
    
    private void showCatalog() {
        System.out.println("\n=== КАТАЛОГ ТОВАРОВ ===");
        List<Product> products = productService.findInStock();
        
        if (products.isEmpty()) {
            System.out.println("В каталоге нет товаров.");
            return;
        }
        
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". " + product.toString());
        }
    }
    
    private void searchProducts() {
        System.out.println("\n=== ПОИСК ТОВАРОВ ===");
        System.out.print("Введите название товара для поиска: ");
        String name = scanner.nextLine();
        
        List<Product> products = productService.findByName(name);
        
        if (products.isEmpty()) {
            System.out.println("Товары не найдены.");
            return;
        }
        
        System.out.println("Найдены следующие товары:");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". " + product.toString());
        }
    }
    
    private void showCartMenu() {
        System.out.println("В разработке...");
    }
    
    private void showMyOrders() {
        System.out.println("\n=== МОИ ЗАКАЗЫ ===");
        List<Order> orders = orderService.findByUserId(currentUser.getUserId());
        
        if (orders.isEmpty()) {
            System.out.println("У вас нет заказов.");
            return;
        }
        
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.println((i + 1) + ". " + order.toString());
        }
        
        System.out.print("Введите номер заказа для просмотра деталей (0 для выхода): ");
        int choice = readInt();
        
        if (choice > 0 && choice <= orders.size()) {
            showOrderDetails(orders.get(choice - 1).getOrderId());
        }
    }
    
    private void showOrderDetails(int orderId) {
        System.out.println("\n=== ДЕТАЛИ ЗАКАЗА ===");
        Order order = orderService.findById(orderId);
        
        if (order == null) {
            System.out.println("Заказ не найден.");
            return;
        }
        
        System.out.println("Заказ #" + order.getOrderId());
        System.out.println("Дата заказа: " + order.getOrderDate());
        System.out.println("Дата доставки: " + (order.getDeliveryDate() != null ? order.getDeliveryDate() : "Не указана"));
        
        List<OrderItem> items = order.getOrderItems();
        
        if (items.isEmpty()) {
            System.out.println("В заказе нет товаров.");
            return;
        }
        
        System.out.println("\nТовары в заказе:");
        for (OrderItem item : items) {
            System.out.println(item.toString());
        }
        
        System.out.println("\nИтоговая сумма: " + order.getTotalPrice() + " руб.");
    }
    
    private void showUserProfile() {
        System.out.println("\n=== ЛИЧНЫЙ КАБИНЕТ ===");
        System.out.println("ФИО: " + currentUser.getFullName());
        System.out.println("Логин: " + currentUser.getLogin());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Телефон: " + currentUser.getPhone());
        System.out.println("Статус: " + (currentUser.isRegularCustomer() ? "Постоянный клиент" : "Обычный клиент"));
        System.out.println("Скидка: " + currentUser.getDiscount() + "%");
        System.out.println("Общая сумма покупок: " + currentUser.getTotalPurchases() + " руб.");
        
        System.out.println("\n1. Изменить данные");
        System.out.println("0. Назад");
        System.out.print("Выберите действие: ");
        int choice = readInt();
        
        if (choice == 1) {
            editUserProfile();
        }
    }
    
    private void editUserProfile() {
        System.out.println("\n=== ИЗМЕНЕНИЕ ДАННЫХ ===");
        System.out.println("1. Изменить имя");
        System.out.println("2. Изменить фамилию");
        System.out.println("3. Изменить отчество");
        System.out.println("4. Изменить пароль");
        System.out.println("5. Изменить email");
        System.out.println("6. Изменить телефон");
        System.out.println("0. Назад");
        
        System.out.print("Выберите пункт для изменения: ");
        int choice = readInt();
        
        switch (choice) {
            case 1:
                System.out.print("Введите новое имя: ");
                String firstName = scanner.nextLine();
                currentUser.setFirstName(firstName);
                break;
            case 2:
                System.out.print("Введите новую фамилию: ");
                String lastName = scanner.nextLine();
                currentUser.setLastName(lastName);
                break;
            case 3:
                System.out.print("Введите новое отчество (или оставьте пустым): ");
                String middleName = scanner.nextLine();
                currentUser.setMiddleName(middleName.isEmpty() ? null : middleName);
                break;
            case 4:
                System.out.print("Введите новый пароль: ");
                String password = scanner.nextLine();
                currentUser.setPassword(password);
                break;
            case 5:
                System.out.print("Введите новый email: ");
                String email = scanner.nextLine();
                currentUser.setEmail(email);
                break;
            case 6:
                System.out.print("Введите новый телефон: ");
                String phone = scanner.nextLine();
                currentUser.setPhone(phone);
                break;
            case 0:
                return;
            default:
                System.out.println("Неверный выбор. Попробуйте еще раз.");
                return;
        }
        
        User updatedUser = userService.updateUser(currentUser);
        
        if (updatedUser != null) {
            currentUser = updatedUser;
            System.out.println("Данные успешно обновлены.");
        } else {
            System.out.println("Ошибка при обновлении данных.");
        }
    }
    
    private void showProductManagementMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n=== УПРАВЛЕНИЕ ТОВАРАМИ ===");
            System.out.println("1. Список товаров");
            System.out.println("2. Добавить товар");
            System.out.println("3. Редактировать товар");
            System.out.println("4. Удалить товар");
            System.out.println("5. Поиск товара");
            System.out.println("0. Назад");
            
            System.out.print("Выберите действие: ");
            int choice = readInt();
            
            switch (choice) {
                case 1:
                    showAllProducts();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    editProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    searchProducts();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте еще раз.");
                    break;
            }
        }
    }
    
    private void showAllProducts() {
        System.out.println("\n=== СПИСОК ТОВАРОВ ===");
        List<Product> products = productService.findAll();
        
        if (products.isEmpty()) {
            System.out.println("Список товаров пуст.");
            return;
        }
        
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". " + product.toString());
        }
    }
    
    private void addProduct() {
        System.out.println("\n=== ДОБАВЛЕНИЕ ТОВАРА ===");
        System.out.print("Введите название товара: ");
        String name = scanner.nextLine();
        System.out.print("Введите цену товара: ");
        double price = readDouble();
        System.out.print("Введите единицу измерения (шт, кг, л и т.д.): ");
        String unit = scanner.nextLine();
        System.out.print("Введите количество товара на складе: ");
        int stockQuantity = readInt();
        
        Product product = new Product(name, price, unit, stockQuantity);
        product = productService.addProduct(product);
        
        if (product != null) {
            System.out.println("Товар успешно добавлен.");
        } else {
            System.out.println("Ошибка при добавлении товара.");
        }
    }
    
    private void editProduct() {
        System.out.println("\n=== РЕДАКТИРОВАНИЕ ТОВАРА ===");
        System.out.print("Введите ID товара для редактирования: ");
        int productId = readInt();
        
        Product product = productService.findById(productId);
        
        if (product == null) {
            System.out.println("Товар с ID " + productId + " не найден.");
            return;
        }
        
        System.out.println("Текущие данные товара: " + product.toString());
        
        System.out.print("Введите новое название товара (или Enter для сохранения текущего): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            product.setName(name);
        }
        
        System.out.print("Введите новую цену товара (или 0 для сохранения текущей): ");
        double price = readDouble();
        if (price > 0) {
            product.setPrice(price);
        }
        
        System.out.print("Введите новую единицу измерения (или Enter для сохранения текущей): ");
        String unit = scanner.nextLine();
        if (!unit.isEmpty()) {
            product.setUnit(unit);
        }
        
        System.out.print("Введите новое количество на складе (или -1 для сохранения текущего): ");
        int stockQuantity = readInt();
        if (stockQuantity >= 0) {
            product.setStockQuantity(stockQuantity);
        }
        
        product = productService.updateProduct(product);
        
        if (product != null) {
            System.out.println("Товар успешно обновлен.");
        } else {
            System.out.println("Ошибка при обновлении товара.");
        }
    }
    
    private void deleteProduct() {
        System.out.println("\n=== УДАЛЕНИЕ ТОВАРА ===");
        System.out.print("Введите ID товара для удаления: ");
        int productId = readInt();
        
        if (productService.deleteProduct(productId)) {
            System.out.println("Товар успешно удален.");
        } else {
            System.out.println("Ошибка при удалении товара.");
        }
    }
    
    private void showUserManagementMenu() {
        System.out.println("В разработке...");
    }
    
    private void showOrderManagementMenu() {
        System.out.println("В разработке...");
    }
    
    private void showStatisticsMenu() {
        System.out.println("В разработке...");
    }
    
    private int readInt() {
        int value;
        while (true) {
            try {
                String input = scanner.nextLine();
                value = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.print("Пожалуйста, введите целое число: ");
            }
        }
        return value;
    }
    
    private double readDouble() {
        double value;
        while (true) {
            try {
                String input = scanner.nextLine();
                value = Double.parseDouble(input);
                break;
            } catch (NumberFormatException e) {
                System.out.print("Пожалуйста, введите число: ");
            }
        }
        return value;
    }
}