package com.example.onlineshop.dao.impl;

import com.example.onlineshop.dao.OrderItemDAO;
import com.example.onlineshop.model.OrderItem;
import com.example.onlineshop.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements OrderItemDAO {

    @Override
    public OrderItem findById(Integer id) {
        String sql = "SELECT * FROM order_items WHERE order_item_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToOrderItem(resultSet);
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске элемента заказа по ID: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public List<OrderItem> findAll() {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM order_items";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                orderItems.add(mapResultSetToOrderItem(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при получении всех элементов заказов: " + e.getMessage());
        }
        
        return orderItems;
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, item_price) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setDouble(4, orderItem.getItemPrice());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating order item failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderItem.setOrderItemId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating order item failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении элемента заказа: " + e.getMessage());
            return null;
        }
        
        return orderItem;
    }

    @Override
    public OrderItem update(OrderItem orderItem) {
        String sql = "UPDATE order_items SET order_id = ?, product_id = ?, quantity = ?, item_price = ? " +
                     "WHERE order_item_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getProductId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setDouble(4, orderItem.getItemPrice());
            statement.setInt(5, orderItem.getOrderItemId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating order item failed, no rows affected.");
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении элемента заказа: " + e.getMessage());
            return null;
        }
        
        return orderItem;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM order_items WHERE order_item_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            return statement.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении элемента заказа: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<OrderItem> findByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                orderItems.add(mapResultSetToOrderItem(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске элементов заказа по ID заказа: " + e.getMessage());
        }
        
        return orderItems;
    }

    @Override
    public List<OrderItem> findByProductId(int productId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE product_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                orderItems.add(mapResultSetToOrderItem(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске элементов заказа по ID товара: " + e.getMessage());
        }
        
        return orderItems;
    }
    
    private OrderItem mapResultSetToOrderItem(ResultSet resultSet) throws SQLException {
        OrderItem orderItem = new OrderItem(
            resultSet.getInt("order_item_id"),
            resultSet.getInt("order_id"),
            resultSet.getInt("product_id"),
            resultSet.getInt("quantity"),
            resultSet.getDouble("item_price")
        );
        
        return orderItem;
    }
}