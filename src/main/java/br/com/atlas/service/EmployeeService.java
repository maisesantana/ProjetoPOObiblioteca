package br.com.atlas.service;

import br.com.atlas.dao.*;
import br.com.atlas.model.*;
import br.com.atlas.util.ConnectionDb;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class EmployeeService {

    // BUSCA FUNCIONÁRIO POR CPF (detecta o tipo automaticamente)
    public Optional<Employee> findByCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório para busca!");
        }

        Connection conn = ConnectionDb.getConexao();

        try {
            EmployeeDAO eDao = new EmployeeDAO(conn);

            if (!eDao.exists(cpf)) {
                return Optional.empty();
            }

            LibrarianDAO lDao = new LibrarianDAO(conn);
            AttendantDAO aDao = new AttendantDAO(conn);
            AdministratorDAO adDao = new AdministratorDAO(conn);

            if (lDao.exists(cpf)) return Optional.ofNullable(lDao.findByCpf(cpf));
            if (aDao.exists(cpf)) return Optional.ofNullable(aDao.findByCpf(cpf));
            return Optional.ofNullable(adDao.findByCpf(cpf));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar funcionário por CPF.", e);
        }
    }

    // CREATE (detecta o tipo e insere nas tabelas corretas)
    public void insert(Employee emp) {
        validateEmployee(emp);

        Connection conn = ConnectionDb.getConexao();

        try {
            conn.setAutoCommit(false);

            PersonDAO personDAO = new PersonDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);

            personDAO.insert(emp);
            employeeDAO.insert(emp);

            if (emp instanceof Attendant) {
                new AttendantDAO(conn).insert((Attendant) emp);
            } else if (emp instanceof Librarian) {
                new LibrarianDAO(conn).insert((Librarian) emp);
            } else if (emp instanceof Administrator) {
                new AdministratorDAO(conn).insert((Administrator) emp);
            }

            conn.commit();

        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            throw new RuntimeException("Erro ao cadastrar funcionário.", e);
        }
    }

    // UPDATE
    public void update(Employee emp) {
        validateEmployee(emp);

        Connection conn = ConnectionDb.getConexao();

        try {
            conn.setAutoCommit(false);

            new PersonDAO(conn).update(emp);
            new EmployeeDAO(conn).update(emp);

            conn.commit();

        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            throw new RuntimeException("Erro ao atualizar funcionário.", e);
        }
    }

    // DELETE (detecta o tipo e remove nas tabelas corretas)
    public void delete(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório para deletar!");
        }

        Connection conn = ConnectionDb.getConexao();

        try {
            conn.setAutoCommit(false);

            AttendantDAO attendantDAO = new AttendantDAO(conn);
            LibrarianDAO librarianDAO = new LibrarianDAO(conn);
            AdministratorDAO adminDAO = new AdministratorDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);
            PersonDAO personDAO = new PersonDAO(conn);

            if (attendantDAO.exists(cpf))       attendantDAO.delete(cpf);
            else if (librarianDAO.exists(cpf))  librarianDAO.delete(cpf);
            else if (adminDAO.exists(cpf))      adminDAO.delete(cpf);

            employeeDAO.delete(cpf);
            personDAO.delete(cpf);

            conn.commit();

        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            throw new RuntimeException("Erro ao remover funcionário.", e);
        }
    }

    // READ ALL por tipo
    public List<Attendant> getAllAttendants() {
        try {
            return new AttendantDAO(ConnectionDb.getConexao()).findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar atendentes.", e);
        }
    }

    public List<Librarian> getAllLibrarians() {
        try {
            return new LibrarianDAO(ConnectionDb.getConexao()).findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar bibliotecários.", e);
        }
    }

    public List<Administrator> getAllAdmins() {
        try {
            return new AdministratorDAO(ConnectionDb.getConexao()).findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar administradores.", e);
        }
    }

    // VALIDAÇÃO CENTRALIZADA
    private void validateEmployee(Employee emp) {
        if (emp.getCpf() == null || emp.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório!");
        }
        if (emp.getName() == null || emp.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório!");
        }
        if (emp.getEmail() == null || emp.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório!");
        }
        if (emp.getGender() == '\0') {
            throw new IllegalArgumentException("Gênero é obrigatório!");
        }
        if (emp.getBirthDate() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória!");
        }
        if (emp.getPassword() == 0) {
            throw new IllegalArgumentException("Senha é obrigatória!");
        }
    }
}