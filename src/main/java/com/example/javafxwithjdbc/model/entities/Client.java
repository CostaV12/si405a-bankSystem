package com.example.javafxwithjdbc.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Client implements Serializable {

    private Integer id;
    private String name;
    private String cpf;

    private LocalDate birthDate;

    private List<Account> accounts = new ArrayList<>();
    public Client() {}
    public Client(Integer id, String name, String cpf, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(getId(), client.getId()) && Objects.equals(getName(), client.getName()) && Objects.equals(getCpf(), client.getCpf()) && Objects.equals(getBirthDate(), client.getBirthDate()) && Objects.equals(getAccounts(), client.getAccounts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCpf(), getBirthDate(), getAccounts());
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", birthDate=" + birthDate +"}";
    }
}
