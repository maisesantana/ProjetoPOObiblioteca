package br.com.atlas.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    
    private final Connection connection;

    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public void insert(String category) throws SQLException {
        String sql = "INSERT INTO category (categoryName) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category);
            stmt.executeUpdate();
        }
    }

    // UPDATE
    public void update(String category) throws SQLException {
        String sql = "UPDATE category SET categoryName = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category);
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void delete(String category) throws SQLException {
        String sql = "DELETE FROM category WHERE categoryName = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category);
            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<String> findAll() throws SQLException {
        String sql = "SELECT categoryName FROM category";
        List<String> categories = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("categoryName"));
            }
        }
        return categories;
    }
}
