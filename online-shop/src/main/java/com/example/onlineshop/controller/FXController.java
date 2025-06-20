package com.example.onlineshop.controller;

import com.example.onlineshop.JavaFXApp;
import com.example.onlineshop.model.User;
import com.example.onlineshop.service.OrderService;
import com.example.onlineshop.service.ProductService;
import com.example.onlineshop.service.UserService;
import com.example.onlineshop.service.impl.OrderServiceImpl;
import com.example.onlineshop.service.impl.ProductServiceImpl;
import com.example.onlineshop.service.impl.UserServiceImpl;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public abstract class FXController {
    
    protected final UserService userService;
    protected final ProductService productService;
    protected final OrderService orderService;
    
    public FXController() {
        this.userService = new UserServiceImpl();
        this.productService = new ProductServiceImpl();
        this.orderService = new OrderServiceImpl();
    }
    
    protected User getCurrentUser() {
        return JavaFXApp.getCurrentUser();
    }
    
    protected void setCurrentUser(User user) {
        JavaFXApp.setCurrentUser(user);
    }
    
    protected void loadScene(String fxmlName, String title) {
        JavaFXApp.loadScene(fxmlName, title);
    }
    
    protected void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    protected void showError(String header, String content) {
        showAlert(AlertType.ERROR, "Ошибка", header, content);
    }
    
    protected void showInfo(String header, String content) {
        showAlert(AlertType.INFORMATION, "Информация", header, content);
    }
    
    protected void showWarning(String header, String content) {
        showAlert(AlertType.WARNING, "Предупреждение", header, content);
    }
} 