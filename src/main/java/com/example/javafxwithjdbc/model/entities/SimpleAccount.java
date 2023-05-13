package com.example.javafxwithjdbc.model.entities;


import com.example.javafxwithjdbc.model.exceptions.AccountException;

import java.time.LocalDate;


public class SimpleAccount extends Account{

    public SimpleAccount() {}

    public SimpleAccount(Integer id, String account_type, LocalDate openingDate, Double balance, Double transferLimit, Client client) {
        super(id, account_type, openingDate, balance, transferLimit, client);
    }
}
