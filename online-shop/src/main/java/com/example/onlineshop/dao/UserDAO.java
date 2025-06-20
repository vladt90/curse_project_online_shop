package com.example.onlineshop.dao;

import com.example.onlineshop.model.User;
import java.util.List;

public interface UserDAO extends DAO<User, Integer> {
    User findByLogin(String login);
    User findByEmail(String email);
    List<User> findByRole(int roleId);
    boolean updateTotalPurchases(int userId, double purchaseAmount);
    boolean updateCustomerStatus(int userId);
}