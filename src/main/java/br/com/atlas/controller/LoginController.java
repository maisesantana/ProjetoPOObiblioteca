package br.com.atlas.controller;

import br.com.atlas.dao.LoginDAO;
import br.com.atlas.model.Administrator;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Librarian;
import br.com.atlas.util.ConnectionDb;
import br.com.atlas.view.LoginView;

import java.sql.Connection;
import java.util.Optional;

public class LoginController {

    private final LoginView view;

    public LoginController() {
        this.view = new LoginView();
    }

    public Employee login() {
        while (true) {
            String cpf = view.askCpf();
            if (cpf.isEmpty()) { view.showEmptyFields(); continue; }

            int password = view.askPassword();
            if (password == -1) { view.showInvalidPassword(); continue; }

            Connection conn = ConnectionDb.getConexao();
            if (conn == null) { view.showConnectionError(); continue; }

            LoginDAO loginDAO = new LoginDAO(conn);
            Optional<Employee> resultado = loginDAO.authenticate(cpf, password);

            if (resultado.isEmpty()) { view.showLoginError(); continue; }

            Employee funcionario = resultado.get();
            view.showWelcome(funcionario.getName(), resolveRole(funcionario));
            return funcionario;
        }
    }

    private String resolveRole(Employee emp) {
        if (emp instanceof Administrator) return "Administrador";
        if (emp instanceof Librarian)     return "Bibliotecário";
        if (emp instanceof Attendant)     return "Atendente";
        return "Funcionário";
    }
}