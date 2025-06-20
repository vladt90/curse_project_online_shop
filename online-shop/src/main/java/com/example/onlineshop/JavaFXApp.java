package com.example.onlineshop;

import com.example.onlineshop.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXApp extends Application {

    private static Stage primaryStage;
    private static User currentUser;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Интернет-магазин");
        
        loadScene("login", "Авторизация");
    }
    
    public static void loadScene(String fxmlName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(JavaFXApp.class.getResource("/fxml/" + fxmlName + ".fxml"));
            Parent root = loader.load();
            
            primaryStage.setTitle(title);
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(true);
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка при загрузке FXML: " + e.getMessage());
        }
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    public static void main(String[] args) {
        launch();
    }
} 