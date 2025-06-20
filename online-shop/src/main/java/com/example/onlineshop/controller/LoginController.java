package com.example.onlineshop.controller;

import com.example.onlineshop.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends FXController {

    @FXML
    private TextField loginField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button registerButton;
    
    @FXML
    private void initialize() {
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (login.isEmpty() || password.isEmpty()) {
            showError("Ошибка входа", "Логин и пароль не могут быть пустыми.");
            return;
        }
        
        if (userService.authenticate(login, password)) {
            User user = userService.findByLogin(login);
            setCurrentUser(user);
            
            if (user.getRoleId() == 1) {
                loadScene("admin_dashboard", "Панель администратора");
            } else {
                loadScene("user_dashboard", "Личный кабинет");
            }
        } else {
            showError("Ошибка входа", "Неверный логин или пароль.");
        }
    }
    
    @FXML
    private void handleRegister(ActionEvent event) {
        loadScene("register", "Регистрация");
    }
} 