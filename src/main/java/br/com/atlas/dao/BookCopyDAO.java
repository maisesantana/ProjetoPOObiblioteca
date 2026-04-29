package br.com.atlas.dao;

import br.com.atlas.model.Book;
import br.com.atlas.model.BookCopy;
import br.com.atlas.util.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookCopyDAO {

    public boolean insert(BookCopy copy) {
        
        String sql = "INSERT INTO BookCopy (BookId, StatusAvailable) VALUES (?, ?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, copy.getBook().getBookId());
            stmt.setBoolean(2, copy.isAvailable()); 

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<BookCopy> findAll() {
        List<BookCopy> list = new ArrayList<>();
        String sql = "SELECT bc.*, b.BookName FROM BookCopy bc " +
                     "INNER JOIN Book b ON bc.BookId = b.BookId";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BookCopy copy = new BookCopy();
                copy.setBookCopyId(rs.getInt("BookCopyId"));
                copy.setAvailable(rs.getBoolean("StatusAvailable"));

                Book b = new Book();
                b.setBookId(rs.getInt("BookId"));
                b.setBookName(rs.getString("BookName"));

                copy.setBook(b);
                list.add(copy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public BookCopy findById(int id) {
        String sql = "SELECT bc.*, b.BookName FROM BookCopy bc " +
                     "INNER JOIN Book b ON bc.BookId = b.BookId WHERE bc.BookCopyId = ?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BookCopy copy = new BookCopy();
                    copy.setBookCopyId(rs.getInt("BookCopyId"));
                    copy.setAvailable(rs.getBoolean("StatusAvailable"));

                    Book b = new Book();
                    b.setBookId(rs.getInt("BookId"));
                    b.setBookName(rs.getString("BookName"));

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
        String sql = "UPDATE BookCopy SET StatusAvailable = ? WHERE BookCopyId = ?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, copy.isAvailable());
            stmt.setInt(2, copy.getBookCopyId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM BookCopy WHERE BookCopyId = ?";
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

    public boolean insertByBookId(int bookId) {
        String sql = "INSERT INTO BookCopy (BookId, StatusAvailable) VALUES (?, ?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            stmt.setBoolean(2, true);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}