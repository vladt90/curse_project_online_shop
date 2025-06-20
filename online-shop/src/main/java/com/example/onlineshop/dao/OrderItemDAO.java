package com.example.onlineshop.dao;

import com.example.onlineshop.model.OrderItem;
import java.util.List;

public interface OrderItemDAO extends DAO<OrderItem, Integer> {
    List<OrderItem> findByOrderId(int orderId);
    List<OrderItem> findByProductId(int productId);
}