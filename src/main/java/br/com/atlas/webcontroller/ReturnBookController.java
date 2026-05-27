package br.com.atlas.webcontroller;

import br.com.atlas.dao.ClientDAO;
import br.com.atlas.dao.LoanDAO;
import br.com.atlas.dao.ReturnBookDAO;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Client;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Loan;
import br.com.atlas.service.ReturnBookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class ReturnBookController extends HttpServlet {

    private final ReturnBookService returnBookService =
        new ReturnBookService(new LoanDAO(), new ReturnBookDAO());
    private final LoanDAO loanDAO     = new LoanDAO();
    private final ClientDAO clientDAO = new ClientDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("userLogged");

        if (!(user instanceof Attendant)) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=access_denied");
            return;
        }

        String cpf = request.getParameter("cpf");

        if (cpf != null && !cpf.isBlank()) {
            try {
                Client client = clientDAO.findByCpf(cpf.trim());

                if (client == null) {
                    request.setAttribute("errorMsg", "Cliente não encontrado com CPF: " + cpf);
                    request.getRequestDispatcher("/view/attendant/returnBook.jsp").forward(request, response);
                    return;
                }

                // Busca empréstimos ativos com nome do livro — tudo no DAO
                List<Loan> activeLoans = loanDAO.findActiveLoansByCpf(cpf.trim());

                request.setAttribute("client", client);
                request.setAttribute("loans", activeLoans);

                if (activeLoans.isEmpty()) {
                    request.setAttribute("errorMsg", "Nenhum empréstimo ativo encontrado para este cliente.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMsg", "Erro ao buscar empréstimos.");
            }
        }

        request.getRequestDispatcher("/view/attendant/returnBook.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("userLogged");

        if (!(user instanceof Attendant)) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=access_denied");
            return;
        }

        String loanIdParam = request.getParameter("loanId");
        String cpf         = request.getParameter("cpf");

        try {
            int loanId = Integer.parseInt(loanIdParam);
            returnBookService.registerReturn(loanId);
            response.sendRedirect(request.getContextPath()
                + "/returnBook?cpf=" + cpf + "&msg=return_success");

        } catch (IllegalArgumentException | IllegalStateException e) {
            response.sendRedirect(request.getContextPath()
                + "/returnBook?cpf=" + cpf + "&msg=error&detail="
                + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath()
                + "/returnBook?cpf=" + cpf + "&msg=error");
        }
    }
}