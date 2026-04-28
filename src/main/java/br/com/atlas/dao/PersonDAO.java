package br.com.atlas.dao;

import br.com.atlas.model.Person;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonDAO {

    private final Connection connection;

    public PersonDAO(Connection connection) {
        this.connection = connection;
    }

    // verificação se o cpf existe
    public boolean existsByCpf(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM person WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // CREATE
    public void insert(Person person) throws SQLException {
        String sql = "INSERT INTO person (cpf, name, email, gender, birthDate) VALUES (?,?,?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, person.getCpf());
            stmt.setString(2, person.getName());
            stmt.setString(3, person.getEmail());
            stmt.setString(4, person.getGender());
            stmt.setDate(5, Date.valueOf(person.getBirthDate()));
            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<Person> findAll() throws SQLException {
        String sql = "SELECT cpf, name, email, gender, birthDate FROM person";
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
    public Optional<Person> findByCpf(String cpf) throws SQLException {
        String sql = "SELECT cpf, name, email, gender, birthDate FROM person WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        }

        return Optional.empty();
    }

    // UPDATE
    public void update(Person person) throws SQLException {
        String sql = "UPDATE person SET name = ?, email = ?, gender = ?, birthDate = ? WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, person.getName());
            stmt.setString(2, person.getEmail());
            stmt.setString(3, person.getGender());
            stmt.setDate(4, Date.valueOf(person.getBirthDate()));
            stmt.setString(5, person.getCpf());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void delete(String cpf) throws SQLException {
        String sql = "DELETE FROM person WHERE cpf = ?";

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
        String gender = rs.getString("gender");
        LocalDate birthDate = rs.getDate("birthDate").toLocalDate();

        return new Person(cpf, name, email, gender, birthDate);
    }
}