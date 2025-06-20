package com.example.onlineshop.model;

import java.math.BigDecimal;

public class OrderItem {
    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private BigDecimal pricePerUnit;
    private Product product; // Для отображения информации о товаре
    
    public OrderItem() {
    }
    
    public OrderItem(int id, int orderId, int productId, int quantity, BigDecimal pricePerUnit) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }
    
    public OrderItem(int id, int orderId, Product product, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = product.getId();
        this.product = product;
        this.quantity = quantity;
        this.pricePerUnit = new BigDecimal(String.valueOf(product.getPrice())); // Преобразуем double в BigDecimal
    }
    
    // Геттеры и сеттеры
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }
    
    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
    
    // Для совместимости с UserOrdersController
    public double getPrice() {
        return pricePerUnit != null ? pricePerUnit.doubleValue() : 0.0;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            this.productId = product.getId();
        }
    }
    
    public BigDecimal getTotalPrice() {
        return pricePerUnit.multiply(new BigDecimal(quantity));
    }
    
    // Для совместимости с UserOrdersController
    public double getSubtotal() {
        return pricePerUnit != null ? pricePerUnit.doubleValue() * quantity : 0.0;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
} 