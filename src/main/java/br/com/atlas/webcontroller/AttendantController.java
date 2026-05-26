package br.com.atlas.webcontroller;

import java.io.IOException;
import java.time.LocalDate;

import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.dao.ClientDAO;
import br.com.atlas.dao.LoanDAO;
import br.com.atlas.dao.RenewalDAO;
import br.com.atlas.dao.ReturnBookDAO;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Client;
import br.com.atlas.model.Employee;
import br.com.atlas.service.ClientService;
import br.com.atlas.service.LoanService;
import br.com.atlas.service.RenewalService;
import br.com.atlas.service.ReturnBookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/attendant/action")
public class AttendantController extends HttpServlet {

    private final ClientService clientService         = new ClientService(new ClientDAO());
    private final LoanService loanService             = new LoanService(new LoanDAO(), new ClientDAO(), new BookCopyDAO());
    private final RenewalService renewalService       = new RenewalService(new LoanDAO(), new RenewalDAO());
    private final ReturnBookService returnBookService = new ReturnBookService(new LoanDAO(), new ReturnBookDAO());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("userLogged");

        if (!(user instanceof Attendant)) {
            response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=access_denied");
            return;
        }

        String action = request.getParameter("action");
        String base   = request.getContextPath() + "/view/attendant/";

        try {
            switch (action) {

                case "registerClient":
                    clientService.insert(mapClientFromRequest(request));
                    response.sendRedirect(base + "clients.jsp?msg=success");
                    break;

                case "updateClient":
                    clientService.update(mapClientFromRequest(request));
                    response.sendRedirect(base + "clients.jsp?msg=update_success");
                    break;

                case "removeClient":
                    clientService.delete(request.getParameter("cpf"));
                    response.sendRedirect(base + "clients.jsp?msg=remove_success");
                    break;

                case "loan":
                    String cpfLoan = request.getParameter("cpf");
                    int copyId = Integer.parseInt(request.getParameter("copyId"));
                    loanService.registerLoan(cpfLoan, copyId);
                    response.sendRedirect(base + "attendantPanel.jsp?msg=loan_ok");
                    break;

                case "return":
                    int loanIdReturn = Integer.parseInt(request.getParameter("loanId"));
                    returnBookService.registerReturn(loanIdReturn);
                    response.sendRedirect(base + "attendantPanel.jsp?msg=return_ok");
                    break;

                case "renewal":
                    int loanIdRenewal = Integer.parseInt(request.getParameter("loanId"));
                    renewalService.registerRenewal(loanIdRenewal);
                    response.sendRedirect(base + "attendantPanel.jsp?msg=renewal_ok");
                    break;

                default:
                    response.sendRedirect(base + "attendantPanel.jsp?msg=invalid_action");
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
    response.sendRedirect(base + "attendantPanel.jsp?msg=error&detail=" 
        + java.net.URLEncoder.encode(e.getMessage() != null ? e.getMessage() : "Erro", "UTF-8"));
} catch (Exception e) {
    e.printStackTrace();
    response.sendRedirect(base + "attendantPanel.jsp?msg=error&detail=" 
        + java.net.URLEncoder.encode(e.getMessage() != null ? e.getMessage() : "Erro", "UTF-8"));
}
    }

    private Client mapClientFromRequest(HttpServletRequest request) {
        return new Client(
            request.getParameter("cpf"),
            request.getParameter("name"),
            request.getParameter("email"),
            request.getParameter("gender").charAt(0),
            LocalDate.parse(request.getParameter("birthDate")),
            request.getParameter("address")
        );
    }
}