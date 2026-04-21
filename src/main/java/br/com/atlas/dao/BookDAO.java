package br.com.atlas.dao;

import br.com.atlas.model.Book;
import br.com.atlas.model.Publisher;
import br.com.atlas.util.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void insert(Book book) {
        String sql = "INSERT INTO Book (bookName, bookLocation, numberOfPages, bookSubject, bookOrigin, publisherId) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getBookName());
            stmt.setString(2, book.getBookLocation());
            stmt.setInt(3, book.getNumberOfPages());
            stmt.setString(4, book.getBookSubject());
            stmt.setString(5, book.getBookOrigin());

            if (book.getPublisher() != null && book.getPublisher().getPublisherId() > 0) {
                stmt.setInt(6, book.getPublisher().getPublisherId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT b.*, p.publisherName FROM Book b " +
                "LEFT JOIN Publisher p ON b.publisherId = p.publisherId";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book b = new Book();
                b.setBookId(rs.getInt("bookId"));
                b.setBookName(rs.getString("bookName"));
                b.setBookLocation(rs.getString("bookLocation"));
                b.setNumberOfPages(rs.getInt("numberOfPages"));
                b.setBookSubject(rs.getString("bookSubject"));
                b.setBookOrigin(rs.getString("bookOrigin"));

                int pubId = rs.getInt("publisherId");
                if (!rs.wasNull()) {
                    Publisher p = new Publisher();
                    p.setPublisherId(pubId);
                    p.setPublisherName(rs.getString("publisherName"));
                    b.setPublisher(p);
                }
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Book findById(int id) {
        String sql = "SELECT * FROM Book WHERE bookId = ?";
        Book book = null;

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    book = new Book();
                    book.setBookId(rs.getInt("bookId"));
                    book.setBookName(rs.getString("bookName"));
                    book.setBookLocation(rs.getString("bookLocation"));
                    book.setNumberOfPages(rs.getInt("numberOfPages"));
                    book.setBookSubject(rs.getString("bookSubject"));
                    book.setBookOrigin(rs.getString("bookOrigin"));

                    int pubId = rs.getInt("publisherId");
                    if (!rs.wasNull()) {
                        Publisher p = new Publisher();
                        p.setPublisherId(pubId);
                        book.setPublisher(p);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    public boolean update(Book book) {
        String sql = "UPDATE Book SET bookName=?, bookLocation=?, numberOfPages=?, bookSubject=?, bookOrigin=?, publisherId=? WHERE bookId=?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getBookName());
            stmt.setString(2, book.getBookLocation());
            stmt.setInt(3, book.getNumberOfPages());
            stmt.setString(4, book.getBookSubject());
            stmt.setString(5, book.getBookOrigin());

            if (book.getPublisher() != null) {
                stmt.setInt(6, book.getPublisher().getPublisherId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setInt(7, book.getBookId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Book WHERE bookId=?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}