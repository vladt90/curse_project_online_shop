package com.example.onlineshop.dao;

import com.example.onlineshop.model.Product;
import java.util.List;

public interface ProductDAO extends DAO<Product, Integer> {
    List<Product> findByName(String name);
    List<Product> findInStock();
    boolean updateStock(int productId, int quantity);
}