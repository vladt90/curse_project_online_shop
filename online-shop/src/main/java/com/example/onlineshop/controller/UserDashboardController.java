package com.example.onlineshop.controller;

import com.example.onlineshop.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UserDashboardController extends FXController {

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label userInfoLabel;
    
    @FXML
    private Label discountLabel;
    
    @FXML
    private Button catalogButton;
    
    @FXML
    private Button ordersButton;
    
    @FXML
    private Button profileButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private void initialize() {
        User user = getCurrentUser();
        
        if (user == null) {
            loadScene("login", "Авторизация");
            return;
        }
        
        welcomeLabel.setText("Добро пожаловать, " + user.getFullName() + "!");
        userInfoLabel.setText("Email: " + user.getEmail() + ", Телефон: " + 
                             (user.getPhone() != null ? user.getPhone() : "не указан"));
        
        if (user.isRegularCustomer()) {
            discountLabel.setText("Ваша персональная скидка: " + user.getDiscount() + "%");
        } else {
            discountLabel.setText("У вас еще нет персональной скидки. " +
                                 "Совершите покупки на сумму более 5000 руб., чтобы получить скидку 2%.");
        }
    }
    
    @FXML
    private void handleViewCatalog(ActionEvent event) {
        loadScene("catalog", "Каталог товаров");
    }
    
    @FXML
    private void handleViewOrders(ActionEvent event) {
        loadScene("user_orders", "Мои заказы");
    }
    
    @FXML
    private void handleEditProfile(ActionEvent event) {
        loadScene("edit_profile", "Редактирование профиля");
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        setCurrentUser(null);
        loadScene("login", "Авторизация");
    }
} 