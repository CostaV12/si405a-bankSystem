package com.example.javafxwithjdbc.model.services;

import com.example.javafxwithjdbc.model.dao.ClientDao;
import com.example.javafxwithjdbc.model.dao.DaoFactory;
import com.example.javafxwithjdbc.model.entities.Client;

import java.util.List;

public class ClientService {

    private ClientDao dao = DaoFactory.createClientDao();

    public void save(Client obj) {
        dao.insert(obj);
    }
    public List<Client> findAll() {
        return dao.findAll();
    }
}
