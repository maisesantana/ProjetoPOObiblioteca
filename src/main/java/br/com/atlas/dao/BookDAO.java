package br.com.atlas.dao;

import br.com.atlas.model.Book;
import br.com.atlas.util.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void insert(Book book) {
        String sql = "INSERT INTO Book (BookName, BookLocation, NumberOfPages, BookSubject, Publisher) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getBookName());
            stmt.setString(2, book.getBookLocation());
            stmt.setInt(3, book.getNumberOfPages());
            stmt.setString(4, book.getBookSubject());
            stmt.setString(5, book.getPublisher()); // Agora é String!

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM Book";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book b = new Book();
                b.setBookId(rs.getInt("BookId"));
                b.setBookName(rs.getString("BookName"));
                b.setBookLocation(rs.getString("BookLocation"));
                b.setNumberOfPages(rs.getInt("NumberOfPages"));
                b.setBookSubject(rs.getString("BookSubject"));
                b.setPublisher(rs.getString("Publisher"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}