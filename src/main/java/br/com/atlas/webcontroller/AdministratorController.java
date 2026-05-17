package br.com.atlas.webcontroller;
import br.com.atlas.model.Administrator;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Librarian;
import br.com.atlas.service.EmployeeService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
//import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "AdministratorController", urlPatterns = {"/registerEmployee", "/listEmployees"})
public class AdministratorController extends HttpServlet {

    private final EmployeeService employeeService = new EmployeeService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");

        if (user == null || !(user instanceof Administrator)) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=unauthorized");
            return;
        }

        try {

            String cpf = request.getParameter("cpf");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String gender = request.getParameter("gender");
            String birthDateText = request.getParameter("birthDate");
            String passwordText = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String role = request.getParameter("role");

            if (cpf == null || cpf.isBlank()
                    || name == null || name.isBlank()
                    || email == null || email.isBlank()
                    || gender == null || gender.isBlank()
                    || birthDateText == null || birthDateText.isBlank()
                    || passwordText == null || passwordText.isBlank()
                    || confirmPassword == null || confirmPassword.isBlank()
                    || role == null || role.isBlank()) {

                response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=empty_fields");
                return;
            }

            if (!passwordText.equals(confirmPassword)) {
                response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=password_mismatch");
                return;
            }

            LocalDate birthDate;

            try {
                birthDate = LocalDate.parse(birthDateText);

            } catch (Exception ex) {

                response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=birthdate_format_error");
                return;
            }

            int password;

            try {
                password = Integer.parseInt(passwordText);

            } catch (NumberFormatException ex) {

                response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=password_format_error");
                return;
            }

            String genderLower = gender.toLowerCase();

            if (!"masculino".equals(genderLower)
                    && !"feminino".equals(genderLower)
                    && !"outro".equals(genderLower)) {

                response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=invalid_gender");
                return;
            }

            if (!"bibliotecario".equals(role)
                    && !"atendente".equals(role)
                    && !"administrador".equals(role)) {

                response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=invalid_role");
                return;
            }

            char genderChar = Character.toUpperCase(genderLower.charAt(0));

            Employee newEmployee;

            if ("bibliotecario".equals(role)) {

                newEmployee = new Librarian(
                        cpf,
                        name,
                        email,
                        genderChar,
                        birthDate,
                        password
                );

            } else if ("atendente".equals(role)) {

                newEmployee = new Attendant(
                        cpf,
                        name,
                        email,
                        genderChar,
                        birthDate,
                        password
                );

            } else {

                newEmployee = new Administrator(
                        cpf,
                        name,
                        email,
                        genderChar,
                        birthDate,
                        password
                );
            }

            employeeService.insert(newEmployee);

            response.sendRedirect(
                    request.getContextPath()
                    + "/view/admin/registerEmployee.jsp?status=success"
            );

        } catch (IllegalArgumentException e) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/view/admin/registerEmployee.jsp?msg="
                    + e.getMessage()
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                    + "/view/admin/registerEmployee.jsp?msg=erro_ao_cadastrar_funcionario"
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");

        if (user == null || !(user instanceof Administrator)) {

            response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            return;
        }

        try {
            String cpf = request.getParameter("cpf");

            if (cpf != null && !cpf.isBlank()) {
                Optional<Employee> employeeOpt = employeeService.findByCpf(cpf.trim());

                if (employeeOpt.isEmpty()) {
                    response.sendRedirect(
                            request.getContextPath()
                            + "/listEmployees?msg=not_found"
                    );
                    return;
                }

                request.setAttribute("employee", employeeOpt.get());
                request.getRequestDispatcher("/view/admin/employeeProfile.jsp")
                        .forward(request, response);
                return;
            }

            List<Employee> employees = employeeService.getAllEmployees();
            request.setAttribute("employees", employees);
            request.getRequestDispatcher("/view/admin/listEmployees.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                    + "/view/admin/adminPanel.jsp"
            );
        }
    }
}