package br.com.atlas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {
    
    private final Connection connection;

    public AuthorDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public void insert(String author) throws SQLException {
        String sql = "INSERT INTO author (authorName) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author);
            stmt.executeUpdate();
        }
    }

    // UPDATE
    public void update(String author) throws SQLException {
        String sql = "UPDATE author SET authorName = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author);
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void delete(String author) throws SQLException {
        String sql = "DELETE FROM author WHERE authorName = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author);
            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<String> findAll() throws SQLException {
        String sql = "SELECT authorName FROM author";
        List<String> authors = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                authors.add(rs.getString("authorName"));
            }
        }
        return authors;
    }
}
