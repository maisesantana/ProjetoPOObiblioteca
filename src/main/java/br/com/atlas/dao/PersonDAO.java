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
        List<Person> persons = new ArrayList<>(); // faz a busca de todos e armazena nessa lista

        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                persons.add(mapResultSet(rs));
            }
        }

        return persons;
    }

    // ISSO EH PRA SABER NO BANCO DE DADOS QUEM VC QUER ALTERAR/DELETAR JA QUE EH A CHAVE PRIMARIA
    public Person findByCpf(String cpf) throws SQLException {
        String sql = "SELECT cpf, name, email, gender, birthDate FROM person WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }

        return null; // não encontrou
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

        Date date = rs.getDate("birthDate");
        LocalDate birthDate = (date != null) ? date.toLocalDate() : null;

        return new Person(cpf, name, email, gender, birthDate);
    }
}