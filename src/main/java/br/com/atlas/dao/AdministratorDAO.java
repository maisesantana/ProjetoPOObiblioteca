package br.com.atlas.dao;

import br.com.atlas.service.Administrator;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministratorDAO extends EmployeeDAO {

    public AdministratorDAO(Connection connection) {
        super(connection); // mesma conexão para toda a hierarquia
    }

    public void insert(Administrator adm) throws SQLException {
        // 1. Insere Person + Employee (usa a mesma conexão herdada)
        super.insert(adm);

        // 2. Insere na tabela Administrator
        String sqlAdmin = "INSERT INTO Administrator (Cpf) VALUES (?)";
        try (PreparedStatement stmtA = connection.prepareStatement(sqlAdmin)) {
            stmtA.setString(1, adm.getCpf());
            stmtA.executeUpdate();
        }
        // Ainda sem commit — a transação continua aberta para quem chamou
    }

    public boolean isAdministrator(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM Administrator WHERE Cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Administrator> findAllAdministrators() throws SQLException {
        List<Administrator> list = new ArrayList<>();
        String sql = """
            SELECT p.*, e.Password
            FROM Person p
            INNER JOIN Employee e ON p.Cpf = e.Cpf
            INNER JOIN Administrator a ON p.Cpf = a.Cpf
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Administrator(
                    rs.getString("Cpf"),
                    rs.getString("Name"),
                    rs.getString("Email"),
                    rs.getString("Gender"),
                    rs.getDate("BirthDate").toLocalDate(),
                    rs.getInt("Password")
                ));
            }
        }
        return list;
    }
}