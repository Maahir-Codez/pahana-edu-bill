package com.pahanaedu.dao;

import com.pahanaedu.models.Item;
import com.pahanaedu.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDAO implements IItemDAO {
    private final Connection connection;

    public ItemDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Item create(Item item) {
        String sql = "INSERT INTO items (sku, name, description, category, author, price, stock_quantity, is_active, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getSku());
            statement.setString(2, item.getName());
            statement.setString(3, item.getDescription());
            statement.setString(4, item.getCategory());
            statement.setString(5, item.getAuthor());
            statement.setBigDecimal(6, item.getPrice());
            statement.setInt(7, item.getStockQuantity());
            statement.setBoolean(8, item.isActive());
            statement.setTimestamp(9, Timestamp.valueOf(item.getCreatedAt()));
            statement.setTimestamp(10, Timestamp.valueOf(item.getUpdatedAt()));

            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getLong(1));
                }
            }
            return item;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating item", e);
        }
    }

    @Override
    public Optional<Item> findById(long id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items ORDER BY name ASC";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public void update(Item item) {
        String sql = "UPDATE items SET sku=?, name=?, description=?, category=?, author=?, price=?, stock_quantity=?, is_active=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getSku());
            statement.setString(2, item.getName());
            statement.setString(3, item.getDescription());
            statement.setString(4, item.getCategory());
            statement.setString(5, item.getAuthor());
            statement.setBigDecimal(6, item.getPrice());
            statement.setInt(7, item.getStockQuantity());
            statement.setBoolean(8, item.isActive());
            statement.setLong(9, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating item", e);
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting item", e);
        }
    }

    @Override
    public boolean existsBySku(String sku, Long itemIdToIgnore) {
        String sql = "SELECT COUNT(*) FROM items WHERE sku = ? AND id != ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sku);
            statement.setLong(2, itemIdToIgnore);
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

    private Item mapResultSetToItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getLong("id"));
        item.setSku(rs.getString("sku"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setCategory(rs.getString("category"));
        item.setAuthor(rs.getString("author"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setStockQuantity(rs.getInt("stock_quantity"));
        item.setActive(rs.getBoolean("is_active"));
        item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        item.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return item;
    }
}