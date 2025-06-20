package com.example.onlineshop.service.impl;

import com.example.onlineshop.dao.UserDAO;
import com.example.onlineshop.dao.impl.UserDAOImpl;
import com.example.onlineshop.model.User;
import com.example.onlineshop.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    
    private final UserDAO userDAO;
    
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public User findById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User findByLogin(String login) {
        return userDAO.findByLogin(login);
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public List<User> findByRole(int roleId) {
        return userDAO.findByRole(roleId);
    }

    @Override
    public User registerUser(User user) {
        if (findByLogin(user.getLogin()) != null) {
            System.err.println("Пользователь с таким логином уже существует");
            return null;
        }
        
        if (findByEmail(user.getEmail()) != null) {
            System.err.println("Пользователь с таким email уже существует");
            return null;
        }
        
        return userDAO.save(user);
    }

    @Override
    public User updateUser(User user) {
        User existingUser = userDAO.findById(user.getId());
        
        if (existingUser == null) {
            System.err.println("Пользователь с ID " + user.getId() + " не найден");
            return null;
        }
        
        User loginCheck = userDAO.findByLogin(user.getLogin());
        if (loginCheck != null && loginCheck.getId() != user.getId()) {
            System.err.println("Пользователь с таким логином уже существует");
            return null;
        }
        
        User emailCheck = userDAO.findByEmail(user.getEmail());
        if (emailCheck != null && emailCheck.getId() != user.getId()) {
            System.err.println("Пользователь с таким email уже существует");
            return null;
        }
        
        return userDAO.update(user);
    }

    @Override
    public boolean deleteUser(int id) {
        User user = userDAO.findById(id);
        
        if (user == null) {
            System.err.println("Пользователь с ID " + id + " не найден");
            return false;
        }
        
        return userDAO.delete(id);
    }

    @Override
    public boolean authenticate(String login, String password) {
        User user = userDAO.findByLogin(login);
        
        if (user == null) {
            System.err.println("Пользователь с логином " + login + " не найден");
            return false;
        }
        
        return user.getPasswordHash().equals(password);
    }

    @Override
    public boolean updateTotalPurchases(int userId, double purchaseAmount) {
        User user = userDAO.findById(userId);
        
        if (user == null) {
            System.err.println("Пользователь с ID " + userId + " не найден");
            return false;
        }
        
        boolean updated = userDAO.updateTotalPurchases(userId, purchaseAmount);
        
        if (updated) {
            updateCustomerStatus(userId);
        }
        
        return updated;
    }

    @Override
    public boolean updateCustomerStatus(int userId) {
        User user = userDAO.findById(userId);
        
        if (user == null) {
            System.err.println("Пользователь с ID " + userId + " не найден");
            return false;
        }
        
        return userDAO.updateCustomerStatus(userId);
    }
}