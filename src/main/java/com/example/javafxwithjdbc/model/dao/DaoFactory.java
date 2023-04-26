package com.example.javafxwithjdbc.model.dao;

import com.example.javafxwithjdbc.model.dao.impl.DepartmentDaoJDBC;
import com.example.javafxwithjdbc.model.dao.impl.SellerDaoJDBC;
import db.DB;

public class DaoFactory {
    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
