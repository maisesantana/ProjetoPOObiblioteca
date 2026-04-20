package br.com.atlas.dao;

import br.com.atlas.model.BookCopy;
import br.com.atlas.model.Client;
import br.com.atlas.model.Loan;
import br.com.atlas.util.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    public void insert(Loan loan) {
        String sql = "INSERT INTO Loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loan.getClient().getCpf());
            stmt.setInt(2, loan.getBookCopy().getBookCopyId());
            stmt.setTimestamp(3, Timestamp.valueOf(loan.getLoanDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(loan.getExpectedReturnDate()));
            stmt.setInt(5, loan.getRenewals());
            stmt.setBoolean(6, loan.isActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public List<Loan> findAll() {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM Loan";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Loan l = new Loan();
                l.setLoanId(rs.getInt("loanId"));
                Client c = new Client();
                c.setCpf(rs.getString("cpf"));
                l.setClient(c);
                BookCopy bc = new BookCopy();
                bc.setBookCopyId(rs.getInt("bookCopyId"));
                l.setBookCopy(bc);

                if (rs.getTimestamp("loanDate") != null) {
                    l.setLoanDate(rs.getTimestamp("loanDate").toLocalDateTime());
                }
                if (rs.getTimestamp("expectedReturnDate") != null) {
                    l.setExpectedReturnDate(rs.getTimestamp("expectedReturnDate").toLocalDateTime());
                }

                l.setRenewals(rs.getInt("renewals"));
                l.setActive(rs.getBoolean("active"));

                list.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}