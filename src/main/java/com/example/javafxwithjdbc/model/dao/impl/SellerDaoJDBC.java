package com.example.javafxwithjdbc.model.dao.impl;

import com.example.javafxwithjdbc.model.dao.SellerDao;
import com.example.javafxwithjdbc.model.entities.Department;
import com.example.javafxwithjdbc.model.entities.Seller;
import db.DB;
import db.DbException;


import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private final Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, Date.valueOf(seller.getBirthDate()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5,seller.getDepartment().getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    seller.setId(id);
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
    public void update(Seller seller) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE seller "
                    + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                    + "WHERE Id = ?");

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, Date.valueOf(seller.getBirthDate()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5,seller.getDepartment().getId());
            preparedStatement.setInt(6, seller.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("DELETE FROM seller WHERE Id = ?");

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT s.*,d.Name as DepName "
                    + "FROM seller AS s INNER JOIN department AS d "
                    + "ON s.DepartmentId = d.Id "
                    + "WHERE s.Id = ?");

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Department department = instantiateDepartment(resultSet);

                return instantiateSeller(resultSet, department);
            } else {
                return null;
            }


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        return new Seller(
                resultSet.getInt("Id"),
                resultSet.getString("Name"),
                resultSet.getString("Email"),
                resultSet.getDate("BirthDate").toLocalDate(),
                resultSet.getDouble("BaseSalary"),
                department);
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        return new Department(resultSet.getInt("DepartmentId"), resultSet.getString("DepName"));
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT s.*,d.Name as DepName "
                            + "FROM seller AS s INNER JOIN department as d "
                            + "ON s.DepartmentId = d.Id "
                            + "ORDER BY Name");

            resultSet = preparedStatement.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()) {

                Department department = departmentMap.get(resultSet.getInt("DepartmentId"));

                if (department == null) {
                    department = instantiateDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"), department);
                }

                sellers.add(instantiateSeller(resultSet, department));
            }

            return sellers;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Seller> findByDepartment(Integer departmentId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT s.*,d.Name as DepName "
                    + "FROM seller AS s INNER JOIN department as d "
                    + "ON s.DepartmentId = d.Id "
                    + "WHERE DepartmentId = ? "
                    + "ORDER BY Name");

            preparedStatement.setInt(1, departmentId);
            resultSet = preparedStatement.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()) {

                Department department = departmentMap.get(departmentId);

                if (department == null) {
                    department = instantiateDepartment(resultSet);
                    departmentMap.put(departmentId, department);
                }

                sellers.add(instantiateSeller(resultSet, department));
            }

            return sellers;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
