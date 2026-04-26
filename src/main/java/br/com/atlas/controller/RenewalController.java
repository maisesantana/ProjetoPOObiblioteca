package br.com.atlas.controller;

import br.com.atlas.dao.LoanDAO;
import br.com.atlas.dao.RenewalDAO;
import br.com.atlas.model.Loan;
import br.com.atlas.model.Renewal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/renewLoan")
public class RenewalController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Pega o ID do empréstimo que o usuário quer renovar
        int loanId = Integer.parseInt(request.getParameter("loanId"));

        LoanDAO loanDao = new LoanDAO();
        Loan loan = loanDao.findById(loanId);

        // 2. Verifica se o empréstimo existe e se PODE renovar (Regra do Model!)
        if (loan != null && loan.canRenew()) {
            
            // 3. Calcula a nova data (Geralmente +7 dias a partir da data atual de devolução)
            LocalDateTime newDate = loan.getExpectedReturnDate().plusDays(7);
            
            // 4. Cria o objeto de Renovação
            // O número da renovação é o tamanho atual da lista + 1
            int nextNumber = loan.getRenewals().size() + 1;
            Renewal renewal = new Renewal(newDate, nextNumber, loan);

            // 5. Salva no banco
            RenewalDAO renewalDao = new RenewalDAO();
            renewalDao.insert(renewal);

            response.sendRedirect("views/clerk/loanDetail.jsp?msg=renewed_ok");
        } else {
            // Se já renovou 3 vezes ou o empréstimo está inativo
            response.sendRedirect("views/clerk/loanDetail.jsp?msg=cannot_renew");
        }
    }
}