package com.example.onlineshop.dao.impl;

import com.example.onlineshop.dao.ProductDAO;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product findById(Integer id) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToProduct(resultSet);
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске товара по ID: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                products.add(mapResultSetToProduct(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при получении всех товаров: " + e.getMessage());
        }
        
        return products;
    }

    @Override
    public Product save(Product product) {
        String sql = "INSERT INTO products (name, price, unit, stock_quantity) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setString(3, product.getUnit());
            statement.setInt(4, product.getStockQuantity());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении товара: " + e.getMessage());
            return null;
        }
        
        return product;
    }

    @Override
    public Product update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, unit = ?, stock_quantity = ? WHERE product_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setString(3, product.getUnit());
            statement.setInt(4, product.getStockQuantity());
            statement.setInt(5, product.getId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating product failed, no rows affected.");
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении товара: " + e.getMessage());
            return null;
        }
        
        return product;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            return statement.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении товара: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Product> findByName(String name) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                products.add(mapResultSetToProduct(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске товаров по названию: " + e.getMessage());
        }
        
        return products;
    }

    @Override
    public List<Product> findInStock() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE stock_quantity > 0";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                products.add(mapResultSetToProduct(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при получении товаров в наличии: " + e.getMessage());
        }
        
        return products;
    }

    @Override
    public boolean updateStock(int productId, int quantity) {
        String sql = "UPDATE products SET stock_quantity = stock_quantity + ? WHERE product_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            
            return statement.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении количества товара: " + e.getMessage());
            return false;
        }
    }
    
    private Product mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product(
            resultSet.getInt("product_id"),
            resultSet.getString("name"),
            new java.math.BigDecimal(resultSet.getString("price")),
            resultSet.getString("unit"),
            resultSet.getInt("stock_quantity")
        );
        
        return product;
    }
}