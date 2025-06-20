package com.example.onlineshop.service;

import com.example.onlineshop.model.User;
import java.util.List;

public interface UserService {
    User findById(int id);
    List<User> findAll();
    User findByLogin(String login);
    User findByEmail(String email);
    List<User> findByRole(int roleId);
    User registerUser(User user);
    User updateUser(User user);
    boolean deleteUser(int id);
    boolean authenticate(String login, String password);
    boolean updateTotalPurchases(int userId, double purchaseAmount);
    boolean updateCustomerStatus(int userId);
}