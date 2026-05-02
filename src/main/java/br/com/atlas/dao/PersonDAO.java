package br.com.atlas.dao;

import br.com.atlas.model.Person;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {

    private final Connection connection;

    public PersonDAO(Connection connection) {
        this.connection = connection;
    }

    // Verificação se o CPF existe
    public boolean exists(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM Person WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // CREATE
    public void insert(Person person) throws SQLException {
        String sql = "INSERT INTO Person (cpf, name, email, gender, birthDate) VALUES (?,?,?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, person.getCpf());
            stmt.setString(2, person.getName());
            stmt.setString(3, person.getEmail());
            stmt.setString(4, String.valueOf(person.getGender()));
            stmt.setDate(5, Date.valueOf(person.getBirthDate()));

            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<Person> findAll() throws SQLException {
        String sql = "SELECT cpf, name, email, gender, birthDate FROM Person";
        List<Person> persons = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                persons.add(mapResultSet(rs));
            }
        }
        return persons;
    }

    // READ BY CPF
    public Person findByCpf(String cpf) throws SQLException {
        String sql = "SELECT cpf, name, email, gender, birthDate FROM Person WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public void update(Person person) throws SQLException {
        String sql = "UPDATE Person SET name = ?, email = ?, gender = ?, birthDate = ? WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, person.getName());
            stmt.setString(2, person.getEmail());
            stmt.setString(3, String.valueOf(person.getGender()));
            stmt.setDate(4, Date.valueOf(person.getBirthDate()));
            stmt.setString(5, person.getCpf());

            stmt.executeUpdate();
        }
    }

    // DELETE
    public void delete(String cpf) throws SQLException {
        String sql = "DELETE FROM Person WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        }
    }

    // MÉTODO AUXILIAR
    private Person mapResultSet(ResultSet rs) throws SQLException {
        String cpf = rs.getString("cpf");
        String name = rs.getString("name");
        String email = rs.getString("email");
        char gender = rs.getString("gender").charAt(0);
        LocalDate birthDate = rs.getDate("birthDate").toLocalDate();

        return new Person(cpf, name, email, gender, birthDate);
    }
}