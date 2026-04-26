package br.com.atlas.dao;

import br.com.atlas.model.Client;
import br.com.atlas.util.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public ClientDAO() { } // Construtor vazio para o Controller funcionar

    public void insert(Client client) throws SQLException {
        String sqlPerson = "INSERT INTO Person (Cpf, Name, Email, Gender, BirthDate) VALUES (?,?,?,?,?)";
        String sqlClient = "INSERT INTO Client (Cpf, Address) VALUES (?,?)";

        try (Connection conn = ConnectionDb.getConexao()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtP = conn.prepareStatement(sqlPerson);
                 PreparedStatement stmtC = conn.prepareStatement(sqlClient)) {

                stmtP.setString(1, client.getCpf());
                stmtP.setString(2, client.getName());
                stmtP.setString(3, client.getEmail());
                stmtP.setString(4, client.getGender());
                stmtP.setDate(5, Date.valueOf(client.getBirthDate()));
                stmtP.executeUpdate();

                stmtC.setString(1, client.getCpf());
                stmtC.setString(2, client.getAddress());
                stmtC.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT p.*, c.Address, c.StartSuspensionDate, c.EndSuspensionDate " +
                     "FROM Person p INNER JOIN Client c ON p.Cpf = c.Cpf";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                clients.add(mapResultSet(rs));
            }
        }
        return clients;
    }

    public Client findByCpf(String cpf) throws SQLException {
        String sql = "SELECT p.*, c.Address, c.StartSuspensionDate, c.EndSuspensionDate " +
                     "FROM Person p INNER JOIN Client c ON p.Cpf = c.Cpf WHERE p.Cpf = ?";
        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        }
        return null;
    }

    public void delete(String cpf) throws SQLException {
        try (Connection conn = ConnectionDb.getConexao()) {
            conn.setAutoCommit(false);
            try (PreparedStatement s1 = conn.prepareStatement("DELETE FROM Client WHERE Cpf = ?");
                 PreparedStatement s2 = conn.prepareStatement("DELETE FROM Person WHERE Cpf = ?")) {
                s1.setString(1, cpf); s1.executeUpdate();
                s2.setString(1, cpf); s2.executeUpdate();
                conn.commit();
            } catch (SQLException e) { conn.rollback(); throw e; }
        }
    }

    private Client mapResultSet(ResultSet rs) throws SQLException {
        Client c = new Client(rs.getString("Cpf"), rs.getString("Name"), rs.getString("Email"),
                             rs.getString("Gender"), rs.getDate("BirthDate").toLocalDate(), rs.getString("Address"));
        Date start = rs.getDate("StartSuspensionDate");
        Date end = rs.getDate("EndSuspensionDate");
        if (start != null) c.setStartSuspensionDate(start.toLocalDate());
        if (end != null) c.setEndSuspensionDate(end.toLocalDate());
        return c;
    }
}