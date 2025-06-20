package com.example.onlineshop.model;

import java.math.BigDecimal;

public class User {
    private int id;
    private int roleId;
    private String login;
    private String passwordHash;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String email;
    private String phone;
    private String status;
    private BigDecimal discount;
    private double totalPurchases; // Общая сумма покупок
    
    public User() {
        this.discount = new BigDecimal("0.00");
        this.totalPurchases = 0.0;
        this.status = "обычный";
    }
    
    public User(int id, int roleId, String login, String passwordHash, String lastName, 
                String firstName, String patronymic, String email, String phone, 
                String status, BigDecimal discount) {
        this.id = id;
        this.roleId = roleId;
        this.login = login;
        this.passwordHash = passwordHash;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.discount = discount != null ? discount : new BigDecimal("0.00");
        this.totalPurchases = 0.0;
    }
    
    // Конструктор для совместимости с MainController
    public User(int roleId, String firstName, String lastName, String patronymic, 
                String login, String passwordHash, String email, String phone) {
        this.id = 0; // ID будет назначен базой данных
        this.roleId = roleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phone = phone;
        this.status = "активен"; // Статус по умолчанию
        this.discount = new BigDecimal("0.00"); // Скидка по умолчанию
        this.totalPurchases = 0.0;
    }
    
    // Геттеры и сеттеры
    public int getId() {
        return id;
    }
    
    // Для совместимости с существующим кодом
    public int getUserId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getRoleId() {
        return roleId;
    }
    
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    // Для совместимости с существующим кодом
    public void setPassword(String password) {
        this.passwordHash = password; // В реальном приложении здесь должно быть хеширование
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getPatronymic() {
        return patronymic;
    }
    
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    
    // Альтернативный метод для совместимости с кодом
    public void setMiddleName(String middleName) {
        this.patronymic = middleName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getDiscount() {
        return discount;
    }
    
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (lastName != null) sb.append(lastName).append(" ");
        if (firstName != null) sb.append(firstName).append(" ");
        if (patronymic != null) sb.append(patronymic);
        return sb.toString().trim();
    }
    
    public double getTotalPurchases() {
        return totalPurchases;
    }
    
    public void setTotalPurchases(double totalPurchases) {
        this.totalPurchases = totalPurchases;
    }
    
    public boolean isRegularCustomer() {
        return "постоянный".equalsIgnoreCase(status);
    }
} 