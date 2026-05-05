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
        
        // SQLs para a lógica de suspensão
        String sqlGetClient = "SELECT endSuspensionDate FROM Client WHERE cpf = ?";
        String sqlNewSuspension = "UPDATE Client SET StartSuspensionDate = ?, EndSuspensionDate = ? WHERE Cpf = ?";
        String sqlExtendSuspension = "UPDATE Client SET EndSuspensionDate = ? WHERE Cpf = ?";

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

                // 4. Lógica de Suspensão Corrigida (Cumulativa)
                if (returnObj.isLate()) {
                    int diasParaAdicionar = returnObj.calculateSuspensionDays();
                    String cpf = returnObj.getLoan().getClient().getCpf();
                    LocalDate dataFimAtual = null;

                    // Busca se o cliente já possui uma suspensão ativa no banco
                    try (PreparedStatement stmtGet = conn.prepareStatement(sqlGetClient)) {
                        stmtGet.setString(1, cpf);
                        try (ResultSet rs = stmtGet.executeQuery()) {
                            if (rs.next()) {
                                Date dbDate = rs.getDate("endSuspensionDate");
                                if (dbDate != null) dataFimAtual = dbDate.toLocalDate();
                            }
                        }
                    }

                    // Verifica se a suspensão atual ainda é válida (isSuspended logic)
                    if (dataFimAtual != null && dataFimAtual.isAfter(LocalDate.now())) {
                        // CASO 1: JÁ ESTÁ SUSPENSO. Somamos os novos dias à data final existente.
                        LocalDate novaDataFim = dataFimAtual.plusDays(diasParaAdicionar);
                        try (PreparedStatement stmtExtend = conn.prepareStatement(sqlExtendSuspension)) {
                            stmtExtend.setDate(1, Date.valueOf(novaDataFim));
                            stmtExtend.setString(2, cpf);
                            stmtExtend.executeUpdate();
                        }
                    } else {
                        // CASO 2: NOVA SUSPENSÃO. Começa a contar a partir de hoje.
                        LocalDate start = LocalDate.now();
                        LocalDate end = start.plusDays(diasParaAdicionar);
                        try (PreparedStatement stmtNew = conn.prepareStatement(sqlNewSuspension)) {
                            stmtNew.setDate(1, Date.valueOf(start));
                            stmtNew.setDate(2, Date.valueOf(end));
                            stmtNew.setString(3, cpf);
                            stmtNew.executeUpdate();
                        }
                    }
                }

                conn.commit(); // Confirma todas as operações de uma vez
            } catch (SQLException e) {
                conn.rollback(); // Se qualquer passo falhar, desfaz tudo
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}