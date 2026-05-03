package br.com.atlas.model;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import br.com.atlas.dao.*;
import br.com.atlas.util.ConnectionDb;

public class Administrator extends Employee {

    public Administrator(String cpf, String name, String email, char gender,
            LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate, password);
    }

    @Override
    public void register(Person emp) {
        if (!(emp instanceof Employee)) {
            throw new IllegalArgumentException("Somente funcionários podem ser cadastrados!");
        }

        Connection conn = ConnectionDb.getConexao();

        try {
            conn.setAutoCommit(false);

            PersonDAO personDAO = new PersonDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);

            // PERSON
            personDAO.insert(emp);

            // EMPLOYEE
            employeeDAO.insert((Employee) emp);

            // ESPECÍFICO
            if (emp instanceof Attendant) {
                new AttendantDAO(conn).insert((Attendant) emp);

            } else if (emp instanceof Librarian) {
                new LibrarianDAO(conn).insert((Librarian) emp);

            } else if (emp instanceof Administrator) {
                new AdministratorDAO(conn).insert((Administrator) emp);
            }

            conn.commit();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao cadastrar funcionário.", e);
        }
    }

    @Override
    public void remove(String cpf) {
        Connection conn = ConnectionDb.getConexao();

        try {
            conn.setAutoCommit(false);

            AttendantDAO attendantDAO = new AttendantDAO(conn);
            LibrarianDAO librarianDAO = new LibrarianDAO(conn);
            AdministratorDAO adminDAO = new AdministratorDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);
            PersonDAO personDAO = new PersonDAO(conn);

            if (attendantDAO.exists(cpf)) {
                attendantDAO.delete(cpf);

            } else if (librarianDAO.exists(cpf)) {
                librarianDAO.delete(cpf);

            } else if (adminDAO.exists(cpf)) {
                adminDAO.delete(cpf);
            }

            employeeDAO.delete(cpf);
            personDAO.delete(cpf);

            conn.commit();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao remover funcionário.", e);
        }
    }

    @Override
    public void update(Person emp) {
        if (!(emp instanceof Employee)) {
            throw new IllegalArgumentException("Somente funcionários podem ser atualizados!");
        }

        Connection conn = ConnectionDb.getConexao();

        try {
            conn.setAutoCommit(false);

            PersonDAO personDAO = new PersonDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);

            personDAO.update(emp);
            employeeDAO.update((Employee) emp);

            conn.commit();

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao atualizar funcionário.", e);
        }
    }

    @Override
    public Person findPersonByCpf(String cpf) {

        Connection conn = ConnectionDb.getConexao();

        try {
            EmployeeDAO eDao = new EmployeeDAO(conn);

            if (!eDao.exists(cpf)) {
                throw new IllegalArgumentException("CPF inexistente no sistema.");
            }

            LibrarianDAO lDao = new LibrarianDAO(conn);
            AttendantDAO aDao = new AttendantDAO(conn);
            AdministratorDAO adDao = new AdministratorDAO(conn);

            if (lDao.exists(cpf)) {
                return lDao.findByCpf(cpf);

            } else if (aDao.exists(cpf)) {
                return aDao.findByCpf(cpf);

            } else {
                return adDao.findByCpf(cpf);
            }

        } catch (IllegalArgumentException e) {
            throw e; // mantém erro de regra

        } catch (Exception e) {
            throw new RuntimeException("Erro ao acessar o banco de dados.", e);
        }
    }

    public List<Attendant> getAllAttendants() {
        try {
            Connection conn = ConnectionDb.getConexao();
            return new AttendantDAO(conn).findAll();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao exibir atendentes.", e);
        }
    }

    public List<Librarian> getAllLibrarians() {
        try {
            Connection conn = ConnectionDb.getConexao();
            return new LibrarianDAO(conn).findAll();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao exibir bibliotecários.", e);
        }
    }

    public List<Administrator> getAllAdmins() {
        try {
            Connection conn = ConnectionDb.getConexao();
            return new AdministratorDAO(conn).findAll();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao exibir administradores.", e);
        }
    }
}