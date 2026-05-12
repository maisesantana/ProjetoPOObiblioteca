package br.com.atlas.webcontroller;
import br.com.atlas.model.Administrator;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Librarian;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Para gerenciar a sessão
import java.io.IOException;
import java.time.LocalDate;


@WebServlet("/manageEmployee")
public class AdministratorController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. VERIFICAÇÃO DE SEGURANÇA: O cara está logado como Admin?
        HttpSession session = request.getSession();
        // Supondo que no seu LoginController você guardou o objeto admin na sessão
        
        Object user = session.getAttribute("userLogged");

            if(user == null || !(user instanceof Administrator)){

                response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=unauthorized");

                return;
            }

            Administrator adminLogado = (Administrator) user;

        try {
            // 2. Captura os dados do NOVO FUNCIONÁRIO que o Admin quer cadastrar
            String cpf = request.getParameter("cpf");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String gender = request.getParameter("gender");
            String birthDateText = request.getParameter("birthDate");
            String passwordText = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String role = request.getParameter("role");

            // Validação básica de presença de dados
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

            if (!"masculino".equals(gender) && !"feminino".equals(gender) && !"outro".equals(gender)) {
                response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=invalid_gender");
                return;
            }

            if (!"bibliotecario".equals(role) && !"atendente".equals(role) && !"administrador".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=invalid_role");
                return;
            }

            char genderChar = gender.charAt(0); // pega o primeiro char de gender
            Employee newEmployee;
            if ("bibliotecario".equals(role)) {
                newEmployee = new Librarian(cpf, name, email, genderChar, birthDate, password);
            } else if ("atendente".equals(role)) {
                newEmployee = new Attendant(cpf, name, email, genderChar, birthDate, password);
            } else {
                newEmployee = new Administrator(cpf, name, email, genderChar, birthDate, password);
            }

            // 4. O Administrador (Service) executa a ação de registro
            // Usamos o objeto que já está na sessão, pois ele é o "executor" real
            adminLogado.register(newEmployee);

            response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?status=success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/view/admin/registerEmployee.jsp?msg=erro_ao_cadastrar_funcionario");
        }
    }
}