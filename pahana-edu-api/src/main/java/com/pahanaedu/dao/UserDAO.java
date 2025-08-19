package com.pahanaedu.dao;

import com.pahanaedu.models.Role;
import com.pahanaedu.models.User;
import com.pahanaedu.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAO implements IUserDAO {

    private final Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));

        user.setRole(Role.valueOf(rs.getString("role").toUpperCase()));

        user.setActive(rs.getBoolean("is_active"));

        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        if (rs.getTimestamp("last_login") != null) {
            user.setLastLogin(rs.getTimestamp("last_login").toLocalDateTime());
        }

        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users (username, password_hash, full_name, email, role, is_active, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // The Statement.RETURN_GENERATED_KEYS flag is crucial to get the new user's ID back
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getFullName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getRole().name());
            statement.setBoolean(6, user.isActive());
            statement.setTimestamp(7, java.sql.Timestamp.valueOf(user.getCreatedAt()));

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // Get the auto-generated ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                    return user;
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // It's better to re-throw a custom exception here in a real app
            throw new RuntimeException("Error creating user in database", e);
        }
    }

}