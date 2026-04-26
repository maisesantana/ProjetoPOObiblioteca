package br.com.atlas.controller;

import br.com.atlas.dao.LoanDAO;
import br.com.atlas.dao.ReturnBookDAO;
import br.com.atlas.model.Loan;
import br.com.atlas.model.ReturnBook;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/processReturn")
public class ReturnController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Pega o ID do empréstimo que está sendo encerrado
        int loanId = Integer.parseInt(request.getParameter("loanId"));

        // 2. Busca o empréstimo completo no banco
        LoanDAO loanDao = new LoanDAO();
        Loan loan = loanDao.findById(loanId);

        if (loan != null && loan.isActive()) {
            // 3. Cria o objeto de devolução com a data atual
            ReturnBook returnRecord = new ReturnBook(LocalDateTime.now(), loan);

            // 4. Salva no banco (O DAO vai cuidar de suspender o cliente se isLate() for true)
            ReturnBookDAO returnDao = new ReturnBookDAO();
            returnDao.insert(returnRecord);

            // 5. Redirecionamento com feedback
            if (returnRecord.isLate()) {
                response.sendRedirect("views/clerk/returnSuccess.jsp?status=late&days=" + returnRecord.calculateSuspensionDays());
            } else {
                response.sendRedirect("views/clerk/returnSuccess.jsp?status=ok");
            }
        } else {
            response.sendRedirect("views/clerk/error.jsp?msg=loan_not_active");
        }
    }
}