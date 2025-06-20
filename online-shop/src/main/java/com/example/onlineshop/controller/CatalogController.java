package com.example.onlineshop.controller;

import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.User;
import com.example.onlineshop.service.OrderService;
import com.example.onlineshop.service.ProductService;
import com.example.onlineshop.service.impl.OrderServiceImpl;
import com.example.onlineshop.service.impl.ProductServiceImpl;

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
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CatalogController extends FXController {

    @FXML
    private TextField searchField;
    
    @FXML
    private TableView<Product> productTable;
    
    @FXML
    private TableColumn<Product, String> nameColumn;
    
    @FXML
    private TableColumn<Product, String> descriptionColumn;
    
    @FXML
    private TableColumn<Product, Double> priceColumn;
    
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    
    @FXML
    private TableColumn<Product, Void> actionColumn;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private Label statusLabel;
    
    private final OrderService orderService;
    private final ProductService productService;
    private ObservableList<Product> productList;
    
    public CatalogController() {
        super();
        this.orderService = new OrderServiceImpl();
        this.productService = new ProductServiceImpl();
    }
    
    @FXML
    private void initialize() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            loadScene("login", "Авторизация");
            return;
        }
        
        setupTableColumns();
        loadProducts();
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchProducts(newValue);
        });
    }
    
    private void setupTableColumns() {
        nameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getName()));
        
        descriptionColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDescription()));
        
        priceColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        quantityColumn.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        
        actionColumn.setCellFactory(createActionCellFactory());
    }
    
    private Callback<TableColumn<Product, Void>, TableCell<Product, Void>> createActionCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button buyButton = new Button("Купить");
                    private final HBox pane = new HBox(5, buyButton);
                    
                    {
                        buyButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            handleBuyProduct(product);
                        });
                    }
                    
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
            }
        };
    }
    
    private void loadProducts() {
        List<Product> products = productService.findAll();
        productList = FXCollections.observableArrayList(products);
        productTable.setItems(productList);
    }
    
    private void searchProducts(String query) {
        if (query == null || query.isEmpty()) {
            loadProducts();
        } else {
            List<Product> filteredProducts = productService.findByName(query);
            productTable.setItems(FXCollections.observableArrayList(filteredProducts));
        }
    }
    
    @FXML
    private void handleSearch(ActionEvent event) {
        String query = searchField.getText().trim();
        searchProducts(query);
    }
    
    private void handleBuyProduct(Product product) {
        User user = getCurrentUser();
        if (user != null) {
            if (product.getStock() > 0) {
                // Запрашиваем количество товара для покупки
                TextInputDialog dialog = new TextInputDialog("1");
                dialog.setTitle("Покупка товара");
                dialog.setHeaderText("Товар: " + product.getName());
                dialog.setContentText("Введите количество (макс. " + product.getStock() + "):");

                Optional<String> result = dialog.showAndWait();
                
                if (result.isPresent()) {
                    try {
                        int quantity = Integer.parseInt(result.get());
                        
                        // Проверяем введенное количество
                        if (quantity <= 0) {
                            showError("Ошибка", "Количество товара должно быть больше 0");
                            return;
                        }
                        
                        if (quantity > product.getStock()) {
                            quantity = product.getStock();
                            showWarning("Внимание", "Количество товара уменьшено до доступного на складе: " + quantity);
                        }
                        
                        // Создаем заказ
                        BigDecimal totalPrice = new BigDecimal(product.getPrice() * quantity);
                        Order order = new Order();
                        order.setId(0);
                        order.setUserId(user.getId());
                        order.setOrderDate(LocalDateTime.now());
                        order.setTotalCost(totalPrice);
                        
                        // Сохраняем заказ и добавляем товар в заказ
                        boolean success = orderService.createOrderWithItem(order, product.getId(), quantity, product.getPrice());
                        
                        if (success) {
                            showInfo("Заказ оформлен", "Заказ успешно оформлен. Вы можете отслеживать его в разделе 'Мои заказы'.");
                            // Обновляем список товаров, чтобы отобразить обновлённое количество
                            loadProducts();
                        } else {
                            showError("Ошибка", "Не удалось оформить заказ. Попробуйте еще раз.");
                        }
                    } catch (NumberFormatException e) {
                        showError("Ошибка", "Введите корректное число");
                    }
                }
            } else {
                showError("Ошибка", "Товара нет в наличии.");
            }
        } else {
            loadScene("login", "Авторизация");
        }
    }
    
    @FXML
    private void handleGoToHome(ActionEvent event) {
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
    
    @FXML
    private void handleViewOrders(ActionEvent event) {
        loadScene("user_orders", "Мои заказы");
    }
}
