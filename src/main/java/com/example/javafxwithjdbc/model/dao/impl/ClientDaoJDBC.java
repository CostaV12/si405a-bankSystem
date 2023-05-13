package com.example.javafxwithjdbc.model.dao.impl;

import com.example.javafxwithjdbc.db.DB;
import com.example.javafxwithjdbc.db.DbException;
import com.example.javafxwithjdbc.model.dao.ClientDao;
import com.example.javafxwithjdbc.model.entities.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDaoJDBC implements ClientDao {

    private final Connection connection;

    public ClientDaoJDBC(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void insert(Client client) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO Client " +
                            "(name, cpf, birth_date) " +
                            "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getCpf());
            preparedStatement.setDate(3, Date.valueOf(client.getBirthDate()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    client.setId(id);
                }
                DB.closeResultSet(resultSet);
            } else {
                throw new DbException("Unexpected error occurred! No rows returned!");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Client> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM Client ORDER BY Name");

            resultSet = preparedStatement.executeQuery();

            List<Client> clients = new ArrayList<>();

            while (resultSet.next()) {
                clients.add(instantiateClient(resultSet));
            }

            return clients;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }


    private Client instantiateClient(ResultSet resultSet) throws SQLException {
        return new Client(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("cpf"),
                resultSet.getDate("birth_date").toLocalDate());
    }
}
