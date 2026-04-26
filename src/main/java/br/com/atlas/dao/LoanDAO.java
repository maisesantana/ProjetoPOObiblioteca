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
        String sql = "INSERT INTO Loan (Cpf, BookCopyId, LoanDate, ExpectedReturnDate, Renewals, Active) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loan.getClient().getCpf());
            stmt.setInt(2, loan.getBookCopy().getBookCopyId());
            stmt.setTimestamp(3, Timestamp.valueOf(loan.getLoanDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(loan.getExpectedReturnDate()));
            
            // O banco espera um INT, então pegamos o tamanho da lista de renovações
            stmt.setInt(5, loan.getRenewals().size());
            stmt.setBoolean(6, loan.isActive());

            stmt.executeUpdate();

            // Atualiza o exemplar para indisponível
            BookCopyDAO bcDAO = new BookCopyDAO();
            loan.getBookCopy().setAvailable(false); 
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
                // Usando construtor que aceita os objetos conforme o Model
                Client c = new Client();
                c.setCpf(rs.getString("Cpf"));
                
                BookCopy bc = new BookCopy();
                bc.setBookCopyId(rs.getInt("BookCopyId"));

                Loan l = new Loan(c, bc);
                l.setLoanId(rs.getInt("LoanId"));
                l.setLoanDate(rs.getTimestamp("LoanDate").toLocalDateTime());
                l.setExpectedReturnDate(rs.getTimestamp("ExpectedReturnDate").toLocalDateTime());
                l.setActive(rs.getBoolean("Active"));

                list.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Loan findById(int id) {
        String sql = "SELECT * FROM Loan WHERE LoanId = ?";
        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client c = new Client();
                    c.setCpf(rs.getString("Cpf"));
                    BookCopy bc = new BookCopy();
                    bc.setBookCopyId(rs.getInt("BookCopyId"));

                    Loan loan = new Loan(c, bc);
                    loan.setLoanId(rs.getInt("LoanId"));
                    loan.setLoanDate(rs.getTimestamp("LoanDate").toLocalDateTime());
                    loan.setExpectedReturnDate(rs.getTimestamp("ExpectedReturnDate").toLocalDateTime());
                    loan.setActive(rs.getBoolean("Active"));
                    return loan;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(Loan loan) {
        String sql = "UPDATE Loan SET Cpf=?, BookCopyId=?, LoanDate=?, ExpectedReturnDate=?, Renewals=?, Active=? WHERE LoanId=?";
        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loan.getClient().getCpf());
            stmt.setInt(2, loan.getBookCopy().getBookCopyId());
            stmt.setTimestamp(3, Timestamp.valueOf(loan.getLoanDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(loan.getExpectedReturnDate()));
            stmt.setInt(5, loan.getRenewals().size());
            stmt.setBoolean(6, loan.isActive());
            stmt.setInt(7, loan.getLoanId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}