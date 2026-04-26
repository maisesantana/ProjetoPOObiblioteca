package br.com.atlas.controller;

import br.com.atlas.dao.LoanDAO;
import br.com.atlas.dao.ClientDAO;
import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.model.BookCopy;
import br.com.atlas.model.Client;
import br.com.atlas.model.Loan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/registerLoan")
public class LoanController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String cpf = request.getParameter("cpf");
        int bookCopyId = Integer.parseInt(request.getParameter("bookCopyId"));

        ClientDAO clientDao = new ClientDAO();
        BookCopyDAO bookCopyDao = new BookCopyDAO();
        
        try {
            Client client = clientDao.findByCpf(cpf);
            BookCopy bookCopy = bookCopyDao.findById(bookCopyId);

            if (client == null || bookCopy == null) {
                response.sendRedirect("views/clerk/error.jsp?msg=not_found");
                return;
            }

            if (!client.canBorrow()) {
                response.sendRedirect("views/clerk/error.jsp?msg=client_suspended");
                return;
            }

            if (!bookCopy.isAvailable()) {
                response.sendRedirect("views/clerk/error.jsp?msg=book_not_available");
                return;
            }

            // Usando o construtor do Model: Loan(Client client, BookCopy bookCopy)
            Loan newLoan = new Loan(client, bookCopy);
            newLoan.setActive(true);
            newLoan.setExpectedReturnDate(LocalDateTime.now().plusDays(8));

            LoanDAO loanDao = new LoanDAO();
            loanDao.insert(newLoan);

            response.sendRedirect("views/clerk/loanSuccess.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("views/clerk/error.jsp");
        }
    }
}