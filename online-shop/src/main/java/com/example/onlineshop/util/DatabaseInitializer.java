package com.example.onlineshop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseInitializer {
    
    private static final String DB_NAME = "online_store_db";
    
    public static void initializeDatabase() {
        try {
            // Получаем содержимое SQL-скрипта из файла
            String sqlScript = readSqlScriptFromFile();
            
            // Разделяем скрипт на отдельные SQL-команды
            List<String> sqlCommands = splitSqlScript(sqlScript);
            
            // Выполняем команды
            executeSqlCommands(sqlCommands);
            
            System.out.println("База данных успешно инициализирована!");
            
        } catch (IOException | SQLException e) {
            System.err.println("Ошибка при инициализации базы данных: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String readSqlScriptFromFile() throws IOException {
        StringBuilder scriptContent = new StringBuilder();
        
        try (InputStream inputStream = DatabaseInitializer.class.getClassLoader().getResourceAsStream("database_schema.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            
            if (inputStream == null) {
                throw new IOException("Не удалось найти файл database_schema.sql");
            }
            
            String line;
            while ((line = reader.readLine()) != null) {
                scriptContent.append(line).append("\n");
            }
        }
        
        return scriptContent.toString();
    }
    
    private static List<String> splitSqlScript(String script) {
        List<String> commands = new ArrayList<>();
        StringBuilder currentCommand = new StringBuilder();
        
        for (String line : script.split("\n")) {
            // Пропускаем комментарии и пустые строки
            line = line.trim();
            if (line.isEmpty() || line.startsWith("--")) {
                continue;
            }
            
            currentCommand.append(line).append(" ");
            
            // Если строка заканчивается на точку с запятой, это конец команды
            if (line.endsWith(";")) {
                commands.add(currentCommand.toString());
                currentCommand = new StringBuilder();
            }
        }
        
        return commands;
    }
    
    private static void executeSqlCommands(List<String> commands) throws SQLException, IOException {
        // Загружаем настройки подключения
        Properties props = new Properties();
        try (InputStream input = DatabaseInitializer.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IOException("Не удалось найти файл database.properties");
            }
            props.load(input);
        }
        
        // Получаем параметры подключения
        String baseUrl = props.getProperty("db.url").replaceAll("/online_store_db.*", "/");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        
        System.out.println("Подключение к MySQL без указания базы данных...");
        
        // Сначала подключаемся без указания базы данных
        try (Connection connection = DriverManager.getConnection(baseUrl, user, password);
             Statement statement = connection.createStatement()) {
            
            // Создаем базу данных, если она не существует
            System.out.println("Создание базы данных " + DB_NAME + "...");
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            
            // Выбираем созданную базу данных
            System.out.println("Выбор базы данных " + DB_NAME + "...");
            statement.executeUpdate("USE " + DB_NAME);
            
            // Выполняем все команды из скрипта
            System.out.println("Выполнение SQL-команд...");
            for (String command : commands) {
                if (!command.trim().isEmpty()) {
                    try {
                        statement.executeUpdate(command);
                    } catch (SQLException e) {
                        System.err.println("Ошибка при выполнении команды: " + command);
                        System.err.println("Ошибка: " + e.getMessage());
                        // Продолжаем выполнение, игнорируя ошибки
                    }
                }
            }
        }
        
        System.out.println("Все SQL-команды выполнены.");
    }
} 