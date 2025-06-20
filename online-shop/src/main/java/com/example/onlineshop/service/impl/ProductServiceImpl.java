package com.example.onlineshop.service.impl;

import com.example.onlineshop.dao.ProductDAO;
import com.example.onlineshop.dao.impl.ProductDAOImpl;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    
    private final ProductDAO productDAO;
    
    public ProductServiceImpl() {
        this.productDAO = new ProductDAOImpl();
    }

    @Override
    public Product findById(int id) {
        return productDAO.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productDAO.findAll();
    }

    @Override
    public List<Product> findByName(String name) {
        return productDAO.findByName(name);
    }

    @Override
    public List<Product> findInStock() {
        return productDAO.findInStock();
    }

    @Override
    public Product addProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            System.err.println("Название товара не может быть пустым");
            return null;
        }
        
        if (product.getPrice() <= 0) {
            System.err.println("Цена товара должна быть положительной");
            return null;
        }
        
        if (product.getUnit() == null || product.getUnit().trim().isEmpty()) {
            System.err.println("Единица измерения не может быть пустой");
            return null;
        }
        
        if (product.getStockQuantity() < 0) {
            System.err.println("Количество товара на складе не может быть отрицательным");
            return null;
        }
        
        return productDAO.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product existingProduct = productDAO.findById(product.getProductId());
        
        if (existingProduct == null) {
            System.err.println("Товар с ID " + product.getProductId() + " не найден");
            return null;
        }
        
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            System.err.println("Название товара не может быть пустым");
            return null;
        }
        
        if (product.getPrice() <= 0) {
            System.err.println("Цена товара должна быть положительной");
            return null;
        }
        
        if (product.getUnit() == null || product.getUnit().trim().isEmpty()) {
            System.err.println("Единица измерения не может быть пустой");
            return null;
        }
        
        if (product.getStockQuantity() < 0) {
            System.err.println("Количество товара на складе не может быть отрицательным");
            return null;
        }
        
        return productDAO.update(product);
    }

    @Override
    public boolean deleteProduct(int id) {
        Product product = productDAO.findById(id);
        
        if (product == null) {
            System.err.println("Товар с ID " + id + " не найден");
            return false;
        }
        
        return productDAO.delete(id);
    }

    @Override
    public boolean updateStock(int productId, int quantityChange) {
        Product product = productDAO.findById(productId);
        
        if (product == null) {
            System.err.println("Товар с ID " + productId + " не найден");
            return false;
        }
        
        int newQuantity = product.getStockQuantity() + quantityChange;
        
        if (newQuantity < 0) {
            System.err.println("Недостаточно товара на складе");
            return false;
        }
        
        return productDAO.updateStock(productId, quantityChange);
    }
}