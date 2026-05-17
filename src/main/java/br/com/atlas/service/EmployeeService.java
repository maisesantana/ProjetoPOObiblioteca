package br.com.atlas.service;

import br.com.atlas.dao.*;
import br.com.atlas.model.*;
import br.com.atlas.util.ConnectionDb;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeService {

    private EmployeeTypeDAO resolveDao(Employee emp, Connection conn) {
        if (emp instanceof Attendant)     return new AttendantDAO(conn);
        if (emp instanceof Librarian)     return new LibrarianDAO(conn);
        if (emp instanceof Administrator) return new AdministratorDAO(conn);
        throw new IllegalArgumentException("Tipo de funcionário desconhecido: " + emp.getClass().getSimpleName());
    }

    private EmployeeTypeDAO resolveDaoByCpf(String cpf, Connection conn) throws Exception {
        if (new AttendantDAO(conn).exists(cpf))     return new AttendantDAO(conn);
        if (new LibrarianDAO(conn).exists(cpf))     return new LibrarianDAO(conn);
        if (new AdministratorDAO(conn).exists(cpf)) return new AdministratorDAO(conn);
        return null;
    }

    // CREATE
    public void insert(Employee emp) {
        validateEmployee(emp);

        Connection conn = ConnectionDb.getConexao();
        try {
            conn.setAutoCommit(false);

            new PersonDAO(conn).insert(emp);
            new EmployeeDAO(conn).insert(emp);
            resolveDao(emp, conn).insert(emp);

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

            // Remove da tabela de tipo antiga (qualquer que seja)
            EmployeeTypeDAO oldDao = resolveDaoByCpf(emp.getCpf(), conn);
            if (oldDao != null) {
                oldDao.delete(emp.getCpf());
            }

            // Insere na tabela do novo tipo
            resolveDao(emp, conn).insert(emp);

            conn.commit();

        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            throw new RuntimeException("Erro ao atualizar funcionário.", e);
        }
    }

    // DELETE
    public void delete(String cpf) {
        if (cpf == null || cpf.trim().isEmpty())
            throw new IllegalArgumentException("CPF é obrigatório para deletar!");

        Connection conn = ConnectionDb.getConexao();
        try {
            conn.setAutoCommit(false);

            EmployeeTypeDAO typeDao = resolveDaoByCpf(cpf, conn);
            if (typeDao == null)
                throw new IllegalArgumentException("Funcionário não encontrado com CPF: " + cpf);

            typeDao.delete(cpf);
            new EmployeeDAO(conn).delete(cpf);
            new PersonDAO(conn).delete(cpf);

            conn.commit();

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            throw new RuntimeException("Erro ao remover funcionário.", e);
        }
    }

    // BUSCA POR CPF
    private String normalizeCpf(String cpf) {
        return (cpf == null) ? null : cpf.replaceAll("\\D+", "");
    }

    public Optional<Employee> findByCpf(String cpf) {
        cpf = normalizeCpf(cpf);
        if (cpf == null || cpf.isBlank())
            throw new IllegalArgumentException("CPF é obrigatório para busca!");

        Connection conn = ConnectionDb.getConexao();
        try {
            AttendantDAO aDao      = new AttendantDAO(conn);
            LibrarianDAO lDao      = new LibrarianDAO(conn);
            AdministratorDAO adDao = new AdministratorDAO(conn);

            if (aDao.exists(cpf))  return Optional.ofNullable(aDao.findByCpf(cpf));
            if (lDao.exists(cpf))  return Optional.ofNullable(lDao.findByCpf(cpf));
            if (adDao.exists(cpf)) return Optional.ofNullable(adDao.findByCpf(cpf));

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar funcionário por CPF.", e);
        }
    }

    // READ ALL por tipo
    public List<Attendant> getAllAttendants() {
        try { return new AttendantDAO(ConnectionDb.getConexao()).findAll(); }
        catch (Exception e) { throw new RuntimeException("Erro ao buscar atendentes.", e); }
    }

    public List<Librarian> getAllLibrarians() {
        try { return new LibrarianDAO(ConnectionDb.getConexao()).findAll(); }
        catch (Exception e) { throw new RuntimeException("Erro ao buscar bibliotecários.", e); }
    }

    public List<Administrator> getAllAdmins() {
        try { return new AdministratorDAO(ConnectionDb.getConexao()).findAll(); }
        catch (Exception e) { throw new RuntimeException("Erro ao buscar administradores.", e); }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.addAll(getAllAdmins());
        employees.addAll(getAllAttendants());
        employees.addAll(getAllLibrarians());
        return employees;
    }

    // VALIDAÇÃO CENTRALIZADA
    private void validateEmployee(Employee emp) {
        if (emp.getCpf() == null || emp.getCpf().trim().isEmpty())
            throw new IllegalArgumentException("CPF é obrigatório!");
        if (emp.getName() == null || emp.getName().trim().isEmpty())
            throw new IllegalArgumentException("Nome é obrigatório!");
        if (emp.getEmail() == null || emp.getEmail().trim().isEmpty())
            throw new IllegalArgumentException("Email é obrigatório!");
        if (emp.getGender() == '\0')
            throw new IllegalArgumentException("Gênero é obrigatório!");
        if (emp.getBirthDate() == null)
            throw new IllegalArgumentException("Data de nascimento é obrigatória!");
        if (emp.getPassword() == 0)
            throw new IllegalArgumentException("Senha é obrigatória!");
    }
}