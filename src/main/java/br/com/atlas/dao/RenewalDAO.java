package br.com.atlas.dao;

import br.com.atlas.model.Renewal;
import br.com.atlas.util.ConnectionDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RenewalDAO {

    // CREATE (registra uma renovação)
    public void insert(Renewal renewal) {

        String sql = "INSERT INTO Renewal (NewReturnDate, RenewalNumber, LoanId) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(renewal.getNewReturnDate()));
            stmt.setInt(2, renewal.getRenewalNumber());
            stmt.setInt(3, renewal.getLoan().getLoanId());

            stmt.executeUpdate();

            // Recupera o ID gerado para o objeto Java
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    renewal.setRenewalId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (busca renovações de um empréstimo)
    public List<Renewal> findByLoanId(int loanId) {

        List<Renewal> list = new ArrayList<>();

        String sql = "SELECT * FROM Renewal WHERE LoanId = ? ORDER BY RenewalNumber";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loanId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Renewal r = new Renewal(
                        rs.getTimestamp("NewReturnDate").toLocalDateTime(),
                        rs.getInt("RenewalNumber"),
                        null // não precisa carregar Loan completo
                    );
                    r.setRenewalId(rs.getInt("renewalId")); // Garante que o ID venha na busca
                    list.add(r);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}