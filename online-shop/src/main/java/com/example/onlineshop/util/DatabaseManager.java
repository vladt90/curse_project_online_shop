package com.example.onlineshop.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class DatabaseManager {
    private static final String CONFIG_FILE = "database.properties";
    
    private static final String DB_URL_KEY = "db.url";
    private static final String DB_USER_KEY = "db.user";
    private static final String DB_PASSWORD_KEY = "db.password";
    private static final String DB_DRIVER_KEY = "db.driver";
    
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private String dbDriver;
    
    private static DatabaseManager instance;
    private Connection connection;
    

    private DatabaseManager() {
        loadConfig();
        
        try {

            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка загрузки драйвера JDBC: " + e.getMessage());
            throw new RuntimeException("Не удалось загрузить драйвер MySQL", e);
        }
    }
    

    private void loadConfig() {
        Properties props = new Properties();
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Не удалось найти файл " + CONFIG_FILE);
                throw new RuntimeException("Файл конфигурации " + CONFIG_FILE + " не найден!");
            }
            
            props.load(input);
            dbUrl = props.getProperty(DB_URL_KEY);
            dbUser = props.getProperty(DB_USER_KEY);
            dbPassword = props.getProperty(DB_PASSWORD_KEY);
            dbDriver = props.getProperty(DB_DRIVER_KEY);
            
            if (dbUrl == null || dbUser == null || dbPassword == null || dbDriver == null) {
                throw new RuntimeException("Не все параметры подключения к БД указаны в файле конфигурации!");
            }
            
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла конфигурации: " + e.getMessage());
            throw new RuntimeException("Не удалось прочитать файл конфигурации", e);
        }
    }
    
    /**
     * Перезагружает настройки подключения из файла конфигурации.
     * Полезно, когда настройки могли измениться во время выполнения программы.
     */
    public void reloadConfig() {
        // Закрываем текущее соединение, если оно открыто
        closeConnection();
        
        // Загружаем настройки заново
        loadConfig();
        
        System.out.println("Настройки подключения к базе данных перезагружены.");
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        }
        return connection;
    }
    

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Соединение с базой данных закрыто");
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }
}