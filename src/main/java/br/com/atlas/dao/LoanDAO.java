package br.com.atlas.dao;

import br.com.atlas.model.BookCopy;
import br.com.atlas.model.Client;
import br.com.atlas.model.Loan;
import br.com.atlas.util.ConnectionDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // CREATE
    public void insert(Loan loan) {
        String sql = "INSERT INTO loan (cpf, bookCopyId, loanDate, expectedReturnDate, renewals, active) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConexao()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, loan.getClient().getCpf());
                stmt.setInt(2, loan.getBookCopy().getBookCopyId());
                stmt.setTimestamp(3, Timestamp.valueOf(loan.getLoanDate()));
                stmt.setTimestamp(4, Timestamp.valueOf(loan.getExpectedReturnDate()));
                stmt.setInt(5, loan.getRenewalCount());
                stmt.setBoolean(6, loan.isActive());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) loan.setLoanId(rs.getInt(1));
                }

                BookCopyDAO bcDAO = new BookCopyDAO(conn);
                loan.getBookCopy().setAvailable(false);
                bcDAO.update(loan.getBookCopy());

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ALL
    public List<Loan> findAll() {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loan";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(mapResultSet(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // READ BY ID
    public Loan findById(int id) {
        String sql = "SELECT * FROM loan WHERE loanId = ?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // BUSCA EMPRÉSTIMOS ATIVOS DE UM CLIENTE COM NOME DO LIVRO
    public List<Loan> findActiveLoansByCpf(String cpf) {
        List<Loan> list = new ArrayList<>();
        String sql = """
            SELECT l.loanId, l.cpf, l.bookCopyId, l.loanDate,
                   l.expectedReturnDate, l.renewals, l.active,
                   b.bookName
            FROM Loan l
            JOIN BookCopy bc ON l.bookCopyId = bc.bookCopyId
            JOIN Book b ON bc.bookId = b.bookId
            WHERE l.cpf = ? AND l.active = TRUE
        """;

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = mapResultSet(rs);
                    // Popula o objeto Book dentro do BookCopy com o nome do livro
                    br.com.atlas.model.Book book = new br.com.atlas.model.Book();
                    book.setBookName(rs.getString("bookName"));
                    loan.getBookCopy().setBook(book);
                    list.add(loan);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

// BUSCA TODOS OS EMPRÉSTIMOS ATIVOS COM NOME DO LIVRO E NOME DO CLIENTE
    public List<Loan> findAllActive() {
        List<Loan> list = new ArrayList<>();
        String sql = """
            SELECT l.loanId, l.cpf, l.bookCopyId, l.loanDate,
                l.expectedReturnDate, l.renewals, l.active,
                b.bookName,
                p.name AS clientName
            FROM Loan l
            JOIN BookCopy bc ON l.bookCopyId = bc.bookCopyId
            JOIN Book b      ON bc.bookId    = b.bookId
            JOIN Person p    ON l.cpf        = p.cpf
            WHERE l.active = TRUE
            ORDER BY l.expectedReturnDate ASC
        """;

        try (Connection conn = ConnectionDb.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Loan loan = mapResultSet(rs);

                // Popula nome do cliente
                Client client = loan.getClient();
                client.setName(rs.getString("clientName"));

                // Popula nome do livro dentro do BookCopy
                br.com.atlas.model.Book book = new br.com.atlas.model.Book();
                book.setBookName(rs.getString("bookName"));
                loan.getBookCopy().setBook(book);

                list.add(loan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    // UPDATE
    public boolean update(Loan loan) {
        String sql = "UPDATE loan SET cpf=?, bookCopyId=?, loanDate=?, expectedReturnDate=?, renewals=?, active=? WHERE loanId=?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loan.getClient().getCpf());
            stmt.setInt(2, loan.getBookCopy().getBookCopyId());
            stmt.setTimestamp(3, Timestamp.valueOf(loan.getLoanDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(loan.getExpectedReturnDate()));
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

    // CONTA EMPRÉSTIMOS ATIVOS DE UM CLIENTE (RF19)
    public int countActiveLoans(String cpf) {
        String sql = "SELECT COUNT(*) FROM loan WHERE cpf = ? AND active = TRUE";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean hasActiveLoans() {
        String sql = "SELECT COUNT(*) FROM loan WHERE active = TRUE";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> listActiveLoansInfo() {
        List<String> report = new ArrayList<>();
        String sql = """
            SELECT p.name AS clientName, b.bookName, l.loanDate, l.expectedReturnDate, l.renewals
            FROM Loan l
            JOIN Person p ON l.cpf = p.cpf
            JOIN BookCopy bc ON l.bookCopyId = bc.bookCopyId
            JOIN Book b ON bc.bookId = b.bookId
            WHERE l.active = TRUE
        """;

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String info = String.format(
                    "📖 Livro: %-15s | 👤 Cliente: %-15s | 📅 Devolução: %s | 🔄 Renov: %d",
                    rs.getString("bookName"),
                    rs.getString("clientName"),
                    rs.getTimestamp("expectedReturnDate").toLocalDateTime().toLocalDate(),
                    rs.getInt("renewals")
                );
                report.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return report;
    }

    private Loan mapResultSet(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setCpf(rs.getString("cpf"));

        BookCopy bc = new BookCopy();
        bc.setBookCopyId(rs.getInt("bookCopyId"));

        Loan loan = new Loan(c, bc);
        loan.setLoanId(rs.getInt("loanId"));
        loan.setLoanDate(rs.getTimestamp("loanDate").toLocalDateTime());
        loan.setExpectedReturnDate(rs.getTimestamp("expectedReturnDate").toLocalDateTime());
        loan.setActive(rs.getBoolean("active"));
        loan.setRenewalCount(rs.getInt("renewals"));

        return loan;
    }
}