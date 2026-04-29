package br.com.atlas.dao;

import br.com.atlas.model.Employee;

import java.sql.*;

public class EmployeeDAO {

    // conexão com o banco de dados usada pelo DAO 
    // (final para não ser alterada após inicialização)
    private final Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    // verificação se o cpf existe
    public boolean exists(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM employee WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // CREATE
    public void insert(Employee emp) throws SQLException {
        String sql = "INSERT INTO employee (cpf, password) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, emp.getCpf()); // herda de Person
            stmt.setInt(2, emp.getPassword()); // atributo próprio de Employee
            stmt.executeUpdate();
        }
    }

    // UPDATE
    public void update(Employee emp) throws SQLException {
        String sql = "UPDATE employee SET password = ? WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, emp.getPassword());
            stmt.setString(2, emp.getCpf());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void delete(String cpf) throws SQLException {
        String sql = "DELETE FROM employee WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        }
    }
}