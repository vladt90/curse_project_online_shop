package com.example.onlineshop.service.impl;

import com.example.onlineshop.dao.OrderDAO;
import com.example.onlineshop.dao.OrderItemDAO;
import com.example.onlineshop.dao.ProductDAO;
import com.example.onlineshop.dao.UserDAO;
import com.example.onlineshop.dao.impl.OrderDAOImpl;
import com.example.onlineshop.dao.impl.OrderItemDAOImpl;
import com.example.onlineshop.dao.impl.ProductDAOImpl;
import com.example.onlineshop.dao.impl.UserDAOImpl;
import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.OrderItem;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.User;
import com.example.onlineshop.service.OrderService;

import java.time.LocalDate;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    
    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    
    public OrderServiceImpl() {
        this.orderDAO = new OrderDAOImpl();
        this.orderItemDAO = new OrderItemDAOImpl();
        this.productDAO = new ProductDAOImpl();
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public Order findById(int id) {
        Order order = orderDAO.findById(id);
        
        if (order != null) {
            List<OrderItem> orderItems = orderItemDAO.findByOrderId(id);
            order.setOrderItems(orderItems);
            
            for (OrderItem item : orderItems) {
                Product product = productDAO.findById(item.getProductId());
                item.setProduct(product);
            }
        }
        
        return order;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = orderDAO.findAll();
        
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemDAO.findByOrderId(order.getOrderId());
            order.setOrderItems(orderItems);
            
            for (OrderItem item : orderItems) {
                Product product = productDAO.findById(item.getProductId());
                item.setProduct(product);
            }
        }
        
        return orders;
    }

    @Override
    public List<Order> findByUserId(int userId) {
        List<Order> orders = orderDAO.findByUserId(userId);
        
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemDAO.findByOrderId(order.getOrderId());
            order.setOrderItems(orderItems);
            
            for (OrderItem item : orderItems) {
                Product product = productDAO.findById(item.getProductId());
                item.setProduct(product);
            }
        }
        
        return orders;
    }

    @Override
    public List<Order> findByDate(LocalDate date) {
        List<Order> orders = orderDAO.findByDate(date);
        
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemDAO.findByOrderId(order.getOrderId());
            order.setOrderItems(orderItems);
            
            for (OrderItem item : orderItems) {
                Product product = productDAO.findById(item.getProductId());
                item.setProduct(product);
            }
        }
        
        return orders;
    }

    @Override
    public List<Order> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderDAO.findByDateRange(startDate, endDate);
        
        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemDAO.findByOrderId(order.getOrderId());
            order.setOrderItems(orderItems);
            
            for (OrderItem item : orderItems) {
                Product product = productDAO.findById(item.getProductId());
                item.setProduct(product);
            }
        }
        
        return orders;
    }

    @Override
    public Order createOrder(int userId) {
        User user = userDAO.findById(userId);
        
        if (user == null) {
            System.err.println("Пользователь с ID " + userId + " не найден");
            return null;
        }
        
        Order order = new Order(userId);
        
        if (user.isRegularCustomer()) {
            order.applyDiscount(user.getDiscount());
        }
        
        return orderDAO.save(order);
    }

    @Override
    public boolean addItemToOrder(int orderId, int productId, int quantity) {
        Order order = orderDAO.findById(orderId);
        
        if (order == null) {
            System.err.println("Заказ с ID " + orderId + " не найден");
            return false;
        }
        
        Product product = productDAO.findById(productId);
        
        if (product == null) {
            System.err.println("Товар с ID " + productId + " не найден");
            return false;
        }
        
        if (quantity <= 0) {
            System.err.println("Количество товара должно быть положительным");
            return false;
        }
        
        if (product.getStockQuantity() < quantity) {
            System.err.println("Недостаточно товара на складе");
            return false;
        }
        
        OrderItem orderItem = new OrderItem(orderId, productId, quantity, product.getPrice());
        orderItemDAO.save(orderItem);
        
        order = findById(orderId);
        order.recalculateTotalPrice();
        orderDAO.update(order);
        
        productDAO.updateStock(productId, -quantity);
        
        return true;
    }

    @Override
    public boolean removeItemFromOrder(int orderId, int orderItemId) {
        Order order = orderDAO.findById(orderId);
        
        if (order == null) {
            System.err.println("Заказ с ID " + orderId + " не найден");
            return false;
        }
        
        OrderItem orderItem = orderItemDAO.findById(orderItemId);
        
        if (orderItem == null || orderItem.getOrderId() != orderId) {
            System.err.println("Элемент заказа не найден или не принадлежит этому заказу");
            return false;
        }
        
        productDAO.updateStock(orderItem.getProductId(), orderItem.getQuantity());
        boolean deleted = orderItemDAO.delete(orderItemId);
        
        if (deleted) {
            order = findById(orderId);
            order.recalculateTotalPrice();
            orderDAO.update(order);
        }
        
        return deleted;
    }

    @Override
    public boolean updateOrderItemQuantity(int orderId, int orderItemId, int newQuantity) {
        if (newQuantity <= 0) {
            System.err.println("Новое количество товара должно быть положительным");
            return false;
        }
        
        Order order = orderDAO.findById(orderId);
        
        if (order == null) {
            System.err.println("Заказ с ID " + orderId + " не найден");
            return false;
        }
        
        OrderItem orderItem = orderItemDAO.findById(orderItemId);
        
        if (orderItem == null || orderItem.getOrderId() != orderId) {
            System.err.println("Элемент заказа не найден или не принадлежит этому заказу");
            return false;
        }
        
        int quantityDifference = newQuantity - orderItem.getQuantity();
        
        if (quantityDifference != 0) {
            Product product = productDAO.findById(orderItem.getProductId());
            
            if (product == null) {
                System.err.println("Товар с ID " + orderItem.getProductId() + " не найден");
                return false;
            }
            
            if (quantityDifference > 0 && product.getStockQuantity() < quantityDifference) {
                System.err.println("Недостаточно товара на складе");
                return false;
            }
            
            orderItem.setQuantity(newQuantity);
            orderItemDAO.update(orderItem);
            
            productDAO.updateStock(orderItem.getProductId(), -quantityDifference);
            
            order = findById(orderId);
            order.recalculateTotalPrice();
            orderDAO.update(order);
        }
        
        return true;
    }

    @Override
    public boolean applyDiscount(int orderId, double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            System.err.println("Скидка должна быть от 0 до 100 процентов");
            return false;
        }
        
        Order order = orderDAO.findById(orderId);
        
        if (order == null) {
            System.err.println("Заказ с ID " + orderId + " не найден");
            return false;
        }
        
        order.applyDiscount(discountPercent);
        
        return orderDAO.update(order) != null;
    }

    @Override
    public boolean setDeliveryDate(int orderId, LocalDate deliveryDate) {
        if (deliveryDate == null) {
            System.err.println("Дата доставки не может быть пустой");
            return false;
        }
        
        Order order = orderDAO.findById(orderId);
        
        if (order == null) {
            System.err.println("Заказ с ID " + orderId + " не найден");
            return false;
        }
        
        order.setDeliveryDate(deliveryDate);
        
        return orderDAO.update(order) != null;
    }

    @Override
    public boolean finalizeOrder(int orderId) {
        Order order = orderDAO.findById(orderId);
        
        if (order == null) {
            System.err.println("Заказ с ID " + orderId + " не найден");
            return false;
        }
        
        if (order.getOrderItems().isEmpty()) {
            System.err.println("Заказ не содержит товаров");
            return false;
        }
        
        if (order.getDeliveryDate() == null) {
            order.setDeliveryDays(3);
        }
        
        User user = userDAO.findById(order.getUserId());
        
        if (user != null) {
            userDAO.updateTotalPurchases(user.getUserId(), order.getTotalPrice());
            userDAO.updateCustomerStatus(user.getUserId());
        }
        
        return orderDAO.update(order) != null;
    }

    @Override
    public List<OrderItem> getOrderItems(int orderId) {
        Order order = orderDAO.findById(orderId);
        
        if (order == null) {
            System.err.println("Заказ с ID " + orderId + " не найден");
            return null;
        }
        
        List<OrderItem> orderItems = orderItemDAO.findByOrderId(orderId);
        
        for (OrderItem item : orderItems) {
            Product product = productDAO.findById(item.getProductId());
            item.setProduct(product);
        }
        
        return orderItems;
    }

    @Override
    public boolean deleteOrder(int id) {
        Order order = orderDAO.findById(id);
        
        if (order == null) {
            System.err.println("Заказ с ID " + id + " не найден");
            return false;
        }
        
        List<OrderItem> orderItems = orderItemDAO.findByOrderId(id);
        
        for (OrderItem item : orderItems) {
            productDAO.updateStock(item.getProductId(), item.getQuantity());
            orderItemDAO.delete(item.getOrderItemId());
        }
        
        return orderDAO.delete(id);
    }
}