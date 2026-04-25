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
    // !! protegida para que outras classes q herdam de pessoa consigam usar
    protected boolean existsByCpf(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM person WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf); // procura o cpf no banco

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true se já existe
            }
        }
    }

    // VALIDAÇÃO CENTRALIZADA
    private void validatePerson(Person person) {

        if (person.getCpf() == null || person.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório!");
        }
        if (person.getName() == null || person.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório!");
        }
        if (person.getEmail() == null || person.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório!");
        }
        if (person.getGender() == null || person.getGender().trim().isEmpty()) {
            throw new IllegalArgumentException("Gênero é obrigatório!");
        }
        if (person.getBirthDate() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória!");
        }
    }

    // CREATE
    public void insert(Person person) throws SQLException {

        // obriga colocar todos os campos
        validatePerson(person);

        // VALIDAÇÃO ANTES DE INSERIR
        if (existsByCpf(person.getCpf())) {
            throw new SQLException("CPF já cadastrado: " + person.getCpf());
        }

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

    // aq é pra saber quem no bd vc quer alterar/deletar ja q é uma pk
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

        return Optional.empty(); // não encontrou
    }

    // UPDATE
    public void update(Person person) throws SQLException {

        validatePerson(person);

        String sql = "UPDATE person SET name = ?, email = ?, gender = ?, birthDate = ? WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, person.getName());
            stmt.setString(2, person.getEmail());
            stmt.setString(3, person.getGender());
            stmt.setDate(4, Date.valueOf(person.getBirthDate()));
            stmt.setString(5, person.getCpf());
            int rows = stmt.executeUpdate(); // executa o update e retorna quantas linhas foram afetadas
            if (rows == 0) { // verifica se aquele cpf existe
                throw new SQLException("Nenhum registro encontrado com o CPF: " + person.getCpf());
            }
        }
    }

    // DELETE atraves do cpf
    public void delete(String cpf) throws SQLException {
        String sql = "DELETE FROM person WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf); // um unico parametro que é justament o cpf
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Nenhum registro encontrado com o CPF: " + cpf);
            }
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