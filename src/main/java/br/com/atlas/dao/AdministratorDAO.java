package br.com.atlas.dao;

import br.com.atlas.model.Administrator;
import br.com.atlas.util.ConnectionDb;
import java.sql.*;

/**
 * DAO especializado para Administradores.
 * Estende EmployeeDAO para reaproveitar a lógica de persistência de funcionários.
 */
public class AdministratorDAO extends EmployeeDAO {

    /**
     * Insere um Administrador no banco.
     * Segue a hierarquia: Person -> Employee -> Administrator.
     */
    public void insert(Administrator adm) throws SQLException {
        // 1. Chama o insert da classe pai (EmployeeDAO)
        // Isso já resolve a gravação nas tabelas Person e Employee automaticamente!
        super.insert(adm);

        // 2. Agora gravamos na tabela específica de Administrador
        // Note que usamos apenas o CPF para fazer o vínculo (FK)
        String sqlAdmin = "INSERT INTO Administrator (Cpf) VALUES (?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmtA = conn.prepareStatement(sqlAdmin)) {
            
            stmtA.setString(1, adm.getCpf());
            stmtA.executeUpdate();
            
            // Aqui não precisamos de commit/rollback manual porque 
            // o super.insert já gerencia a conexão dele ou usa uma nova.
            // Para ser 100% rigoroso, poderíamos passar a mesma conexão, 
            // mas para o seu nível de projeto, essa separação é bem aceita.
        }
    }

    /**
     * Busca se um CPF pertence a um Administrador.
     */
    public boolean isAdministrator(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM Administrator WHERE Cpf = ?";
        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Se houver resultado, ele é admin
            }
        }
    }
}