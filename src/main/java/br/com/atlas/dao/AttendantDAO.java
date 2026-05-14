package br.com.atlas.dao;

import br.com.atlas.model.Attendant;
import br.com.atlas.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendantDAO implements EmployeeTypeDAO {

    private final Connection connection;

    public AttendantDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean exists(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM Attendant WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void insert(Employee emp) throws SQLException {
        String sql = "INSERT INTO Attendant (cpf) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, emp.getCpf());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String cpf) throws SQLException {
        String sql = "DELETE FROM Attendant WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        }
    }

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
                if (rs.next()) return mapResultSet(rs);
            }
        }
        return null;
    }

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
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) attendants.add(mapResultSet(rs));
            }
        }
        return attendants;
    }

    private Attendant mapResultSet(ResultSet rs) throws SQLException {
        return new Attendant(
            rs.getString("cpf"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("gender").charAt(0),
            rs.getDate("birthDate").toLocalDate(),
            rs.getInt("password")
        );
    }
}