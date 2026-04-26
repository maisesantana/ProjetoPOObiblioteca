package br.com.atlas.dao;

import br.com.atlas.model.ReturnBook;
import br.com.atlas.util.ConnectionDb;
import java.time.LocalDate;
import java.sql.*;

public class ReturnBookDAO {

    public void insert(ReturnBook returnObj) {
        String sqlReturn = "INSERT INTO ReturnBook (ReturnDate, LoanId) VALUES (?, ?)";
        String sqlUpdateLoan = "UPDATE Loan SET Active = FALSE WHERE LoanId = ?";
        String sqlUpdateCopy = "UPDATE BookCopy SET StatusAvailable = TRUE WHERE BookCopyId = ?";
        String sqlUpdateClient = "UPDATE Client SET StartSuspensionDate = ?, EndSuspensionDate = ? WHERE Cpf = ?";

        try (Connection conn = ConnectionDb.getConexao()) {
            conn.setAutoCommit(false); // Transação para garantir consistência total

            try {
                // 1. Insere o registro de devolução
                try (PreparedStatement stmtReturn = conn.prepareStatement(sqlReturn)) {
                    stmtReturn.setTimestamp(1, Timestamp.valueOf(returnObj.getReturnDate()));
                    stmtReturn.setInt(2, returnObj.getLoan().getLoanId());
                    stmtReturn.executeUpdate();
                }

                // 2. Desativa o Empréstimo
                try (PreparedStatement stmtLoan = conn.prepareStatement(sqlUpdateLoan)) {
                    stmtLoan.setInt(1, returnObj.getLoan().getLoanId());
                    stmtLoan.executeUpdate();
                }

                // 3. Libera o Exemplar
                try (PreparedStatement stmtCopy = conn.prepareStatement(sqlUpdateCopy)) {
                    stmtCopy.setInt(1, returnObj.getLoan().getBookCopy().getBookCopyId());
                    stmtCopy.executeUpdate();
                }

                // 4. Se houver atraso, aplica a suspensão no Cliente
                if (returnObj.isLate()) {
                    try (PreparedStatement stmtClient = conn.prepareStatement(sqlUpdateClient)) {
                        LocalDate start = LocalDate.now();
                        LocalDate end = start.plusDays(returnObj.calculateSuspensionDays());
                        
                        stmtClient.setDate(1, Date.valueOf(start));
                        stmtClient.setDate(2, Date.valueOf(end));
                        stmtClient.setString(3, returnObj.getLoan().getClient().getCpf());
                        stmtClient.executeUpdate();
                    }
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