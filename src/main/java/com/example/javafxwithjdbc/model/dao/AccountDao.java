package com.example.javafxwithjdbc.model.dao;


import com.example.javafxwithjdbc.model.entities.Account;

import java.util.List;

public interface AccountDao {

    void insert(Account account);
    List<Account> findAll();
    void withdraw(Account account, Double amount);
    void deposit(Account account, Double amount);
    void transfer(Account accountCreditor, Account accountDebtor, Double amount);
}
