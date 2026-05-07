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
     * Tenta autenticar o usurio e retorna o objeto com o cargo correto.
     */
    public Optional<Employee> authenticate(String cpf, int password) {
    String sql = "SELECT 1 FROM Employee WHERE cpf = ? AND password = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, cpf);
        stmt.setInt(2, password);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {

                AdministratorDAO adminDAO = new AdministratorDAO(connection);
                if (adminDAO.exists(cpf)) return Optional.ofNullable(adminDAO.findByCpf(cpf));

                LibrarianDAO libDAO = new LibrarianDAO(connection);
                if (libDAO.exists(cpf)) return Optional.ofNullable(libDAO.findByCpf(cpf));

                AttendantDAO attDAO = new AttendantDAO(connection);
                if (attDAO.exists(cpf)) return Optional.ofNullable(attDAO.findByCpf(cpf));

                // fallback: CPF autenticado mas sem cargo definido
                System.err.println("AVISO: CPF " + cpf + " sem cargo definido.");
            }
        }

    } catch (SQLException e) { // ← agora captura os SQLException de todos os DAOs também
        e.printStackTrace();
    }

    return Optional.empty();
}
}