package com.example.javafxwithjdbc.model.dao;

import com.example.javafxwithjdbc.model.entities.Client;

import java.util.List;

public interface ClientDao {

    void insert(Client client);
    List<Client> findAll();
}
