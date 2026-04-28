package br.com.atlas.dao;

import br.com.atlas.model.BookCopy;
import br.com.atlas.model.Client;
import br.com.atlas.model.Loan;
import br.com.atlas.util.ConnectionDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // CREATE (insere novo empréstimo no banco)
    public void insert(Loan loan) {

        String sql = "INSERT INTO loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConexao()) {

            conn.setAutoCommit(false); // controla transação (commit/rollback)

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, loan.getClient().getCpf());
                stmt.setInt(2, loan.getBookCopy().getBookCopyId());
                stmt.setTimestamp(3, Timestamp.valueOf(loan.getLoanDate()));
                stmt.setTimestamp(4, Timestamp.valueOf(loan.getExpectedReturnDate()));

                // salva quantidade de renovações (inicia com 0)
                stmt.setInt(5, loan.getRenewalCount());

                stmt.setBoolean(6, loan.isActive());

                stmt.executeUpdate();

                // atualiza disponibilidade do exemplar (fica indisponível)
                BookCopyDAO bcDAO = new BookCopyDAO();
                loan.getBookCopy().setAvailable(false);
                bcDAO.update(loan.getBookCopy());

                conn.commit(); // confirma tudo no banco

            } catch (SQLException e) {
                conn.rollback(); // desfaz tudo se der erro
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ALL (busca todos os empréstimos)
    public List<Loan> findAll() {

        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loan";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // usa método auxiliar pra montar o objeto
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // READ BY ID (busca empréstimo específico)
    public Loan findById(int id) {

        String sql = "SELECT * FROM loan WHERE loanId = ?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs); // usa método auxiliar
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE (atualiza dados do empréstimo)
    public boolean update(Loan loan) {

        String sql = "UPDATE loan SET cpf=?, bookCopyId=?, loanDate=?, expectedReturnDate=?, renewals=?, active=? WHERE loanId=?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loan.getClient().getCpf());
            stmt.setInt(2, loan.getBookCopy().getBookCopyId());
            stmt.setTimestamp(3, Timestamp.valueOf(loan.getLoanDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(loan.getExpectedReturnDate()));

            // atualiza quantidade de renovações
            stmt.setInt(5, loan.getRenewalCount());

            stmt.setBoolean(6, loan.isActive());
            stmt.setInt(7, loan.getLoanId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // MÉTODO AUXILIAR (evita repetição de código)
    private Loan mapResultSet(ResultSet rs) throws SQLException {

        // cria objetos básicos (lazy load - só cpf e id)
        Client c = new Client();
        c.setCpf(rs.getString("cpf"));

        BookCopy bc = new BookCopy();
        bc.setBookCopyId(rs.getInt("bookCopyId"));

        // monta o objeto Loan
        Loan loan = new Loan(c, bc);

        loan.setLoanId(rs.getInt("loanId"));
        loan.setLoanDate(rs.getTimestamp("loanDate").toLocalDateTime());
        loan.setExpectedReturnDate(rs.getTimestamp("expectedReturnDate").toLocalDateTime());
        loan.setActive(rs.getBoolean("active"));

        // pega quantidade de renovações do banco
        loan.setRenewalCount(rs.getInt("renewals"));

        return loan;
    }
}