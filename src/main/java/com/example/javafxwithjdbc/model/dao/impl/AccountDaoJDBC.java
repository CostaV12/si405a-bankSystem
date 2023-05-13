package com.example.javafxwithjdbc.model.dao.impl;

import com.example.javafxwithjdbc.db.DB;
import com.example.javafxwithjdbc.db.DbException;
import com.example.javafxwithjdbc.model.dao.AccountDao;
import com.example.javafxwithjdbc.model.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountDaoJDBC implements AccountDao {

    private final Connection connection;

    public AccountDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Account account) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO Account " +
                            "(account_type, balance, opening_date, transfer_limit, " +
                            "creditLimit, birth_date_account, client_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)");

            if (account instanceof SimpleAccount) {
                preparedStatement.setString(1, "simple");
                preparedStatement.setNull(5, Types.VARCHAR);
                preparedStatement.setNull(6, Types.VARCHAR);
            }
            else if (account instanceof SpecialAccount) {
                preparedStatement.setString(1, "special");
                preparedStatement.setDouble(5, ((SpecialAccount) account).getCreditLimit());
                preparedStatement.setNull(6, Types.VARCHAR);
            }
            else if (account instanceof SavingAccount) {
                preparedStatement.setString(1, "saving");
                preparedStatement.setNull(5, Types.VARCHAR);
                preparedStatement.setInt(6, ((SavingAccount) account).getBirthDateAccount());
            }
            preparedStatement.setDouble(2, account.getBalance());
            preparedStatement.setDate(3, Date.valueOf(account.getOpeningDate()));
            preparedStatement.setDouble(4, account.getTransferLimit());
            preparedStatement.setInt(7, account.getClient().getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Unexpected error occurred! No rows returned!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }


    }

    @Override
    public List<Account> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT a.*, c.* FROM Account AS a " +
                    "INNER JOIN Client AS c " +
                    "ON a.client_id = c.id " +
                    "ORDER BY a.opening_date");

            resultSet = preparedStatement.executeQuery();

            List<Account> accounts = new ArrayList<>();
            Map<Integer, Client> clientMap = new HashMap<>();

            while (resultSet.next()) {

                Client client = clientMap.get(resultSet.getInt("client_id"));

                if (client == null) {
                    client = instantiateClient(resultSet);
                    clientMap.put(resultSet.getInt("client_id"), client);
                }

                accounts.add(instantiateAccount(resultSet, client));
            }

            return accounts;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Account instantiateAccount(ResultSet resultSet, Client client) throws SQLException {
        switch (resultSet.getString("account_type")) {
            case "simple":
                return new SimpleAccount(
                        resultSet.getInt("id"),
                        resultSet.getString("account_type"),
                        resultSet.getDate("opening_date").toLocalDate(),
                        resultSet.getDouble("balance"),
                        resultSet.getDouble("transfer_limit"),
                        client
                );
            case "special":
                return new SpecialAccount(
                        resultSet.getInt("id"),
                        resultSet.getString("account_type"),
                        resultSet.getDouble("creditLimit"),
                        resultSet.getDate("opening_date").toLocalDate(),
                        resultSet.getDouble("balance"),
                        resultSet.getDouble("transfer_limit"),
                        client
                );
            case "saving":
                return new SavingAccount(
                        resultSet.getInt("id"),
                        resultSet.getString("account_type"),
                        resultSet.getDate("opening_date").toLocalDate(),
                        resultSet.getDouble("balance"),
                        resultSet.getDouble("transfer_limit"),
                        client,
                        resultSet.getInt("birth_date_account")
                );
            default:
                throw new SQLException("Error instantiate account");
        }
    }

    private Client instantiateClient(ResultSet resultSet) throws SQLException {
        return new Client(
                resultSet.getInt("client_id"),
                resultSet.getString("name"),
                resultSet.getString("cpf"),
                resultSet.getDate("birth_date").toLocalDate()
        );
    }

    @Override
    public void withdraw(Account account, Double amount) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE Account SET balance = ? WHERE id = ?"
            );

            preparedStatement.setDouble(1, account.getBalance() - amount);
            preparedStatement.setInt(2, account.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Unexpected error occurred! No rows returned!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deposit(Account account, Double amount) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE Account SET balance = ? WHERE id = ?"
            );

            preparedStatement.setDouble(1, account.getBalance() + amount);
            preparedStatement.setInt(2, account.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Unexpected error occurred! No rows returned!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void transfer(Account accountCreditor, Account accountDebtor, Double amount) {
        PreparedStatement preparedStatement = null;

        try {
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(
                    "UPDATE Account SET balance = ? WHERE id = ?"
            );

            preparedStatement.setDouble(1, accountCreditor.getBalance() - amount);
            preparedStatement.setInt(2, accountCreditor.getId());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(
                    "UPDATE Account SET balance = ? WHERE id = ?"
            );

            preparedStatement.setDouble(1, accountDebtor.getBalance() + amount);
            preparedStatement.setInt(2, accountDebtor.getId());
            preparedStatement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DbException(e.getMessage());
                }
            }
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }
}
