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
        Administrator adminLogado = (Administrator) session.getAttribute("userLogged");

        if (adminLogado == null) {
            // Se não tem ninguém na sessão, manda pro login
            response.sendRedirect("login.jsp?msg=unauthorized");
            return; 
        }

        try {
            // 2. Captura os dados do NOVO FUNCIONÁRIO que o Admin quer cadastrar
            String cpf = request.getParameter("cpf");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String gender = request.getParameter("gender");
            LocalDate birthDate = LocalDate.parse(request.getParameter("birthDate"));
            int password = Integer.parseInt(request.getParameter("password"));

            // 3. Cria o Employee (O novo Atendente ou Bibliotecário)
            String role = request.getParameter("role");
            Employee newEmployee;
            if ("bibliotecario".equals(role)) {
                newEmployee = new Librarian(cpf, name, email, gender, birthDate, password);
            } else {
                newEmployee = new Attendant(cpf, name, email, gender, birthDate, password);
            }

            // 4. O Administrador (Service) executa a ação de registro
            // Usamos o objeto que já está na sessão, pois ele é o "executor" real
            adminLogado.register(newEmployee);

            response.sendRedirect("views/admin/team_management.jsp?status=success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("views/admin/error.jsp?msg=erro_ao_cadastrar_funcionario");
        }
    }
}