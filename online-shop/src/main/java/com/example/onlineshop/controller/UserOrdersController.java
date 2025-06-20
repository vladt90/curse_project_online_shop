package com.example.onlineshop.controller;

import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.OrderItem;
import com.example.onlineshop.model.User;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserOrdersController extends FXController {

    @FXML
    private TableView<Order> ordersTable;
    
    @FXML
    private TableColumn<Order, String> orderIdColumn;
    
    @FXML
    private TableColumn<Order, String> orderDateColumn;
    
    @FXML
    private TableColumn<Order, String> orderStatusColumn;
    
    @FXML
    private TableColumn<Order, Double> orderTotalColumn;
    
    @FXML
    private VBox orderDetailsBox;
    
    @FXML
    private Label orderDetailsLabel;
    
    @FXML
    private TableView<OrderItem> orderItemsTable;
    
    @FXML
    private TableColumn<OrderItem, String> itemNameColumn;
    
    @FXML
    private TableColumn<OrderItem, Integer> itemQuantityColumn;
    
    @FXML
    private TableColumn<OrderItem, Double> itemPriceColumn;
    
    @FXML
    private TableColumn<OrderItem, Double> itemTotalColumn;
    
    @FXML
    private Button backButton;
    
    private ObservableList<Order> orders;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @FXML
    private void initialize() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            loadScene("login", "Авторизация");
            return;
        }
        
        setupOrdersTable();
        loadOrders(currentUser.getId());
        
        // Скрываем детали заказа пока ни один заказ не выбран
        orderDetailsBox.setVisible(false);
    }
    
    private void setupOrdersTable() {
        // Настраиваем колонки таблицы заказов
        orderIdColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        
        orderDateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getOrderDate().format(dateFormatter)));
        
        orderStatusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatus()));
        
        orderTotalColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());
        
        // Добавляем обработчик выбора строки в таблице заказов
        ordersTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showOrderDetails(newValue));
        
        // Настраиваем колонки таблицы товаров в заказе
        itemNameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        
        itemQuantityColumn.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        
        itemPriceColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        itemTotalColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getSubtotal()).asObject());
    }
    
    private void loadOrders(int userId) {
        List<Order> userOrders = orderService.getOrdersByUserId(userId);
        orders = FXCollections.observableArrayList(userOrders);
        ordersTable.setItems(orders);
    }
    
    private void showOrderDetails(Order order) {
        if (order == null) {
            orderDetailsBox.setVisible(false);
            return;
        }
        
        // Показываем детали выбранного заказа
        orderDetailsBox.setVisible(true);
        orderDetailsLabel.setText("Детали заказа #" + order.getId() + 
                                 " от " + order.getOrderDate().format(dateFormatter));
        
        // Загружаем товары заказа
        List<OrderItem> items = orderService.getOrderItems(order.getId());
        orderItemsTable.setItems(FXCollections.observableArrayList(items));
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        User user = getCurrentUser();
        if (user != null) {
            if (user.getRoleId() == 1) {
                loadScene("admin_dashboard", "Панель администратора");
            } else {
                loadScene("user_dashboard", "Личный кабинет");
            }
        } else {
            loadScene("login", "Авторизация");
        }
    }
}