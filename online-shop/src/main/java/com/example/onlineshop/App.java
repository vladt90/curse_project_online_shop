package com.example.onlineshop;

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
                System.out.println("URL: " + connection.getMetaData().getURL());
                System.out.println("Пользователь: " + connection.getMetaData().getUserName());
                System.out.println("База данных: " + connection.getCatalog());
            } else {
                System.err.println("Не удалось установить соединение с базой данных.");
            }
            

            dbManager.closeConnection();
            
        } catch (SQLException e) {
            System.err.println("Ошибка при работе с базой данных: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
