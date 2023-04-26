package com.example.javafxwithjdbc.model.dao;


import com.example.javafxwithjdbc.model.entities.Department;

import java.util.List;

public interface DepartmentDao {
    void insert(Department department);
    void update(Department department);
    void deleteById(Integer id);
    Department findById(Integer id);
    List<Department> findAll();
}
