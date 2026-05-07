package br.com.atlas.dao;

import br.com.atlas.model.Category;
import java.sql.*;

public class CategoryDAO {
    private final Connection connection;

    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Category category) throws SQLException {
        String sql = "INSERT INTO category (categoryName) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getCategoryName());
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setCategoryId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Category findByName(String name) throws SQLException {
        String sql = "SELECT * FROM category WHERE categoryName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("categoryId"), rs.getString("categoryName"));
                }
            }
        }
        return null;
    }
}