package com.example.onlineshop.service.impl;

import com.example.onlineshop.dao.OrderDAO;
import com.example.onlineshop.dao.ProductDAO;
import com.example.onlineshop.dao.impl.OrderDAOImpl;
import com.example.onlineshop.dao.impl.ProductDAOImpl;
import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.OrderItem;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;

    public OrderServiceImpl() {
        this.orderDAO = new OrderDAOImpl();
        this.productDAO = new ProductDAOImpl();
    }

    @Override
    public Order createOrder(Order order) {
        return orderDAO.createOrder(order);
    }

    @Override
    public boolean updateOrder(Order order) {
        return orderDAO.updateOrder(order);
    }

    @Override
    public boolean deleteOrder(int orderId) {
        return orderDAO.deleteOrder(orderId);
    }

    @Override
    public Order getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        return orderDAO.getOrdersByUserId(userId);
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateOrderStatus(orderId, status);
    }

    @Override
    public List<OrderItem> getOrderItems(int orderId) {
        return orderDAO.getOrderItems(orderId);
    }

    @Override
    public boolean addItemToOrder(int orderId, int productId, int quantity, double price) {
        // Проверяем доступность товара
        Product product = productDAO.findById(productId);
        if (product == null || product.getStock() < quantity) {
            return false;
        }

        // Обновляем количество товара на складе
        product.setStock(product.getStock() - quantity);
        productDAO.update(product);

        // Добавляем товар в заказ
        return orderDAO.addItemToOrder(orderId, productId, quantity, price);
    }

    @Override
    public boolean removeItemFromOrder(int orderItemId) {
        return orderDAO.removeItemFromOrder(orderItemId);
    }

    @Override
    public boolean createOrderWithItem(Order order, int productId, int quantity, double price) {
        if (order == null || quantity <= 0) {
            return false;
        }

        // Проверяем доступность товара
        Product product = productDAO.findById(productId);
        if (product == null || product.getStock() < quantity) {
            return false;
        }

        // Создаем заказ
        Order createdOrder = orderDAO.createOrder(order);
        if (createdOrder == null || createdOrder.getId() <= 0) {
            return false;
        }

        // Обновляем количество товара на складе
        product.setStock(product.getStock() - quantity);
        productDAO.update(product);

        // Добавляем товар в заказ
        return orderDAO.addItemToOrder(createdOrder.getId(), productId, quantity, price);
    }
}