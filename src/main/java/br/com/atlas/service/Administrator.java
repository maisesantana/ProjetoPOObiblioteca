package br.com.atlas.service;

import java.time.LocalDate;

import br.com.atlas.dao.EmployeeDAO;
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
        if (!(emp instanceof Employee)) { //Só insere se for instancia de funcionario!!
            throw new IllegalArgumentException("Somente funcionários podem ser cadastrados!");
        }

        try {
            EmployeeDAO employeeDAO = new EmployeeDAO();
            employeeDAO.insert((Employee)emp); //converto pra Funcionario
        }  catch (Exception e) {
        throw new RuntimeException("Erro ao cadastrar funcionário", e);
        }
    }

    @Override
    public void remove(String cpf) {
        try {
            
            EmployeeDAO employeeDAO = new EmployeeDAO();
            employeeDAO.delete(cpf);
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
            personDAO.update((Employee)emp);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar funcionário", e);
        }   
    }
}