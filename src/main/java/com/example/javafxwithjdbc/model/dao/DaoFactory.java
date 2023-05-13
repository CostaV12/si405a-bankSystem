package com.example.javafxwithjdbc.model.dao;

import com.example.javafxwithjdbc.db.DB;
import com.example.javafxwithjdbc.model.dao.impl.AccountDaoJDBC;
import com.example.javafxwithjdbc.model.dao.impl.ClientDaoJDBC;

public class DaoFactory {

    public static ClientDao createClientDao() {
        return new ClientDaoJDBC(DB.getConnection());
    }

    public static AccountDao createAccountDao() {
        return new AccountDaoJDBC(DB.getConnection());
    }
}
