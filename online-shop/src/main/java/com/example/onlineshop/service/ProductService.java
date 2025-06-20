package com.example.onlineshop.service;

import com.example.onlineshop.model.Product;
import java.util.List;

public interface ProductService {
    Product findById(int id);
    List<Product> findAll();
    List<Product> findByName(String name);
    List<Product> findInStock();
    Product addProduct(Product product);
    Product updateProduct(Product product);
    boolean deleteProduct(int id);
    boolean updateStock(int productId, int quantityChange);
    
    // Методы-алиасы для совместимости с контроллерами
    default List<Product> getAllProducts() {
        return findAll();
    }
    
    default List<Product> searchProducts(String query) {
        return findByName(query);
    }
}