package br.com.atlas.webcontroller;

import java.io.IOException;
import java.util.List;

import br.com.atlas.dao.LoanDAO;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Loan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/activeLoans")
public class ActiveLoansController extends HttpServlet {

    private final LoanDAO loanDAO = new LoanDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");

        if (user == null || !(user instanceof Attendant)) {
            response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
            return;
        }

        List<Loan> loans = loanDAO.findAllActive();

        request.setAttribute("loans", loans);
        request.getRequestDispatcher("/view/attendant/activeLoans.jsp")
                .forward(request, response);
    }
}
