package br.com.atlas.dao;

import br.com.atlas.model.Person;
import br.com.atlas.util.ConnectionDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {

    public void insert(Person person) {
        String sql = "INSERT INTO Person (cpf, name, socialName, email, gender, birthDate, password, phone, rg) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, person.getCpf());
            stmt.setString(2, person.getName());
            stmt.setString(3, person.getSocialName());
            stmt.setString(4, person.getEmail());
            stmt.setString(5, person.getGender());
            stmt.setDate(6, Date.valueOf(person.getBirthDate()));
            stmt.setString(7, person.getPassword());
            stmt.setString(8, person.getPhone());
            stmt.setString(9, person.getRg());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Person> findAll() {
        List<Person> list = new ArrayList<>();
        String sql = "SELECT * FROM Person";

        try (Connection conn = ConnectionDb.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Person p = new Person();
                p.setCpf(rs.getString("cpf"));
                p.setName(rs.getString("name"));
                p.setSocialName(rs.getString("socialName"));
                p.setEmail(rs.getString("email"));
                p.setGender(rs.getString("gender"));
                p.setBirthDate(rs.getDate("birthDate").toLocalDate());
                p.setPassword(rs.getString("password"));
                p.setPhone(rs.getString("phone"));
                p.setRg(rs.getString("rg"));

                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}