package com.example.onlineshop;

import com.example.onlineshop.controller.MainController;
import com.example.onlineshop.util.DatabaseInitializer;
import com.example.onlineshop.util.DatabaseManager;
import java.sql.Connection;
import java.sql.SQLException;

public class App 
{
    public static void main(String[] args)
    {
        System.out.println("Запуск приложения Интернет-магазин");
        
        try {
            System.out.println("Инициализация базы данных...");
            DatabaseInitializer.initializeDatabase();
            
            System.out.println("Проверка соединения с базой данных...");
            DatabaseManager.getInstance().reloadConfig();
            
            Connection connection = DatabaseManager.getInstance().getConnection();
            
            if (connection != null && !connection.isClosed()) {
                System.out.println("Соединение с базой данных успешно установлено!");
                
                // Проверяем аргументы запуска для выбора интерфейса
                boolean useConsole = false;
                
                for (String arg : args) {
                    if (arg.equals("--console") || arg.equals("-c")) {
                        useConsole = true;
                        break;
                    }
                }
                
                if (useConsole) {
                    // Запускаем консольное приложение
                    MainController controller = new MainController();
                    controller.start();
                } else {
                    // Запускаем JavaFX приложение
                    JavaFXApp.main(args);
                }
                
                DatabaseManager.getInstance().closeConnection();
                
            } else {
                System.err.println("Не удалось установить соединение с базой данных.");
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при работе с базой данных: " + e.getMessage());
            e.printStackTrace();
        }
    }
}