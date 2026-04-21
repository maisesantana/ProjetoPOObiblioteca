package br.com.atlas.dao;

import br.com.atlas.model.Client;
import br.com.atlas.util.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void insert(Client client) {
        String sql = "INSERT INTO Client (cpf, isStudying, address, zipCode, startSuspensionDate, endSuspensionDate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getCpf());
            stmt.setBoolean(2, client.isStudying());
            stmt.setString(3, client.getAddress());
            stmt.setString(4, client.getZipCode());

            // Tratamento para datas nulas de suspensão
            if (client.getStartSuspensionDate() != null) {
                stmt.setDate(5, Date.valueOf(client.getStartSuspensionDate()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            if (client.getEndSuspensionDate() != null) {
                stmt.setDate(6, Date.valueOf(client.getEndSuspensionDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Client> findAll() {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT p.*, c.isStudying, c.address, c.zipCode, c.startSuspernsionDate, c.endSuspensionDate FROM Person p " +
                "INNER JOIN Client c ON p.cpf = c.cpf";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Client c = new Client();
                c.setCpf(rs.getString("cpf"));
                c.setName(rs.getString("name"));
                c.setEmail(rs.getString("email"));
                c.setStudying(rs.getBoolean("isStudying"));
                c.setAddress(rs.getString("address"));
                c.setZipCode(rs.getString("zipCode"));

                if (rs.getDate("startSuspensionDate") != null) {
                    c.setStartSuspensionDate(rs.getDate("startSuspensionDate").toLocalDate());
                }
                if (rs.getDate("endSuspensionDate") != null) {
                    c.setEndSuspensionDate(rs.getDate("endSuspensionDate").toLocalDate());
                }

                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Client findById(String cpf) {
        String sql = "SELECT p.*, c.isStudying, c.address, c.zipCode, c.startSuspensionDate, c.endSuspensionDate FROM Person p " +
                "INNER JOIN Client c ON p.cpf = c.cpf";
        Client c = null;

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    c = new Client();
                    c.setCpf(rs.getString("cpf"));
                    c.setName(rs.getString("name"));
                    c.setEmail(rs.getString("email"));
                    c.setStudying(rs.getBoolean("isStudying"));
                    c.setAddress(rs.getString("address"));
                    c.setZipCode(rs.getString("zipCode"));

                    if (rs.getDate("startSuspensionDate") != null) {
                        c.setStartSuspensionDate(rs.getDate("startSuspensionDate").toLocalDate());
                    }
                    if (rs.getDate("endSuspensionDate") != null) {
                        c.setEndSuspensionDate(rs.getDate("endSuspensionDate").toLocalDate());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public boolean update(Client client) {
        String sql = "UPDATE Client SET isStudying=?, address=?, zipCode=?, startSuspensionDate=?, endSuspensionDate=? WHERE cpf=?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, client.isStudying());
            stmt.setString(2, client.getAddress());
            stmt.setString(3, client.getZipCode());

            if (client.getStartSuspensionDate() != null) {
                stmt.setDate(4, Date.valueOf(client.getStartSuspensionDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            if (client.getEndSuspensionDate() != null) {
                stmt.setDate(5, Date.valueOf(client.getEndSuspensionDate()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            stmt.setString(6, client.getCpf());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String cpf) {
        String sql = "DELETE FROM Client WHERE cpf=?";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}