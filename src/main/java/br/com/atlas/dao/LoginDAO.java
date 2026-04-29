package br.com.atlas.dao;

import br.com.atlas.model.*;
import java.sql.*;
import java.util.Optional;

public class LoginDAO {

    private final Connection connection;

    public LoginDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Tenta autenticar o usuário e retorna o objeto com o cargo correto.
     */
    public Optional<Employee> authenticate(String cpf, int senha) {
        // 1. Primeiro validamos o básico: CPF e Senha existem na tabela Employee?
        String sql = "SELECT 1 FROM employee WHERE cpf = ? AND senha = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setInt(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 2. Credenciais válidas! Agora vamos descobrir o cargo por "eliminação"
                    
                    // Verificamos se é Administrador
                    AdministratorDAO adminDAO = new AdministratorDAO(connection);
                    if (adminDAO.exists(cpf)) {
                        return adminDAO.findByCpf(cpf).map(admin -> (Employee) admin);
                    }

                    // Verificamos se é Bibliotecário
                    LibrarianDAO libDAO = new LibrarianDAO(connection);
                    if (libDAO.exists(cpf)) {
                        return libDAO.findByCpf(cpf).map(lib -> (Employee) lib);
                    }

                    // Verificamos se é Atendente
                    AttendantDAO attDAO = new AttendantDAO(connection);
                    if (attDAO.exists(cpf)) {
                        return attDAO.findByCpf(cpf).map(att -> (Employee) att);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Optional.empty(); // Retorna vazio se a senha estiver errada ou o CPF não existir
    }
}