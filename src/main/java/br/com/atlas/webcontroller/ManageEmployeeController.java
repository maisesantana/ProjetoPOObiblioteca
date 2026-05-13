package br.com.atlas.webcontroller;

import br.com.atlas.model.*;
import br.com.atlas.service.EmployeeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

public class ManageEmployeeController extends HttpServlet {

    private final EmployeeService employeeService = new EmployeeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Segurança: apenas Administrador
        Administrator admin = getAdminOrRedirect(request, response);
        if (admin == null) return;

        String action = request.getParameter("action");

        // --- Busca por CPF (manageEmployee.jsp) ---
        if ("search".equals(action)) {
            String cpf = request.getParameter("cpf");

            if (cpf == null || cpf.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/view/admin/manageEmployee.jsp");
                return;
            }

            try {
                Employee found = employeeService.findByCpf(cpf.trim())
                        .orElseThrow(() -> new IllegalArgumentException("CPF não encontrado"));
                request.setAttribute("employeeFound", found);
                request.getRequestDispatcher("/view/admin/manageEmployee.jsp")
                       .forward(request, response);

            } catch (IllegalArgumentException e) {
                // CPF não encontrado
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/manageEmployee.jsp?msg=not_found");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/manageEmployee.jsp?msg=error");
            }

        // --- Carregar tela de edição (editEmployee.jsp) ---
        } else if ("edit".equals(action)) {
            String cpf = request.getParameter("cpf");

            if (cpf == null || cpf.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/view/admin/manageEmployee.jsp");
                return;
            }

            try {
                Employee found = employeeService.findByCpf(cpf.trim())
                        .orElseThrow(() -> new IllegalArgumentException("CPF não encontrado"));
                request.setAttribute("employeeFound", found);
                request.getRequestDispatcher("/view/admin/editEmployee.jsp")
                       .forward(request, response);

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/manageEmployee.jsp?msg=error");
            }

        } else {
            // Nenhuma action: apenas exibe a tela de busca limpa
            request.getRequestDispatcher("/view/admin/manageEmployee.jsp")
                   .forward(request, response);
        }
    }

    // POST: remover ou atualizar funcionário
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Administrator admin = getAdminOrRedirect(request, response);
        if (admin == null) return;

        String action = request.getParameter("action");

        // ── REMOVER 
        if ("remove".equals(action)) {
            String cpf = request.getParameter("cpf");

            if (cpf == null || cpf.isBlank()) {
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/manageEmployee.jsp?msg=error");
                return;
            }

            try {
                admin.remove(cpf.trim());
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/manageEmployee.jsp?msg=removed");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/manageEmployee.jsp?msg=error");
            }

        // ── ATUALIZAR ─
        } else if ("update".equals(action)) {
            String cpf           = request.getParameter("cpf");
            String name          = request.getParameter("name");
            String email         = request.getParameter("email");
            String gender        = request.getParameter("gender");
            String birthDateText = request.getParameter("birthDate");
            String passwordText  = request.getParameter("password");
            String confirmPw     = request.getParameter("confirmPassword");
            String role          = request.getParameter("role");

            // Validação de campos obrigatórios
            if (isBlank(cpf, name, email, gender, birthDateText, passwordText, confirmPw, role)) {
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/editEmployee.jsp?msg=empty_fields&cpf=" + cpf);
                return;
            }

            // Senhas coincidem?
            if (!passwordText.equals(confirmPw)) {
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/editEmployee.jsp?msg=password_mismatch&cpf=" + cpf);
                return;
            }

            // Parse de data
            LocalDate birthDate;
            try {
                birthDate = LocalDate.parse(birthDateText);
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/editEmployee.jsp?msg=empty_fields&cpf=" + cpf);
                return;
            }

            // Parse de senha
            int password;
            try {
                password = Integer.parseInt(passwordText);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/editEmployee.jsp?msg=empty_fields&cpf=" + cpf);
                return;
            }

            char genderChar = gender.charAt(0);

            // Monta o objeto Employee atualizado conforme o cargo
            Employee updated;
            if ("bibliotecario".equals(role)) {
                updated = new Librarian(cpf, name, email, genderChar, birthDate, password);
            } else {
                updated = new Attendant(cpf, name, email, genderChar, birthDate, password);
            }

            try {
                admin.update(updated);
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/editEmployee.jsp?msg=success&cpf=" + cpf);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/editEmployee.jsp?msg=error&cpf=" + cpf);
            }

        } else {
            response.sendRedirect(request.getContextPath() + "/view/admin/manageEmployee.jsp");
        }
    }


    /** Verifica autenticação; retorna o Admin ou null (já redirecionou). */
    private Administrator getAdminOrRedirect(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        HttpSession session = req.getSession(false);
        Object user = (session != null) ? session.getAttribute("userLogged") : null;

        if (user instanceof Administrator) {
            return (Administrator) user;
        }

        res.sendRedirect(req.getContextPath() + "/view/login.jsp?msg=unauthorized");
        return null;
    }

    /** Retorna true se qualquer String for nula ou em branco. */
    private boolean isBlank(String... values) {
        for (String v : values) {
            if (v == null || v.isBlank()) return true;
        }
        return false;
    }
}