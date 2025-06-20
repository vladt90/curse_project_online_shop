package com.example.onlineshop.controller;

import com.example.onlineshop.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AdminDashboardController extends FXController {

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Button productsButton;
    
    @FXML
    private Button usersButton;
    
    @FXML
    private Button ordersButton;
    
    @FXML
    private Button statisticsButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private void initialize() {
        User user = getCurrentUser();
        
        if (user == null || user.getRoleId() != 1) {
            loadScene("login", "Авторизация");
            return;
        }
        
        welcomeLabel.setText("Панель администратора - " + user.getFullName());
    }
    
    @FXML
    private void handleManageProducts(ActionEvent event) {
        loadScene("admin_products", "Управление товарами");
    }
    
    @FXML
    private void handleManageUsers(ActionEvent event) {
        loadScene("admin_users", "Управление пользователями");
    }
    
    @FXML
    private void handleManageOrders(ActionEvent event) {
        loadScene("admin_orders", "Управление заказами");
    }
    
    @FXML
    private void handleViewStatistics(ActionEvent event) {
        loadScene("admin_statistics", "Статистика");
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        setCurrentUser(null);
        loadScene("login", "Авторизация");
    }
} 