package com.example.onlineshop.dao.impl;

import com.example.onlineshop.dao.OrderDAO;
import com.example.onlineshop.dao.ProductDAO;
import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.OrderItem;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.util.DatabaseManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    private final DatabaseManager dbManager;
    private final ProductDAO productDAO;

    public OrderDAOImpl() {
        this.dbManager = DatabaseManager.getInstance();
        this.productDAO = new ProductDAOImpl();
    }

    @Override
    public Order createOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, order_date, status, total_price) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, order.getUserId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            stmt.setString(3, order.getStatus());
            stmt.setDouble(4, order.getTotalPrice());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getInt(1));
                        return order;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET user_id = ?, order_date = ?, status = ?, total_price = ? WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, order.getUserId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            stmt.setString(3, order.getStatus());
            stmt.setDouble(4, order.getTotalPrice());
            stmt.setInt(5, order.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public boolean deleteOrder(int orderId) {
        // Сначала удаляем все элементы заказа
        String deleteItemsSql = "DELETE FROM order_items WHERE order_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement deleteItemsStmt = conn.prepareStatement(deleteItemsSql)) {
            
            deleteItemsStmt.setInt(1, orderId);
            deleteItemsStmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error deleting order items: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        // Затем удаляем сам заказ
        String deleteOrderSql = "DELETE FROM orders WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderSql)) {
            
            deleteOrderStmt.setInt(1, orderId);
            int affectedRows = deleteOrderStmt.executeUpdate();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractOrder(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Order order = extractOrder(rs);
                orders.add(order);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all orders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return orders;
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = extractOrder(rs);
                    orders.add(order);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting user orders: " + e.getMessage());
            e.printStackTrace();
        }
        
        return orders;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name, p.description, p.price as current_price, p.stock_quantity, p.unit " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE oi.order_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int productId = rs.getInt("product_id");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price_per_unit");
                    
                    // Создаем продукт
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    double currentPrice = rs.getDouble("current_price");
                    int stockQuantity = rs.getInt("stock_quantity");
                    String unit = rs.getString("unit");
                    
                    Product product = new Product(name, currentPrice, unit, stockQuantity);
                    product.setId(productId);
                    product.setDescription(description);
                    
                    // Создаем элемент заказа
                    OrderItem item = new OrderItem();
                    item.setId(id);
                    item.setOrderId(orderId);
                    item.setProductId(productId);
                    item.setQuantity(quantity);
                    item.setPricePerUnit(new BigDecimal(String.valueOf(price)));
                    item.setProduct(product);
                    
                    items.add(item);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order items: " + e.getMessage());
            e.printStackTrace();
        }
        
        return items;
    }

    @Override
    public boolean addItemToOrder(int orderId, int productId, int quantity, double price) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding item to order: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public boolean removeItemFromOrder(int orderItemId) {
        String sql = "DELETE FROM order_items WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderItemId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error removing item from order: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    private Order extractOrder(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        Timestamp orderDateTimestamp = rs.getTimestamp("order_date");
        LocalDateTime orderDate = orderDateTimestamp.toLocalDateTime();
        String status = rs.getString("status");
        double totalPrice = rs.getDouble("total_price");
        
        Order order = new Order();
        order.setId(id);
        order.setUserId(userId);
        order.setOrderDate(orderDate);
        order.setStatus(status);
        order.setTotalCost(new BigDecimal(String.valueOf(totalPrice)));
        
        return order;
    }
}