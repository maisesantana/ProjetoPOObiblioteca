package br.com.atlas.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//import java.util.Optional;

import br.com.atlas.model.Attendant;

public class AttendantDAO {

    // conexão com o banco de dados usada pelo DAO (final para não ser alterada após inicialização)
    private final Connection connection;

    public AttendantDAO(Connection connection) {
        this.connection = connection;
    }

    // verificação se o cpf existe
    public boolean exists(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM Attendant WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // CREATE
    public void insert(Attendant att) throws SQLException {
        String sql = "INSERT INTO Attendant (cpf) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, att.getCpf());
            stmt.executeUpdate();
        }
    }

    // READ ALL (com JOIN para pegar dados de Person + Employee)
    public List<Attendant> findAll() throws SQLException {
        String sql = """
            SELECT p.cpf, p.name, p.email, p.gender, p.birthDate, e.password
            FROM Person p
            JOIN Employee e ON p.cpf = e.cpf
            JOIN Attendant a ON a.cpf = e.cpf
        """;

        List<Attendant> attendants = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                attendants.add(mapResultSet(rs));
            }
        }

        return attendants;
    }

    // READ BY CPF
    public Attendant findByCpf(String cpf) throws SQLException {
        String sql = """
            SELECT p.cpf, p.name, p.email, p.gender, p.birthDate, e.password
            FROM Person p
            JOIN Employee e ON p.cpf = e.cpf
            JOIN Attendant a ON a.cpf = e.cpf
            WHERE p.cpf = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }

        return null;
    }

    // DELETE
    public void delete(String cpf) throws SQLException {
        String sql = "DELETE FROM Attendant WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        }
    }

    // MÉTODO AUXILIAR
    private Attendant mapResultSet(ResultSet rs) throws SQLException {
        String cpf = rs.getString("cpf");
        String name = rs.getString("name");
        String email = rs.getString("email");
        char gender = rs.getString("gender").charAt(0);
        LocalDate birthDate = rs.getDate("birthDate").toLocalDate();
        int password = rs.getInt("password");

        return new Attendant(cpf, name, email, gender, birthDate, password);
    }

    // READ BY NAME
    public List<Attendant> findByName(String name) throws SQLException {
        String sql = """
            SELECT p.cpf, p.name, p.email, p.gender, p.birthDate, e.password
            FROM Person p
            JOIN Employee e ON p.cpf = e.cpf
            JOIN Attendant a ON a.cpf = e.cpf
            WHERE p.name LIKE ?
        """;

        List<Attendant> attendants = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%"); // busca parcial

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    attendants.add(mapResultSet(rs));
                }
            }
        }
        return attendants;
    }
}