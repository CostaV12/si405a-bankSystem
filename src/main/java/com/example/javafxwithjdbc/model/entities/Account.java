package com.example.javafxwithjdbc.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Account implements Serializable {

    private Integer id;

    private LocalDate openingDate;

    private Double balance;

    private Double transferLimit;

    private String account_type;

    private Client client;

    public Account() {}

    public Account(Integer id, String account_type, LocalDate openingDate, double balance, double transferLimit, Client client) {
        this.id = id;
        this.account_type = account_type;
        this.openingDate = openingDate;
        this.balance = balance;
        this.transferLimit = transferLimit;
        this.client = client;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Double getTransferLimit() {
        return transferLimit;
    }

    public void setTransferLimit(double transferLimit) {
        this.transferLimit = transferLimit;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Double.compare(account.getBalance(), getBalance()) == 0 && Double.compare(account.getTransferLimit(), getTransferLimit()) == 0 && Objects.equals(getId(), account.getId()) && Objects.equals(getOpeningDate(), account.getOpeningDate()) && Objects.equals(getClient(), account.getClient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOpeningDate(), getBalance(), getTransferLimit(), getClient());
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", openingDate=" + openingDate +
                ", balance=" + balance +
                ", transferLimit=" + transferLimit +
                ", client=" + client +
                '}';
    }
}
