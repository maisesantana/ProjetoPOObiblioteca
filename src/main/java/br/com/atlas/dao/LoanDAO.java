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

            BookCopyDAO bcDAO = new BookCopyDAO();
            loan.getBookCopy().setStatusAvailable(false);
            bcDAO.update(loan.getBookCopy());


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

    public Loan findById(int id) {
        String sql = "SELECT * FROM Loan WHERE loanId = ?";
        Loan loan = null;

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    loan = new Loan();
                    loan.setLoanId(rs.getInt("loanId"));

                    Client c = new Client();
                    c.setCpf(rs.getString("cpf"));
                    loan.setClient(c);

                    BookCopy bc = new BookCopy();
                    bc.setBookCopyId(rs.getInt("bookCopyId"));
                    loan.setBookCopy(bc);

                    if (rs.getTimestamp("loanDate") != null) {
                        loan.setLoanDate(rs.getTimestamp("loanDate").toLocalDateTime());
                    }
                    if (rs.getTimestamp("expectedReturnDate") != null) {
                        loan.setExpectedReturnDate(rs.getTimestamp("expectedReturnDate").toLocalDateTime());
                    }

                    loan.setRenewals(rs.getInt("renewals"));
                    loan.setActive(rs.getBoolean("active"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loan;
    }

    public boolean update(Loan loan) {
        String sql = "UPDATE Loan SET cpf=?, bookCopyId=?, loanDate=?, expectedReturnDate=?, renewals=?, active=? WHERE loanId=?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loan.getClient().getCpf());
            stmt.setInt(2, loan.getBookCopy().getBookCopyId());
            stmt.setTimestamp(3, Timestamp.valueOf(loan.getLoanDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(loan.getExpectedReturnDate()));
            stmt.setInt(5, loan.getRenewals());
            stmt.setBoolean(6, loan.isActive());
            stmt.setInt(7, loan.getLoanId());

            stmt.executeUpdate();

            // TÓPICO 3: Se o empréstimo foi desativado (devolução), o livro fica DISPONÍVEL
            if (!loan.isActive()) {
                BookCopyDAO bcDAO = new BookCopyDAO();
                loan.getBookCopy().setStatusAvailable(true);
                bcDAO.update(loan.getBookCopy());
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Loan WHERE loanId=?";

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