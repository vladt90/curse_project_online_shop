package com.example.onlineshop.dao;

import java.util.List;

public interface DAO<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    T update(T entity);
    boolean delete(ID id);
}