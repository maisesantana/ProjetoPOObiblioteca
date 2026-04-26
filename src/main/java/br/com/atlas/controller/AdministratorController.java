package br.com.atlas.controller;

import br.com.atlas.dao.AdministratorDAO;
import br.com.atlas.model.Administrator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/registerAdmin")
public class AdministratorController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        try {
            // Captura de dados da tela
            String cpf = request.getParameter("cpf");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String gender = request.getParameter("gender");
            LocalDate birthDate = LocalDate.parse(request.getParameter("birthDate"));
            int password = Integer.parseInt(request.getParameter("password"));

            // Instancia a sua Model exatamente como você definiu
            Administrator newAdmin = new Administrator(cpf, name, email, gender, birthDate, password);

            // Usa o DAO especializado
            AdministratorDAO dao = new AdministratorDAO();
            dao.insert(newAdmin);

            // Redirecionamento para a área de gestão de equipe
            response.sendRedirect("views/admin/team_management.jsp?status=success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("views/admin/error.jsp?msg=erro_cadastro_admin");
        }
    }
}