package br.com.atlas.dao;

import br.com.atlas.model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Category> findAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Category ORDER BY categoryName";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("categoryId"), rs.getString("categoryName")));
            }
        }
        return list;
    }

    public void update(Category category) throws SQLException {
        String sql = "UPDATE Category SET categoryName = ? WHERE categoryId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setInt(2, category.getCategoryId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Category WHERE categoryId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // O FILTRO: Busca títulos dos livros por categoria
    public List<String> findBooksByCategory(int catId) throws SQLException {
        List<String> books = new ArrayList<>();
        String sql = "SELECT b.bookName FROM Book b " +
                    "JOIN BookCategory bc ON b.bookId = bc.bookId " +
                    "WHERE bc.categoryId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, catId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) books.add(rs.getString("bookName"));
            }
        }
        return books;
    }
}