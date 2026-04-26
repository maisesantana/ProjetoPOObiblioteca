package br.com.atlas.dao;

import br.com.atlas.model.Employee;
import br.com.atlas.util.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    /**
     * Método para inserir o Funcionário no banco.
     * Ele respeita a transação: salva na tabela Person e depois na Employee.
     */
    public void insert(Employee emp) throws SQLException {
        // Comandos SQL para as duas tabelas
        String sqlPerson = "INSERT INTO Person (Cpf, Name, Email, Gender, BirthDate) VALUES (?,?,?,?,?)";
        String sqlEmp = "INSERT INTO Employee (Cpf, Password) VALUES (?,?)";

        try (Connection conn = ConnectionDb.getConexao()) {
            // Desativa o auto-commit para garantir que os dois inserts funcionem juntos
            conn.setAutoCommit(false);
            
            try {
                // 1. Insere na tabela pai (Person)
                try (PreparedStatement stmtP = conn.prepareStatement(sqlPerson)) {
                    stmtP.setString(1, emp.getCpf());
                    stmtP.setString(2, emp.getName());
                    stmtP.setString(3, emp.getEmail());
                    stmtP.setString(4, emp.getGender());
                    stmtP.setDate(5, Date.valueOf(emp.getBirthDate()));
                    stmtP.executeUpdate();
                }
                
                // 2. Insere na tabela filha (Employee)
                try (PreparedStatement stmtE = conn.prepareStatement(sqlEmp)) {
                    stmtE.setString(1, emp.getCpf());
                    stmtE.setInt(2, emp.getPassword()); // Usa o atributo password da model
                    stmtE.executeUpdate();
                }
                
                // Confirma as duas operações
                conn.commit();
                
            } catch (SQLException e) {
                // Se der erro em qualquer uma, desfaz tudo para não sujar o banco
                conn.rollback();
                throw e;
            }
        }
    }

    /**
     * Lista todos os funcionários cadastrados unindo Person e Employee.
     */
    public List<Employee> findAll() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT p.*, e.Password FROM Person p INNER JOIN Employee e ON p.Cpf = e.Cpf";
        
        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                // Cria o objeto usando o construtor que definiu na model
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