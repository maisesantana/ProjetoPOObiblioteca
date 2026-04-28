package br.com.atlas.dao;

import br.com.atlas.model.Administrator;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdministratorDAO {

    // conexão com o banco de dados usada pelo DAO (final para não ser alterada após inicialização)
    private final Connection connection;

    public AdministratorDAO(Connection connection) {
        this.connection = connection;
    }

    // verificação se o cpf existe
    public boolean exists(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM administrator WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // CREATE
    public void insert(Administrator admin) throws SQLException {
        String sql = "INSERT INTO administrator (cpf) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getCpf());
            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<Administrator> findAll() throws SQLException {
        String sql = """
            SELECT p.cpf, p.name, p.email, p.gender, p.birthDate, e.senha
            FROM person p
            JOIN employee e ON p.cpf = e.cpf
            JOIN administrator a ON a.cpf = e.cpf
        """;

        List<Administrator> admins = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                admins.add(mapResultSet(rs));
            }
        }

        return admins;
    }

    // READ BY CPF
    public Optional<Administrator> findByCpf(String cpf) throws SQLException {
        String sql = """
            SELECT p.cpf, p.name, p.email, p.gender, p.birthDate, e.senha
            FROM person p
            JOIN employee e ON p.cpf = e.cpf
            JOIN administrator a ON a.cpf = e.cpf
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
        String sql = "DELETE FROM administrator WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        }
    }

    // MÉTODO AUXILIAR
    private Administrator mapResultSet(ResultSet rs) throws SQLException {
        String cpf = rs.getString("cpf");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String gender = rs.getString("gender");
        LocalDate birthDate = rs.getDate("birthDate").toLocalDate();
        int senha = rs.getInt("senha");

        return new Administrator(cpf, name, email, gender, birthDate, senha);
    }

    // READ BY NAME
    public List<Administrator> findByName(String name) throws SQLException {
        String sql = """
            SELECT p.cpf, p.name, p.email, p.gender, p.birthDate, e.senha
            FROM person p
            JOIN employee e ON p.cpf = e.cpf
            JOIN administrator a ON a.cpf = e.cpf
            WHERE p.name LIKE ?
        """;

        List<Administrator> admins = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    admins.add(mapResultSet(rs));
                }
            }
        }
        return admins;
    }
}