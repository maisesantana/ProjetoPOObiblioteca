package br.com.atlas.dao;

import br.com.atlas.model.Renewal;
import br.com.atlas.util.ConnectionDb;
import java.sql.*;

public class RenewalDAO {

    public void insert(Renewal renewal) {
        // SQLs no padrão PascalCase do nosso banco
        String sqlRenewal = "INSERT INTO Renewal (NewReturnDate, RenewalNumber, LoanId) VALUES (?, ?, ?)";
        String sqlUpdateLoan = "UPDATE Loan SET ExpectedReturnDate = ? WHERE LoanId = ?";

        try (Connection conn = ConnectionDb.getConexao()) {
            conn.setAutoCommit(false); // Início da transação

            try {
                // 1. Insere o registro da renovação
                try (PreparedStatement stmtRen = conn.prepareStatement(sqlRenewal)) {
                    stmtRen.setTimestamp(1, Timestamp.valueOf(renewal.getNewReturnDate()));
                    stmtRen.setInt(2, renewal.getRenewalNumber());
                    stmtRen.setInt(3, renewal.getLoan().getLoanId());
                    stmtRen.executeUpdate();
                }

                // 2. Atualiza a data de previsão de devolução no Empréstimo
                try (PreparedStatement stmtLoan = conn.prepareStatement(sqlUpdateLoan)) {
                    stmtLoan.setTimestamp(1, Timestamp.valueOf(renewal.getNewReturnDate()));
                    stmtLoan.setInt(2, renewal.getLoan().getLoanId());
                    stmtLoan.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}