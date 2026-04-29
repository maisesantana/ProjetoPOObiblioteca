package br.com.atlas.controller;

import br.com.atlas.dao.LoginDAO;
import br.com.atlas.model.Administrator;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Librarian;
import br.com.atlas.util.ConnectionDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Captura os dados vindos do formulário (JSP)
        String cpf = request.getParameter("cpf");
        String senhaTexto = request.getParameter("password");

        // Validação básica de campos vazios
        if (cpf == null || cpf.trim().isEmpty() || senhaTexto == null || senhaTexto.trim().isEmpty()) {
            response.sendRedirect("login.jsp?msg=empty_fields");
            return;
        }

        try (Connection conn = ConnectionDb.getConexao()) {
            // 2. Converte a senha para int (conforme o seu banco)
            int password = Integer.parseInt(senhaTexto);

            // 3. Chama o LoginDAO para fazer a busca em cascata
            LoginDAO loginDAO = new LoginDAO(conn);
            Optional<Employee> authUser = loginDAO.authenticate(cpf, password);

            if (authUser.isPresent()) {
                // LOGIN COM SUCESSO!
                Employee funcionario = authUser.get();

                // 4. Cria ou recupera a sessão e guarda o objeto logado
                HttpSession session = request.getSession();
                session.setAttribute("userLogged", funcionario);

                // 5. Redirecionamento baseado no cargo (Polimorfismo em ação!)
                // O Java descobre o tipo real do objeto aqui
                if (funcionario instanceof Administrator) {
                    response.sendRedirect("views/admin/dashboard.jsp");
                    
                } else if (funcionario instanceof Librarian) {
                    response.sendRedirect("views/librarian/inventory.jsp");
                    
                } else {
                    // Se não for nenhum dos dois, por exclusão é Atendente
                    response.sendRedirect("views/attendant/loan_panel.jsp");
                }

            } else {
                // Senha errada ou CPF não encontrado em nenhuma tabela
                response.sendRedirect("login.jsp?msg=invalid_credentials");
            }

        } catch (NumberFormatException e) {
            // Caso o usuário digite letras onde deveria ser senha numérica
            response.sendRedirect("login.jsp?msg=password_must_be_number");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?msg=internal_error");
        }
    }

    /**
     * Opcional: Método para Logout
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate(); // Mata a sessão (destrói o crachá)
        response.sendRedirect("login.jsp?msg=logged_out");
    }
}