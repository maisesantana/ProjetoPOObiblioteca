package br.com.atlas.dao;

import br.com.atlas.model.Employee;
import java.sql.SQLException;

public interface EmployeeTypeDAO {

    void insert(Employee emp) throws SQLException;
    void delete(String cpf) throws SQLException;
    boolean exists(String cpf) throws SQLException;
}