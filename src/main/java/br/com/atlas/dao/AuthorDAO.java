package br.com.atlas.dao;

import br.com.atlas.model.Author;
import java.sql.*;

public class AuthorDAO {
    private final Connection connection;

    public AuthorDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Author author) throws SQLException {
        String sql = "INSERT INTO author (authorName) VALUES (?)";
        // Statement.RETURN_GENERATED_KEYS pega o ID que o MySQL criou na hora
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, author.getAuthorName());
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    author.setAuthorId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Author findByName(String name) throws SQLException {
        String sql = "SELECT * FROM author WHERE authorName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Author(rs.getInt("authorId"), rs.getString("authorName"));
                }
            }
        }
        return null;
    }
}