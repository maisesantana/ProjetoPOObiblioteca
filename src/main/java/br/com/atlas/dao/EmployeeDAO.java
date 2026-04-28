package br.com.atlas.dao;

import br.com.atlas.model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    protected final Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(Employee emp) throws SQLException {
        String sqlPerson = "INSERT INTO Person (Cpf, Name, Email, Gender, BirthDate) VALUES (?,?,?,?,?)";
        String sqlEmp    = "INSERT INTO Employee (Cpf, Password) VALUES (?,?)";

        try (PreparedStatement stmtP = connection.prepareStatement(sqlPerson)) {
            stmtP.setString(1, emp.getCpf());
            stmtP.setString(2, emp.getName());
            stmtP.setString(3, emp.getEmail());
            stmtP.setString(4, emp.getGender());
            stmtP.setDate(5, Date.valueOf(emp.getBirthDate()));
            stmtP.executeUpdate();
        }

        try (PreparedStatement stmtE = connection.prepareStatement(sqlEmp)) {
            stmtE.setString(1, emp.getCpf());
            stmtE.setInt(2, emp.getPassword());
            stmtE.executeUpdate();
        }
        // Sem commit aqui — a transação é controlada por quem chamou
    }

    public List<Employee> findAll() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT p.*, e.Password FROM Person p INNER JOIN Employee e ON p.Cpf = e.Cpf";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Employee(
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