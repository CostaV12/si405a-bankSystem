package com.example.javafxwithjdbc.model.entities;

import com.example.javafxwithjdbc.model.exceptions.AccountException;

import java.time.LocalDate;

public class SpecialAccount extends Account{

    private Double creditLimit;

    public SpecialAccount() {}

    public SpecialAccount(Integer id, String account_type, Double creditLimit, LocalDate openingDate, Double balance, Double transferLimit, Client client) {
        super(id, account_type, openingDate, balance, transferLimit, client);
        this.creditLimit = creditLimit;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }
}
