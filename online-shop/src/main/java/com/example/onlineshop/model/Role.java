package com.example.onlineshop.model;

public class Role {
    // Константы для ID ролей
    public static final int ADMIN_ROLE_ID = 1;
    public static final int CLIENT_ROLE_ID = 2;
    
    private int id;
    private String name;
    
    public Role() {
    }
    
    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Геттеры и сеттеры
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
} 