package com.example.javafxwithjdbc.model.services;

import com.example.javafxwithjdbc.model.dao.AccountDao;
import com.example.javafxwithjdbc.model.dao.DaoFactory;
import com.example.javafxwithjdbc.model.entities.Account;

import java.util.List;

public class AccountService {

    private AccountDao dao = DaoFactory.createAccountDao();

    public List<Account> findAll() {
        return dao.findAll();
    }

    public void save(Account obj) {
        dao.insert(obj);
    }

    public void withdraw(Account account, Double amount) {
        dao.withdraw(account, amount);
    }

    public void deposit(Account account, Double amount) {
        dao.deposit(account, amount);
    }

    public void transfer(Account accountCreditor, Account accountDebtor, Double amount) {
        dao.transfer(accountCreditor, accountDebtor, amount);
    }
}
