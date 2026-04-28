package br.com.atlas.service;

import java.time.LocalDate;

import br.com.atlas.dao.AdministratorDAO;
import br.com.atlas.dao.AttendantDAO;
import br.com.atlas.dao.EmployeeDAO;
import br.com.atlas.dao.LibrarianDAO;
import br.com.atlas.dao.PersonDAO;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Person;
import br.com.atlas.util.ConnectionDb;

public class Administrator extends Employee {


    public Administrator(String cpf, String name, String email, String gender,
            LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate, password);
    }

    @Override
    public void register(Person emp) {
        if (!(emp instanceof Employee)) {
            throw new IllegalArgumentException("Somente funcionários podem ser cadastrados!");
        }

        try {
            EmployeeDAO employeeDAO = new EmployeeDAO(ConnectionDb.getConexao());
            employeeDAO.insert((Employee) emp);

            if (emp instanceof Attendant) {
                AttendantDAO dao = new AttendantDAO();
                dao.insert((Attendant) emp);

            } else if (emp instanceof Librarian) {
                LibrarianDAO dao = new LibrarianDAO(ConnectionDb.getConexao());
                dao.insert((Librarian) emp);

            } else if (emp instanceof Administrator) {
                AdministratorDAO dao = new AdministratorDAO(ConnectionDb.getConexao());
                dao.insert((Administrator) emp);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar funcionário", e);
        }
    }

    @Override
    public void remove(String cpf) {
        try {
            // remove da tabela específica primeiro
            AttendantDAO attendantDAO = new AttendantDAO();
            LibrarianDAO librarianDAO = new LibrarianDAO(ConnectionDb.getConexao());
            AdministratorDAO adminDAO = new AdministratorDAO(ConnectionDb.getConexao());

            if (attendantDAO.exists(cpf)) {
                attendantDAO.delete(cpf);
            } else if (librarianDAO.exists(cpf)) {
                librarianDAO.delete(cpf);
            } else if (adminDAO.exists(cpf)) {
                adminDAO.delete(cpf);
            }

            //remove das tabelas pai
            EmployeeDAO employeeDAO = new EmployeeDAO(ConnectionDb.getConexao());
            employeeDAO.delete(cpf);

            PersonDAO personDAO = new PersonDAO(ConnectionDb.getConexao());
            personDAO.delete(cpf);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover funcionário", e);
        }
    }

    @Override
    public void update(Person emp) {
        if (!(emp instanceof Employee)) { //Só insere se for instancia de funcionario!!
            System.out.println("Somente funcionários podem ser atualizados!");
        }

        try {
            PersonDAO personDAO = new PersonDAO(ConnectionDb.getConexao());
            personDAO.update(emp);
            EmployeeDAO employeeDAO = new EmployeeDAO(ConnectionDb.getConexao());
            employeeDAO.update((Employee) emp);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar funcionário", e);
        }   
    }
}