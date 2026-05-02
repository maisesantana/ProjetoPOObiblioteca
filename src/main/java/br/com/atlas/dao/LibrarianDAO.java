package br.com.atlas.dao;

import br.com.atlas.model.Librarian;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibrarianDAO {

    // conexo com o banco de dados usada pelo DAO (final para no ser alterada aps inicializao)
    private final Connection connection;

    public LibrarianDAO(Connection connection) {
        this.connection = connection;
    }

    // verificao se o cpf existe
    public boolean exists(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM Librarian WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // CREATE
    public void insert(Librarian lib) throws SQLException {
        String sql = "INSERT INTO Librarian (cpf) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lib.getCpf());
            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<Librarian> findAll() throws SQLException {
        String sql = """
            SELECT p.cpf, p.name, p.email, p.gender, p.birthDate, e.password
            FROM Person p
            JOIN Employee e ON p.cpf = e.cpf
            JOIN Librarian l ON l.cpf = e.cpf
        """;

        List<Librarian> librarians = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                librarians.add(mapResultSet(rs));
            }
        }

        return librarians;
    }

    // READ BY CPF
    public Optional<Librarian> findByCpf(String cpf) throws SQLException {
        String sql = """
            SELECT p.cpf, p.name, p.email, p.gender, p.birthDate, e.password
            FROM Person p
            JOIN Employee e ON p.cpf = e.cpf
            JOIN Librarian l ON l.cpf = e.cpf
            WHERE p.cpf = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        }

        return Optional.empty();
    }

    // DELETE
    public void delete(String cpf) throws SQLException {
        String sql = "DELETE FROM Librarian WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        }
    }

    // MTODO AUXILIAR
    private Librarian mapResultSet(ResultSet rs) throws SQLException {
        String cpf = rs.getString("cpf");
        String name = rs.getString("name");
        String email = rs.getString("email");
        char gender = rs.getString("gender").charAt(0);
        LocalDate birthDate = rs.getDate("birthDate").toLocalDate();
        int password = rs.getInt("password");

        return new Librarian(cpf, name, email, gender, birthDate, password);
    }

    // READ BY NAME
    public List<Librarian> findByName(String name) throws SQLException {
        String sql = """
            SELECT p.cpf, p.name, p.email, p.gender, p.birthDate, e.password
            FROM Person p
            JOIN Employee e ON p.cpf = e.cpf
            JOIN Librarian l ON l.cpf = e.cpf
            WHERE p.name LIKE ?
        """;

        List<Librarian> librarians = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    librarians.add(mapResultSet(rs));
                }
            }
        }
        return librarians;
    }
}