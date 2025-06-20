package com.example.onlineshop.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private LocalDateTime orderDate;
    private LocalDate deliveryDate;
    private BigDecimal totalCost;
    private String status;
    private List<OrderItem> items;
    
    public Order() {
        this.items = new ArrayList<>();
        this.orderDate = LocalDateTime.now();
        this.totalCost = BigDecimal.ZERO;
        this.status = "новый";
    }
    
    public Order(int id, int userId, LocalDateTime orderDate, LocalDate deliveryDate, BigDecimal totalCost) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.totalCost = totalCost;
        this.items = new ArrayList<>();
        this.status = "новый";
    }
    
    public Order(int id, int userId, LocalDateTime orderDate, LocalDate deliveryDate, BigDecimal totalCost, String status) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.totalCost = totalCost;
        this.status = status;
        this.items = new ArrayList<>();
    }
    
    // Геттеры и сеттеры
    public int getId() {
        return id;
    }
    
    // Метод для совместимости с существующим кодом
    public int getOrderId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }
    
    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getTotalCost() {
        return totalCost;
    }
    
    // Метод для совместимости с существующим кодом
    public double getTotalPrice() {
        return totalCost != null ? totalCost.doubleValue() : 0.0;
    }
    
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    // Метод для совместимости с существующим кодом
    public List<OrderItem> getOrderItems() {
        return items;
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    
    public void addItem(OrderItem item) {
        this.items.add(item);
        // Пересчитываем общую стоимость
        recalculateTotalCost();
    }
    
    public void removeItem(OrderItem item) {
        this.items.remove(item);
        // Пересчитываем общую стоимость
        recalculateTotalCost();
    }
    
    public void recalculateTotalCost() {
        this.totalCost = BigDecimal.ZERO;
        for (OrderItem item : items) {
            this.totalCost = this.totalCost.add(item.getTotalPrice());
        }
    }
} 