package com.example.onlineshop;

import com.example.onlineshop.controller.MainController;
import com.example.onlineshop.util.DatabaseManager;
import java.sql.Connection;
import java.sql.SQLException;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println("Запуск приложения Интернет-магазин");
        
        try {
            System.out.println("Проверка соединения с базой данных...");
            DatabaseManager dbManager = DatabaseManager.getInstance();
            Connection connection = dbManager.getConnection();
            
            if (connection != null && !connection.isClosed()) {
                System.out.println("Соединение с базой данных успешно установлено!");
                
                MainController controller = new MainController();
                controller.start();
                
                dbManager.closeConnection();
                
            } else {
                System.err.println("Не удалось установить соединение с базой данных.");
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при работе с базой данных: " + e.getMessage());
            e.printStackTrace();
        }
    }
}