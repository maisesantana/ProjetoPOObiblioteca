package br.com.atlas.dao;

import br.com.atlas.model.BookCopy;
import br.com.atlas.util.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookCopyDAO {

    public void insert(BookCopy copy) {
        String sql = "INSERT INTO BookCopy (bookId, statusAvailable, publicationYear) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, copy.getBook().getBookId());
            stmt.setBoolean(2, copy.isStatusAvailable());
            stmt.setInt(3, copy.getPublicationYear());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BookCopy> findAll() {
        List<BookCopy> list = new ArrayList<>();
        String sql = "SELECT * FROM BookCopy";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BookCopy bc = new BookCopy();
                bc.setBookCopyId(rs.getInt("bookCopyId"));
                br.com.atlas.model.Book b = new br.com.atlas.model.Book();
                b.setBookId(rs.getInt("bookId"));
                bc.setStatusAvailable(rs.getBoolean("statusAvailable"));
                bc.setPublicationYear(rs.getInt("publicationYear"));
                list.add(bc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}