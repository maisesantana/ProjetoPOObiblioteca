package br.com.atlas.webcontroller;

import br.com.atlas.model.Attendant;
import br.com.atlas.model.Client;
import br.com.atlas.model.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/attendant/action")
public class AttendantController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("userLogged");

        // SEGURANÇA: Verifica se quem está logado é um Atendente
        if (!(user instanceof Attendant)) {
            response.sendRedirect("../login.jsp?msg=access_denied");
            return;
        }

        Attendant attendant = (Attendant) user;
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "registerClient":
                    attendant.register(mapClientFromRequest(request));
                    response.sendRedirect("clients.jsp?msg=success");
                    break;

                case "updateClient":
                    // @Override atualizar(c: Cliente) em ação
                    attendant.update(mapClientFromRequest(request));
                    response.sendRedirect("clients.jsp?msg=update_success");
                    break;

                case "removeClient":
                    // @Override remover(cpf: String) em ação
                    String cpfToRemove = request.getParameter("cpf");
                    attendant.remove(cpfToRemove);
                    response.sendRedirect("clients.jsp?msg=remove_success");
                    break;

                case "loan":
                    String cpfLoan = request.getParameter("cpf");
                    int copyId = Integer.parseInt(request.getParameter("copyId"));
                    attendant.registerLoan(cpfLoan, copyId);
                    response.sendRedirect("loan_panel.jsp?msg=loan_ok");
                    break;

                case "return":
                    int loanIdReturn = Integer.parseInt(request.getParameter("loanId"));
                    attendant.registerReturn(loanIdReturn);
                    response.sendRedirect("loan_panel.jsp?msg=return_ok");
                    break;

                case "renewal":
                    int loanIdRenewal = Integer.parseInt(request.getParameter("loanId"));
                    attendant.registerRenewal(loanIdRenewal);
                    response.sendRedirect("loan_panel.jsp?msg=renewal_ok");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("loan_panel.jsp?msg=error&detail=" + e.getMessage());
        }
    }

    /**
     * Método auxiliar para evitar repetição de código ao ler dados do Cliente
     */
    private Client mapClientFromRequest(HttpServletRequest request) {
        return new Client(
                request.getParameter("cpf"),
                request.getParameter("name"),
                request.getParameter("email"),
                request.getParameter("gender"),
                LocalDate.parse(request.getParameter("birthDate")),
                request.getParameter("address")
        );
    }
}