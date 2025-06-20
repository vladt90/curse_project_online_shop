package com.example.onlineshop.dao.impl;

import com.example.onlineshop.dao.OrderDAO;
import com.example.onlineshop.model.Order;
import com.example.onlineshop.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public Order findById(Integer id) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToOrder(resultSet);
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске заказа по ID: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                orders.add(mapResultSetToOrder(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при получении всех заказов: " + e.getMessage());
        }
        
        return orders;
    }

    @Override
    public Order save(Order order) {
        String sql = "INSERT INTO orders (user_id, total_price, order_date, delivery_date, discount_applied) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setInt(1, order.getUserId());
            statement.setDouble(2, order.getTotalPrice());
            statement.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            
            if (order.getDeliveryDate() != null) {
                statement.setDate(4, Date.valueOf(order.getDeliveryDate()));
            } else {
                statement.setNull(4, java.sql.Types.DATE);
            }
            
            statement.setDouble(5, order.getDiscountApplied());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении заказа: " + e.getMessage());
            return null;
        }
        
        return order;
    }

    @Override
    public Order update(Order order) {
        String sql = "UPDATE orders SET user_id = ?, total_price = ?, order_date = ?, " +
                     "delivery_date = ?, discount_applied = ? WHERE order_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, order.getUserId());
            statement.setDouble(2, order.getTotalPrice());
            statement.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            
            if (order.getDeliveryDate() != null) {
                statement.setDate(4, Date.valueOf(order.getDeliveryDate()));
            } else {
                statement.setNull(4, java.sql.Types.DATE);
            }
            
            statement.setDouble(5, order.getDiscountApplied());
            statement.setInt(6, order.getOrderId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating order failed, no rows affected.");
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении заказа: " + e.getMessage());
            return null;
        }
        
        return order;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            return statement.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении заказа: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Order> findByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                orders.add(mapResultSetToOrder(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске заказов пользователя: " + e.getMessage());
        }
        
        return orders;
    }

    @Override
    public List<Order> findByDate(LocalDate date) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE DATE(order_date) = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setDate(1, Date.valueOf(date));
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                orders.add(mapResultSetToOrder(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске заказов по дате: " + e.getMessage());
        }
        
        return orders;
    }

    @Override
    public List<Order> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE DATE(order_date) BETWEEN ? AND ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setDate(1, Date.valueOf(startDate));
            statement.setDate(2, Date.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                orders.add(mapResultSetToOrder(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске заказов по диапазону дат: " + e.getMessage());
        }
        
        return orders;
    }
    
    private Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order(
            resultSet.getInt("order_id"),
            resultSet.getInt("user_id"),
            resultSet.getDouble("total_price"),
            resultSet.getTimestamp("order_date").toLocalDateTime(),
            resultSet.getDate("delivery_date") != null ? resultSet.getDate("delivery_date").toLocalDate() : null,
            resultSet.getDouble("discount_applied")
        );
        
        return order;
    }
}