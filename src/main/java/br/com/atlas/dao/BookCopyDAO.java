package br.com.atlas.dao;

import br.com.atlas.model.Book;
import br.com.atlas.model.BookCopy;
import br.com.atlas.util.ConnectionDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookCopyDAO {

    public boolean insert(BookCopy copy) {
        String sql = "INSERT INTO BookCopy (bookId, statusAvailable, publicationYear) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, copy.getBook().getBookId());
            stmt.setBoolean(2, copy.isStatusAvailable());
            stmt.setInt(3, copy.getPublicationYear());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<BookCopy> findAll() {
        List<BookCopy> list = new ArrayList<>();
        String sql = "SELECT bc.*, b.bookName FROM BookCopy bc " +  "INNER JOIN Book b ON bc.bookId = b.bookId";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BookCopy copy = new BookCopy();
                copy.setBookCopyId(rs.getInt("bookCopyId"));
                copy.setStatusAvailable(rs.getBoolean("statusAvailable"));
                copy.setPublicationYear(rs.getInt("publicationYear"));

                Book b = new Book();
                b.setBookId(rs.getInt("bookId"));
                b.setBookOrigin(rs.getString("bookId"));

                copy.setBook(b);
                list.add(copy);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public BookCopy findById(int id){
        String sql = "SELECT bc.*, b.bookName FROM BookCopy bc " +
        "INNER JOIN Book b ON bc.bookId = b.bookId WHERE bc.bookCopyId = ?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BookCopy copy = new BookCopy();
                    copy.setBookCopyId(rs.getInt("bookCopyId"));
                    copy.setStatusAvailable(rs.getBoolean("statusAvailable"));
                    copy.setPublicationYear(rs.getInt("publicationYear"));

                    Book b = new Book();
                    b.setBookId(rs.getInt("bookId"));
                    b.setBookName(rs.getString("bookName"));

                    copy.setBook(b);
                    return copy;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean update(BookCopy copy) {
        String sql = "UPDATE BookCopy SET bookId=?, statusAvailable=?, publicationYear=? WHERE bookCopyId=?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, copy.getBook().getBookId());
            stmt.setBoolean(2, copy.isStatusAvailable());
            stmt.setInt(3, copy.getPublicationYear());
            stmt.setInt(4, copy.getBookCopyId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean delete(int id) {
        String sql = "DELETE FROM BookCopy WHERE bookCopyId=?";

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