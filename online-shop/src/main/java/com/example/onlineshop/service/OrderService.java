package com.example.onlineshop.service;

import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.OrderItem;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    boolean updateOrder(Order order);
    boolean deleteOrder(int orderId);
    Order getOrderById(int orderId);
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(int userId);
    // Метод-алиас для совместимости с MainController
    default List<Order> findByUserId(int userId) {
        return getOrdersByUserId(userId);
    }
    boolean updateOrderStatus(int orderId, String status);
    List<OrderItem> getOrderItems(int orderId);
    boolean addItemToOrder(int orderId, int productId, int quantity, double price);
    boolean removeItemFromOrder(int orderItemId);
    boolean createOrderWithItem(Order order, int productId, int quantity, double price);
    // Метод-алиас для совместимости с MainController
    default Order findById(int orderId) {
        return getOrderById(orderId);
    }
}