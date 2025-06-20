package com.example.onlineshop.dao;

import com.example.onlineshop.model.Order;
import java.time.LocalDate;
import java.util.List;

public interface OrderDAO extends DAO<Order, Integer> {
    List<Order> findByUserId(int userId);
    List<Order> findByDate(LocalDate date);
    List<Order> findByDateRange(LocalDate startDate, LocalDate endDate);
}