package br.com.atlas.webcontroller;

import br.com.atlas.model.Administrator;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Librarian;
import br.com.atlas.service.EmployeeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Polimorfismo: implementa Gerenciavel operando sobre a entidade Funcionario.
 * cadastrar() → cadastra Funcionario (Atendente, Bibliotecario ou Administrador)
 * atualizar() → atualiza Funcionario
 * remover()   → remove Funcionario
 */
public class AdministratorController extends HttpServlet implements Gerenciavel {

    private final EmployeeService employeeService = new EmployeeService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");

        if (user == null || !(user instanceof Administrator)) {
            response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
            return;
        }

        String action = request.getParameter("action");

        if ("update".equals(action)) {
            atualizar(request, response);
        } else if ("delete".equals(action)) {
            remover(request, response);
        } else {
            cadastrar(request, response);
        }
    }

    // POLIMORFISMO: cadastrar() operando sobre Funcionario
    @Override
    public void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cpf           = request.getParameter("cpf");
            String name          = request.getParameter("name");
            String email         = request.getParameter("email");
            String gender        = request.getParameter("gender");
            String birthDateText = request.getParameter("birthDate");
            String passwordText  = request.getParameter("password");
            String confirmPass   = request.getParameter("confirmPassword");
            String role          = request.getParameter("role");

            if (cpf == null || cpf.isBlank() || name == null || name.isBlank()
                    || email == null || email.isBlank() || gender == null || gender.isBlank()
                    || birthDateText == null || birthDateText.isBlank()
                    || passwordText == null || passwordText.isBlank()
                    || confirmPass == null || confirmPass.isBlank()
                    || role == null || role.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=empty_fields");
                return;
            }

            if (!passwordText.equals(confirmPass)) {
                response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=password_mismatch");
                return;
            }

            LocalDate birthDate = LocalDate.parse(birthDateText);
            int password = Integer.parseInt(passwordText);
            char genderChar = Character.toUpperCase(gender.toLowerCase().charAt(0));

            Employee newEmployee;
            if ("bibliotecario".equals(role)) {
                newEmployee = new Librarian(cpf, name, email, genderChar, birthDate, password);
            } else if ("atendente".equals(role)) {
                newEmployee = new Attendant(cpf, name, email, genderChar, birthDate, password);
            } else {
                newEmployee = new Administrator(cpf, name, email, genderChar, birthDate, password);
            }

            employeeService.insert(newEmployee);
            response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?status=success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=erro_ao_cadastrar_funcionario");
        }
    }

    // POLIMORFISMO: atualizar() operando sobre Funcionario
    @Override
    public void atualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cpf           = request.getParameter("cpf");
            String name          = request.getParameter("name");
            String email         = request.getParameter("email");
            String gender        = request.getParameter("gender");
            String birthDateText = request.getParameter("birthDate");
            String passwordText  = request.getParameter("password");
            String role          = request.getParameter("role");

            LocalDate birthDate = LocalDate.parse(birthDateText);
            int password = Integer.parseInt(passwordText);
            char genderChar = Character.toUpperCase(gender.toLowerCase().charAt(0));

            Employee updated;
            if ("bibliotecario".equals(role)) {
                updated = new Librarian(cpf, name, email, genderChar, birthDate, password);
            } else if ("atendente".equals(role)) {
                updated = new Attendant(cpf, name, email, genderChar, birthDate, password);
            } else {
                updated = new Administrator(cpf, name, email, genderChar, birthDate, password);
            }

            employeeService.update(updated);
            response.sendRedirect(request.getContextPath() + "/listEmployees?msg=update_success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/listEmployees?msg=error");
        }
    }

    // POLIMORFISMO: remover() operando sobre Funcionario
    @Override
    public void remover(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cpf = request.getParameter("cpf");
            employeeService.delete(cpf);
            response.sendRedirect(request.getContextPath() + "/listEmployees?msg=delete_success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/listEmployees?msg=error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");

        if (user == null || !(user instanceof Administrator)) {
            response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
            return;
        }

        try {
            String cpf = request.getParameter("cpf");

            if (cpf != null && !cpf.isBlank()) {
                Optional<Employee> employeeOpt = employeeService.findByCpf(cpf.trim());
                if (employeeOpt.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/listEmployees?msg=not_found");
                    return;
                }
                request.setAttribute("employee", employeeOpt.get());
                request.getRequestDispatcher("/view/admin/employeeProfile.jsp").forward(request, response);
                return;
            }

            List<Employee> employees = employeeService.getAllEmployees();
            request.setAttribute("employees", employees);
            request.getRequestDispatcher("/view/admin/listEmployees.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/view/admin/adminPanel.jsp");
        }
    }
}