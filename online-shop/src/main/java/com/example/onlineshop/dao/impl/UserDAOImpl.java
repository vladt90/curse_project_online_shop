package com.example.onlineshop.dao.impl;

import com.example.onlineshop.dao.UserDAO;
import com.example.onlineshop.model.User;
import com.example.onlineshop.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    
    @Override
    public User findById(Integer id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске пользователя по ID: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при получении всех пользователей: " + e.getMessage());
        }
        
        return users;
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (role_id, first_name, last_name, middle_name, login, password, " +
                     "email, phone, is_regular_customer, discount, total_purchases) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            statement.setInt(1, user.getRoleId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getMiddleName());
            statement.setString(5, user.getLogin());
            statement.setString(6, user.getPassword());
            statement.setString(7, user.getEmail());
            statement.setString(8, user.getPhone());
            statement.setBoolean(9, user.isRegularCustomer());
            statement.setDouble(10, user.getDiscount());
            statement.setDouble(11, user.getTotalPurchases());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении пользователя: " + e.getMessage());
            return null;
        }
        
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET role_id = ?, first_name = ?, last_name = ?, middle_name = ?, " +
                     "login = ?, password = ?, email = ?, phone = ?, is_regular_customer = ?, " +
                     "discount = ?, total_purchases = ? WHERE user_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, user.getRoleId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getMiddleName());
            statement.setString(5, user.getLogin());
            statement.setString(6, user.getPassword());
            statement.setString(7, user.getEmail());
            statement.setString(8, user.getPhone());
            statement.setBoolean(9, user.isRegularCustomer());
            statement.setDouble(10, user.getDiscount());
            statement.setDouble(11, user.getTotalPurchases());
            statement.setInt(12, user.getUserId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении пользователя: " + e.getMessage());
            return null;
        }
        
        return user;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            return statement.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User findByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске пользователя по логину: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске пользователя по email: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public List<User> findByRole(int roleId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, roleId);
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
            
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске пользователей по роли: " + e.getMessage());
        }
        
        return users;
    }

    @Override
    public boolean updateTotalPurchases(int userId, double purchaseAmount) {
        String sql = "UPDATE users SET total_purchases = total_purchases + ? WHERE user_id = ?";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setDouble(1, purchaseAmount);
            statement.setInt(2, userId);
            
            return statement.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении суммы покупок: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateCustomerStatus(int userId) {
        String sql = "UPDATE users SET is_regular_customer = true, discount = 2.0 " +
                     "WHERE user_id = ? AND total_purchases > 5000 AND is_regular_customer = false";
        
        try (Connection connection = DatabaseManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, userId);
            
            return statement.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении статуса клиента: " + e.getMessage());
            return false;
        }
    }
    
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User(
            resultSet.getInt("user_id"),
            resultSet.getInt("role_id"),
            resultSet.getString("first_name"),
            resultSet.getString("last_name"),
            resultSet.getString("middle_name"),
            resultSet.getString("login"),
            resultSet.getString("password"),
            resultSet.getString("email"),
            resultSet.getString("phone"),
            resultSet.getBoolean("is_regular_customer"),
            resultSet.getDouble("discount"),
            resultSet.getDouble("total_purchases")
        );
        
        return user;
    }
}