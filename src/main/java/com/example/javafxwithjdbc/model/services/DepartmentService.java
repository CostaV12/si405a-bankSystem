package com.example.javafxwithjdbc.model.services;

import com.example.javafxwithjdbc.model.dao.DaoFactory;
import com.example.javafxwithjdbc.model.dao.DepartmentDao;
import com.example.javafxwithjdbc.model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private DepartmentDao dao = DaoFactory.createDepartmentDao();
    public List<Department> findAll() {
        return dao.findAll();
    }
}
