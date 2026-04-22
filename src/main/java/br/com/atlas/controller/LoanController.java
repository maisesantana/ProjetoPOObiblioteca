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

        // 1. Captura quem está pegando e qual exemplar
        String cpf = request.getParameter("cpf");
        int bookCopyId = Integer.parseInt(request.getParameter("bookCopyId"));

        // 2. Precisamos buscar o Cliente e o Exemplar REAIS no banco de dados
        ClientDAO clientDao = new ClientDAO();
        BookCopyDAO bookCopyDao = new BookCopyDAO();
        
        Client client = clientDao.findById(cpf);
        BookCopy bookCopy = bookCopyDao.findById(bookCopyId);

        // --- AQUI ESTÁ A VERIFICAÇÃO! ---
        
        // 3. Verificação 1: O cliente existe?
        if (client == null) {
            response.sendRedirect("views/clerk/error.jsp?msg=client_not_found");
            return; // Para o código aqui
        }

        // 4. Verificação 2: O cliente está suspenso? (Usa o método do Model Client)
        if (!client.canBorrow()) {
            // Se o canBorrow() retornar false, mandamos para o erro
            response.sendRedirect("views/clerk/error.jsp?msg=client_suspended");
            return; // Para o código aqui
        }

        // 5. Verificação 3: O exemplar está disponível?
        if (!bookCopy.isStatusAvailable()) {
            response.sendRedirect("views/clerk/error.jsp?msg=book_not_available");
            return; // Para o código aqui
        }
        
        // --- SE PASSOU POR TUDO ACIMA, A GENTE CRIA O EMPRÉSTIMO ---

        // 3. Gerar as datas de empréstimo e devolução (Hoje e +8 dias)
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime expectedReturnDate = today.plusDays(8);

        // 4. Montar o objeto Empréstimo (Loan)
        Loan newLoan = new Loan();
        newLoan.setClient(client);
        newLoan.setBookCopy(bookCopy);
        newLoan.setLoanDate(today);
        newLoan.setExpectedReturnDate(expectedReturnDate);
        newLoan.setRenewals(0);
        newLoan.setActive(true);

        // 5. Salvar no banco (O LoanDAO já cuida de mudar o status do livro para indisponível)
        LoanDAO loanDao = new LoanDAO();
        loanDao.insert(newLoan);

        /*
         * 6. Redirecionamento
         * Apontando para a pasta 'clerk' (Atendente) e página de sucesso
         */
        response.sendRedirect("views/clerk/loanSuccess.jsp");
    }
}