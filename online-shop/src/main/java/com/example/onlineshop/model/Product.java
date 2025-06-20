package com.example.onlineshop.model;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String name;
    private BigDecimal price;
    private String unit;
    private int stockQuantity;
    private String description;
    
    public Product() {
    }
    
    public Product(int id, String name, BigDecimal price, String unit, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.stockQuantity = stockQuantity;
    }
    
    public Product(int id, String name, BigDecimal price, String unit, int stockQuantity, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }
    
    // Конструктор для совместимости с MainController
    public Product(String name, double price, String unit, int stockQuantity) {
        this.id = 0; // ID будет назначен базой данных
        this.name = name;
        this.price = new BigDecimal(String.valueOf(price));
        this.unit = unit;
        this.stockQuantity = stockQuantity;
        this.description = "";
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    // Метод для получения цены как BigDecimal
    public BigDecimal getPriceBigDecimal() {
        return price;
    }
    
    // Метод для получения цены как double (для совместимости)
    public double getPrice() {
        return price != null ? price.doubleValue() : 0.0;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    // Метод для установки цены из double (для совместимости)
    public void setPrice(double price) {
        this.price = new BigDecimal(String.valueOf(price));
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public String getDescription() {
        return description != null ? description : "";
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    // Метод для совместимости с текущим кодом
    public int getStock() {
        return stockQuantity;
    }
    
    // Метод для совместимости с текущим кодом
    public void setStock(int stock) {
        this.stockQuantity = stock;
    }
    
    @Override
    public String toString() {
        return name + " (" + price + " руб. за " + unit + ")";
    }
} 