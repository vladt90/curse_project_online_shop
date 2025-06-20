package com.example.onlineshop.service;

import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.OrderItem;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    Order findById(int id);
    List<Order> findAll();
    List<Order> findByUserId(int userId);
    List<Order> findByDate(LocalDate date);
    List<Order> findByDateRange(LocalDate startDate, LocalDate endDate);
    Order createOrder(int userId);
    boolean addItemToOrder(int orderId, int productId, int quantity);
    boolean removeItemFromOrder(int orderId, int orderItemId);
    boolean updateOrderItemQuantity(int orderId, int orderItemId, int newQuantity);
    boolean applyDiscount(int orderId, double discountPercent);
    boolean setDeliveryDate(int orderId, LocalDate deliveryDate);
    boolean finalizeOrder(int orderId);
    List<OrderItem> getOrderItems(int orderId);
    boolean deleteOrder(int id);
}