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
            // controla manualmente exclusões

            PersonDAO personDAO = new PersonDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);

            // INSERE PERSON (tabela pai)
            personDAO.insert(emp);

            // INSERE EMPLOYEE
            employeeDAO.insert((Employee) emp);

            // INSERE NA TABELA ESPECÍFICA
            if (emp instanceof Attendant) {
                AttendantDAO dao = new AttendantDAO(conn);
                dao.insert((Attendant) emp);

            } else if (emp instanceof Librarian) {
                LibrarianDAO dao = new LibrarianDAO(conn);
                dao.insert((Librarian) emp);

            } else if (emp instanceof Administrator) {
                AdministratorDAO dao = new AdministratorDAO(conn);
                dao.insert((Administrator) emp);
            }

            conn.commit(); 
            // confirma tudo e salva no banco

        } catch (Exception e) {
            try {
                conn.rollback(); 
                // desfaz tudo, logo evita dados incompletos se der erro
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao cadastrar funcionário", e);
        }
    }

    @Override
    public void remove(String cpf) {
        Connection conn = ConnectionDb.getConexao();

        try {
            conn.setAutoCommit(false); 
            // controla manualmente exclusões

            AttendantDAO attendantDAO = new AttendantDAO(conn);
            LibrarianDAO librarianDAO = new LibrarianDAO(conn);
            AdministratorDAO adminDAO = new AdministratorDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);
            PersonDAO personDAO = new PersonDAO(conn);

            // remove da tabela específica primeiro
            if (attendantDAO.exists(cpf)) {
                attendantDAO.delete(cpf);
            } else if (librarianDAO.exists(cpf)) {
                librarianDAO.delete(cpf);
            } else if (adminDAO.exists(cpf)) {
                adminDAO.delete(cpf);
            }

            // remove das tabelas pai
            employeeDAO.delete(cpf);
            personDAO.delete(cpf);

            conn.commit(); 
            // confirma remoção completa

        } catch (Exception e) {
            try {
                conn.rollback(); 
                // cancela tudo se falhar no meio
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao remover funcionário", e);
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
            // garante que update seja feito por completo

            PersonDAO personDAO = new PersonDAO(conn);
            EmployeeDAO employeeDAO = new EmployeeDAO(conn);

            // atualiza dados
            personDAO.update(emp);
            employeeDAO.update((Employee) emp);

            conn.commit(); 
            // salva alterações

        } catch (Exception e) {
            try {
                conn.rollback(); 
                // desfaz alterações se der erro
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao atualizar funcionário", e);
        }
    }

    public void findPerson(String cpf) {
        try {
            Connection conn = ConnectionDb.getConexao();
            EmployeeDAO eDao = new EmployeeDAO(conn);

            if (eDao.exists(cpf)) {

                LibrarianDAO lDao = new LibrarianDAO(conn);
                AttendantDAO aDao = new AttendantDAO(conn);
                AdministratorDAO adDao = new AdministratorDAO(conn);

                if (lDao.exists(cpf)) {
                    
                }
                
                
            } else {
                throw new IllegalArgumentException("Cpf inexistente no sistema.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao encontrar funcionário", e);
        }
    }

    public List<Attendant> getAllAttendants() {
        try {
            Connection conn = ConnectionDb.getConexao();
            AttendantDAO attDAO = new AttendantDAO(conn);
            return attDAO.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao exibir atendentes", e);
        }
    }

    public List<Librarian> getAllLibrarians() {
        try {
            Connection conn = ConnectionDb.getConexao();
            LibrarianDAO libDAO = new LibrarianDAO(conn);
            return libDAO.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao exibir bibliotecários", e);
        }
    }

    public List<Administrator> getAllAdmins() {
        try {
            Connection conn = ConnectionDb.getConexao();
            AdministratorDAO adminDAO = new AdministratorDAO(conn);
            return adminDAO.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao exibir administradores", e);
        }
    }
}