package br.com.atlas.dao;

import br.com.atlas.model.Client;
import br.com.atlas.util.ConnectionDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public ClientDAO() { } // mantém construtor vazio 

    // CREATE
    public void insert(Client client) throws SQLException {

        String sqlPerson = "INSERT INTO person (cpf, name, email, gender, birthDate) VALUES (?,?,?,?,?)";
        String sqlClient = "INSERT INTO client (cpf, address) VALUES (?,?)";

        try (Connection conn = ConnectionDb.getConexao()) {
            conn.setAutoCommit(false); // controla transação manualmente

            try (PreparedStatement stmtP = conn.prepareStatement(sqlPerson);
                 PreparedStatement stmtC = conn.prepareStatement(sqlClient)) {

                // INSERE PERSON
                stmtP.setString(1, client.getCpf());
                stmtP.setString(2, client.getName());
                stmtP.setString(3, client.getEmail());
                stmtP.setString(4, String.valueOf(client.getGender()));
                stmtP.setDate(5, Date.valueOf(client.getBirthDate()));
                stmtP.executeUpdate();

                // INSERE CLIENT
                stmtC.setString(1, client.getCpf());
                stmtC.setString(2, client.getAddress());
                stmtC.executeUpdate();

                conn.commit(); // confirma tudo

            } catch (SQLException e) {
                conn.rollback(); // desfaz tudo se der erro
                throw e;
            }
        }
    }

    // READ ALL
    public List<Client> findAll() throws SQLException {

        List<Client> clients = new ArrayList<>();

        String sql = """
            SELECT p.*, c.address, c.startSuspensionDate, c.endSuspensionDate
            FROM person p
            JOIN client c ON p.cpf = c.cpf
        """;

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clients.add(mapResultSet(rs));
            }
        }

        return clients;
    }

    // READ BY CPF
    public Client findByCpf(String cpf) throws SQLException {

        String sql = """
            SELECT p.*, c.address, c.startSuspensionDate, c.endSuspensionDate
            FROM person p
            JOIN client c ON p.cpf = c.cpf
            WHERE p.cpf = ?
        """;

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        }

        return null;
    }

    //READ BY NAME
    public List<Client> findByName(String name) throws SQLException {

        String sql = """
            SELECT p.*, c.address, c.startSuspensionDate, c.endSuspensionDate
            FROM person p
            JOIN client c ON p.cpf = c.cpf
            WHERE p.name LIKE ?
        """;

        List<Client> clients = new ArrayList<>();

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%"); // busca parcial

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clients.add(mapResultSet(rs));
                }
            }
        }

        return clients;
    }

    //UPDATE 
    public void update(Client client) throws SQLException {

        String sqlPerson = "UPDATE person SET name=?, email=?, gender=?, birthDate=? WHERE cpf=?";
        String sqlClient = "UPDATE client SET address=?, startSuspensionDate=?, endSuspensionDate=? WHERE cpf=?";

        try (Connection conn = ConnectionDb.getConexao()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtP = conn.prepareStatement(sqlPerson);
                 PreparedStatement stmtC = conn.prepareStatement(sqlClient)) {

                // atualiza PERSON
                stmtP.setString(1, client.getName());
                stmtP.setString(2, client.getEmail());
                stmtP.setString(3, String.valueOf(client.getGender()));
                stmtP.setDate(4, Date.valueOf(client.getBirthDate()));
                stmtP.setString(5, client.getCpf());
                stmtP.executeUpdate();

                // atualiza CLIENT
                stmtC.setString(1, client.getAddress());

                if (client.getStartSuspensionDate() != null)
                    stmtC.setDate(2, Date.valueOf(client.getStartSuspensionDate()));
                else stmtC.setNull(2, Types.DATE);

                if (client.getEndSuspensionDate() != null)
                    stmtC.setDate(3, Date.valueOf(client.getEndSuspensionDate()));
                else stmtC.setNull(3, Types.DATE);

                stmtC.setString(4, client.getCpf());
                stmtC.executeUpdate();

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // DELETE
    public void delete(String cpf) throws SQLException {

        try (Connection conn = ConnectionDb.getConexao()) {
            conn.setAutoCommit(false);

            try (PreparedStatement s1 = conn.prepareStatement("DELETE FROM client WHERE cpf = ?");
                 PreparedStatement s2 = conn.prepareStatement("DELETE FROM person WHERE cpf = ?")) {

                s1.setString(1, cpf);
                s1.executeUpdate();

                s2.setString(1, cpf);
                s2.executeUpdate();

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // MÉTODO AUXILIAR
    private Client mapResultSet(ResultSet rs) throws SQLException {

        Client c = new Client(
            rs.getString("cpf"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("gender").charAt(0),
            rs.getDate("birthDate").toLocalDate(),
            rs.getString("address")
        );

        Date start = rs.getDate("startSuspensionDate");
        Date end = rs.getDate("endSuspensionDate");

        if (start != null) c.setStartSuspensionDate(start.toLocalDate());
        if (end != null) c.setEndSuspensionDate(end.toLocalDate());

        return c;
    }
}