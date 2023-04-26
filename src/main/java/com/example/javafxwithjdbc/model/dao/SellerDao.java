package com.example.javafxwithjdbc.model.dao;


import com.example.javafxwithjdbc.model.entities.Seller;

import java.util.List;

public interface SellerDao {
    void insert(Seller seller);
    void update(Seller seller);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();
    List<Seller> findByDepartment(Integer departmentId);
}
