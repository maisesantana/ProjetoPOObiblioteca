package br.com.atlas.service;

import br.com.atlas.dao.PersonDAO;
import br.com.atlas.model.Person;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PersonService {

    private final PersonDAO dao;

    public PersonService(PersonDAO dao) {
        this.dao = dao;
    }

    // VALIDAÇÃO CENTRALIZADA (era private no DAO)
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

        //char não pode ser null, logo verifica se é vazio (char padrão = '\0')
        if (person.getGender() == '\0') {
            throw new IllegalArgumentException("Gênero é obrigatório!");
        }

        if (person.getBirthDate() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória!");
        }
    }

    // CREATE
    public void insert(Person person) throws SQLException {
        validatePerson(person);

        if (dao.exists(person.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado: " + person.getCpf());
        }

        dao.insert(person);
    }

    // READ ALL
    public List<Person> findAll() throws SQLException {
        return dao.findAll();
    }

    // READ BY CPF
    public Optional<Person> findByCpf(String cpf) throws SQLException {
        return Optional.ofNullable(dao.findByCpf(cpf));
    }

    // UPDATE
    public void update(Person person) throws SQLException {
        validatePerson(person);

        Optional<Person> existing = Optional.ofNullable(dao.findByCpf(person.getCpf()));
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Nenhum registro encontrado com o CPF: " + person.getCpf());
        }

        dao.update(person);
    }

    // DELETE
    public void delete(String cpf) throws SQLException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório para deletar!");
        }

        Optional<Person> existing = Optional.ofNullable(dao.findByCpf(cpf));
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Nenhum registro encontrado com o CPF: " + cpf);
        }

        dao.delete(cpf);
    }
}