package com.pahanaedu.dao;

import com.pahanaedu.models.Customer;
import com.pahanaedu.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAO implements ICustomerDAO {

    private final Connection connection;

    public CustomerDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY full_name ASC";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public Optional<Customer> findById(long id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setAccountNumber(rs.getString("account_number"));
        customer.setFullName(rs.getString("full_name"));
        customer.setAddress(rs.getString("address"));
        customer.setCity(rs.getString("city"));
        customer.setPostalCode(rs.getString("postal_code"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setEmail(rs.getString("email"));
        customer.setDateRegistered(rs.getTimestamp("date_registered").toLocalDateTime());
        customer.setActive(rs.getBoolean("is_active"));
        customer.setUnitsConsumed(rs.getDouble("units_consumed"));
        return customer;
    }

    @Override
    public Customer create(Customer customer) {
        String sql = "INSERT INTO customers (account_number, full_name, address, city, postal_code, phone_number, email, date_registered, is_active, units_consumed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.getAccountNumber());
            statement.setString(2, customer.getFullName());
            statement.setString(3, customer.getAddress());
            statement.setString(4, customer.getCity());
            statement.setString(5, customer.getPostalCode());
            statement.setString(6, customer.getPhoneNumber());
            statement.setString(7, customer.getEmail());
            statement.setTimestamp(8, java.sql.Timestamp.valueOf(customer.getDateRegistered()));
            statement.setBoolean(9, customer.isActive());
            statement.setDouble(10, customer.getUnitsConsumed());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getLong(1));
                    return customer;
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // In a real application, you'd throw a custom DataAccessException
            throw new RuntimeException("Error creating customer in database.", e);
        }
    }

    @Override
    public void update(Customer customer) {
        String sql = "UPDATE customers SET account_number = ?, full_name = ?, address = ?, city = ?, " +
                "postal_code = ?, phone_number = ?, email = ?, is_active = ?, units_consumed = ? " +
                "WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getAccountNumber());
            statement.setString(2, customer.getFullName());
            statement.setString(3, customer.getAddress());
            statement.setString(4, customer.getCity());
            statement.setString(5, customer.getPostalCode());
            statement.setString(6, customer.getPhoneNumber());
            statement.setString(7, customer.getEmail());
            statement.setBoolean(8, customer.isActive());

            // Note: date_registered is not updated as it's a fixed historical record
            statement.setDouble(9, customer.getUnitsConsumed());
            statement.setLong(10, customer.getId()); // The ID for the WHERE clause

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating customer failed, no rows affected. Customer with ID " + customer.getId() + " may not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating customer in database.", e);
        }
    }


    @Override
    public boolean existsByAccountNumber(String accountNumber, Long customerIdToIgnore) {
        // Build the query. The second part of the WHERE clause is conditional.
        String sql = "SELECT COUNT(*) FROM customers WHERE account_number = ? AND id != ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.setLong(2, customerIdToIgnore);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email, Long customerIdToIgnore) {
        String sql = "SELECT COUNT(*) FROM customers WHERE email = ? AND id != ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setLong(2, customerIdToIgnore);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}