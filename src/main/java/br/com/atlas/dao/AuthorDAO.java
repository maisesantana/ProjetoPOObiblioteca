package br.com.atlas.dao;

import br.com.atlas.model.Author;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Author> findAll() throws SQLException {
        List<Author> list = new ArrayList<>();
        String sql = "SELECT * FROM author ORDER BY authorName";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Author(rs.getInt("authorId"), rs.getString("authorName")));
            }
        }
        return list;
    }

    public void update(Author author) throws SQLException {
        String sql = "UPDATE author SET authorName = ? WHERE authorId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author.getAuthorName());
            stmt.setInt(2, author.getAuthorId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM author WHERE authorId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // O CORAÇÃO DO FILTRO: Busca livros de um autor
    public List<String> findBooksByAuthor(int authorId) throws SQLException {
        List<String> books = new ArrayList<>();
        String sql = "SELECT b.bookName FROM Book b " +
                    "JOIN BookAuthor ba ON b.bookId = ba.bookId " +
                    "WHERE ba.authorId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) books.add(rs.getString("bookName"));
            }
        }
        return books;
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