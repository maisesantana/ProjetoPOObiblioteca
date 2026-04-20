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
        // Fazemos um JOIN para pegar os dados da Person e do Client juntos
        String sql = "SELECT p.*, c.isStudying, c.address, c.zipCode FROM Person p " +
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

                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}