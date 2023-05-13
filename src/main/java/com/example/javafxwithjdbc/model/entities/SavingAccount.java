package com.example.javafxwithjdbc.model.entities;


import com.example.javafxwithjdbc.model.exceptions.AccountException;

import java.time.LocalDate;

public class SavingAccount extends Account{

    private int birthDateAccount;

    public SavingAccount() {}

    public SavingAccount(Integer id, String account_type, LocalDate openingDate, Double balance, Double transferLimit, Client client, int birthDateAccount) {
        super(id, account_type, openingDate, balance, transferLimit, client);
        if (birthDateAccount < 1 || birthDateAccount > 31) {
            throw new AccountException("Give a valid int day");
        }
        this.birthDateAccount = birthDateAccount;
    }

    public int getBirthDateAccount() {
        return birthDateAccount;
    }

    public void setBirthDateAccount(int birthDateAccount) {
        this.birthDateAccount = birthDateAccount;
    }
}
