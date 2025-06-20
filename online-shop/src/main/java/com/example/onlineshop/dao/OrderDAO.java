package com.example.onlineshop.dao;

import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.OrderItem;
import java.time.LocalDate;
import java.util.List;

public interface OrderDAO {
    
    Order createOrder(Order order);
    
    boolean updateOrder(Order order);
    
    boolean deleteOrder(int orderId);
    
    Order getOrderById(int orderId);
    
    List<Order> getAllOrders();
    
    List<Order> getOrdersByUserId(int userId);
    
    boolean updateOrderStatus(int orderId, String status);
    
    List<OrderItem> getOrderItems(int orderId);
    
    boolean addItemToOrder(int orderId, int productId, int quantity, double price);
    
    boolean removeItemFromOrder(int orderItemId);
}