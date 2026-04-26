package br.com.atlas.controller;

import br.com.atlas.dao.EmployeeDAO;
import br.com.atlas.model.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/registerEmployee")
public class EmployeeController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        try {
            // Captura os dados do formulário (JSP)
            String cpf = request.getParameter("cpf");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String gender = request.getParameter("gender");
            LocalDate birthDate = LocalDate.parse(request.getParameter("birthDate"));
            int password = Integer.parseInt(request.getParameter("password"));

            // Instancia a Model exatamente como mandou
            Employee newEmp = new Employee(cpf, name, email, gender, birthDate, password);

            // Chama o DAO para salvar no banco
            EmployeeDAO dao = new EmployeeDAO();
            dao.insert(newEmp);

            // Redireciona para uma tela de sucesso
            response.sendRedirect("views/admin/success_employee.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            // Em caso de erro, volta para a página com uma mensagem
            response.sendRedirect("views/admin/error.jsp?msg=erro_cadastro");
        }
    }
}