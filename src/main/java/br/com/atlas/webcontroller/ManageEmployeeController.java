package br.com.atlas.webcontroller;

import br.com.atlas.model.*;
import br.com.atlas.service.EmployeeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@WebServlet("/manageEmployee")
public class ManageEmployeeController extends HttpServlet {

    private final EmployeeService employeeService = new EmployeeService();

    private String normalizeCpf(String cpf) {
        return (cpf == null) ? null : cpf.replaceAll("\\D+", "");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Administrator admin = getAdminOrRedirect(request, response);
        if (admin == null) return;

        String action = request.getParameter("action");

        if ("search".equals(action)) {
            String cpf = normalizeCpf(request.getParameter("cpf"));

            if (cpf == null || cpf.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/view/admin/manageEmployee.jsp");
                return;
            }

            try {
                Employee found = employeeService.findByCpf(cpf)
                        .orElseThrow(() -> new IllegalArgumentException("CPF não encontrado"));
                request.setAttribute("employeeFound", found);
                request.getRequestDispatcher("/view/admin/manageEmployee.jsp")
                       .forward(request, response);

            } catch (IllegalArgumentException e) {
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/manageEmployee.jsp?msg=not_found");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/manageEmployee.jsp?msg=error");
            }

        } else if ("edit".equals(action)) {
            forwardToEditPage(request, response, normalizeCpf(request.getParameter("cpf")));

        } else {
            request.getRequestDispatcher("/view/admin/manageEmployee.jsp")
                   .forward(request, response);
        }
    }

    private void forwardToEditPage(HttpServletRequest request, HttpServletResponse response, String cpf)
            throws ServletException, IOException {
        if (cpf == null || cpf.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/view/admin/manageEmployee.jsp");
            return;
        }

        try {
            Employee found = employeeService.findByCpf(cpf)
                    .orElseThrow(() -> new IllegalArgumentException("CPF não encontrado"));
            request.setAttribute("employeeFound", found);
            request.setAttribute("isLibrarian",    found instanceof Librarian);
            request.setAttribute("isAdministrator", found instanceof Administrator);
            request.getRequestDispatcher("/view/admin/editEmployee.jsp")
                   .forward(request, response);
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath()
                    + "/view/admin/manageEmployee.jsp?msg=not_found");
        } catch (Exception e) {
            e.printStackTrace();
            String debug = java.net.URLEncoder.encode(
                    e.getClass().getSimpleName() + ": " + e.getMessage(),
                    StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath()
                    + "/view/admin/manageEmployee.jsp?msg=error&debug=" + debug);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Administrator admin = getAdminOrRedirect(request, response);
        if (admin == null) return;

        String action = request.getParameter("action");

        if ("remove".equals(action)) {
            String cpf = normalizeCpf(request.getParameter("cpf"));

            if (cpf == null || cpf.isBlank()) {
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/manageEmployee.jsp?msg=error");
                return;
            }

            try {
                employeeService.delete(cpf.trim());
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/manageEmployee.jsp?msg=removed");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath()
                        + "/view/admin/manageEmployee.jsp?msg=error");
            }

        } else if ("edit".equals(action)) {
            forwardToEditPage(request, response, normalizeCpf(request.getParameter("cpf")));

        } else if ("update".equals(action)) {
            String cpf           = normalizeCpf(request.getParameter("cpf"));
            String name          = request.getParameter("name");
            String email         = request.getParameter("email");
            String gender        = request.getParameter("gender");
            String birthDateText = request.getParameter("birthDate");
            String passwordText  = request.getParameter("password");
            String confirmPw     = request.getParameter("confirmPassword");
            String role          = request.getParameter("role");

            if (isBlank(cpf, name, email, gender, birthDateText, role)) {
                response.sendRedirect(request.getContextPath()
                        + "/manageEmployee?action=edit&cpf=" + cpf + "&msg=empty_fields");
                return;
            }

            LocalDate birthDate;
            try {
                birthDate = LocalDate.parse(birthDateText);
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath()
                        + "/manageEmployee?action=edit&cpf=" + cpf + "&msg=empty_fields");
                return;
            }

            int password;
            if (isBlank(passwordText, confirmPw)) {
                try {
                    Employee existing = employeeService.findByCpf(cpf)
                            .orElseThrow(() -> new IllegalArgumentException("CPF não encontrado"));
                    password = existing.getPassword();
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect(request.getContextPath()
                            + "/manageEmployee?action=edit&cpf=" + cpf + "&msg=error");
                    return;
                }
            } else {
                if (!passwordText.equals(confirmPw)) {
                    response.sendRedirect(request.getContextPath()
                            + "/manageEmployee?action=edit&cpf=" + cpf + "&msg=password_mismatch");
                    return;
                }

                try {
                    int newPassword = Integer.parseInt(passwordText);
                    Employee existing = employeeService.findByCpf(cpf)
                            .orElseThrow(() -> new IllegalArgumentException("CPF não encontrado"));
                    if (existing.getPassword() == newPassword) {
                        response.sendRedirect(request.getContextPath()
                                + "/manageEmployee?action=edit&cpf=" + cpf + "&msg=password_same");
                        return;
                    }
                    password = newPassword;
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath()
                            + "/manageEmployee?action=edit&cpf=" + cpf + "&msg=empty_fields");
                    return;
                }
            }

            char genderChar = Character.toUpperCase(gender.charAt(0));

            Employee updated;
            if ("bibliotecario".equals(role)) {
                updated = new Librarian(cpf, name, email, genderChar, birthDate, password);
            } else if ("administrador".equals(role)) {
                updated = new Administrator(cpf, name, email, genderChar, birthDate, password);
            } else {
                updated = new Attendant(cpf, name, email, genderChar, birthDate, password);
            }

            try {
                employeeService.update(updated);
                response.sendRedirect(request.getContextPath()
                        + "/manageEmployee?action=edit&cpf=" + cpf + "&msg=success");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath()
                        + "/manageEmployee?action=edit&cpf=" + cpf + "&msg=error");
            }

        } else {
            response.sendRedirect(request.getContextPath() + "/view/admin/manageEmployee.jsp");
        }
    }

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

    private boolean isBlank(String... values) {
        for (String v : values) {
            if (v == null || v.isBlank()) return true;
        }
        return false;
    }
}