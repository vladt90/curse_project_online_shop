package com.example.onlineshop.controller;

import com.example.onlineshop.model.Role;
import com.example.onlineshop.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController extends FXController {

    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField middleNameField;
    
    @FXML
    private TextField loginField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneField;
    
    @FXML
    private Button registerButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private void initialize() {
    }
    
    @FXML
    private void handleRegister(ActionEvent event) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String middleName = middleNameField.getText().trim();
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        
        // Проверка обязательных полей
        if (firstName.isEmpty() || lastName.isEmpty() || login.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            showError("Ошибка регистрации", "Пожалуйста, заполните все обязательные поля.");
            return;
        }
        
        // Проверка совпадения паролей
        if (!password.equals(confirmPassword)) {
            showError("Ошибка регистрации", "Пароли не совпадают.");
            return;
        }
        
        // Проверка существующего логина
        if (userService.findByLogin(login) != null) {
            showError("Ошибка регистрации", "Пользователь с таким логином уже существует.");
            return;
        }
        
        // Проверка существующего email
        if (userService.findByEmail(email) != null) {
            showError("Ошибка регистрации", "Пользователь с таким email уже существует.");
            return;
        }
        
        // Создание и сохранение пользователя
        User user = new User(
            Role.CLIENT_ROLE_ID,
            firstName,
            lastName,
            middleName.isEmpty() ? null : middleName,
            login,
            password,
            email,
            phone
        );
        
        User registeredUser = userService.registerUser(user);
        
        if (registeredUser != null) {
            showInfo("Успешная регистрация", "Регистрация завершена успешно. Теперь вы можете войти в систему.");
            loadScene("login", "Авторизация");
        } else {
            showError("Ошибка регистрации", "Не удалось зарегистрировать пользователя.");
        }
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        loadScene("login", "Авторизация");
    }
} 