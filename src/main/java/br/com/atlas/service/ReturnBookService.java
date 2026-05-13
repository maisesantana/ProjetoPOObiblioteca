package br.com.atlas.service;

import br.com.atlas.dao.LoanDAO;
import br.com.atlas.dao.ReturnBookDAO;
import br.com.atlas.model.Loan;
import br.com.atlas.model.ReturnBook;

import java.time.LocalDateTime;
import java.util.Optional;

public class ReturnBookService {

    private final LoanDAO loanDAO;
    private final ReturnBookDAO returnBookDAO;

    public ReturnBookService(LoanDAO loanDAO, ReturnBookDAO returnBookDAO) {
        this.loanDAO = loanDAO;
        this.returnBookDAO = returnBookDAO;
    }

    // REGISTRA DEVOLUÇÃO
    public ReturnBook registerReturn(int loanId) {

        Loan loan = loanDAO.findById(loanId);

        if (loan == null) {
            throw new IllegalArgumentException("Empréstimo não encontrado com ID: " + loanId);
        }

        if (!loan.isActive()) {
            throw new IllegalStateException("Este empréstimo já foi finalizado.");
        }

        ReturnBook returnBook = new ReturnBook(LocalDateTime.now(), loan);
        loan.setReturnBook(returnBook);

        // O ReturnBookDAO executa a transação completa:
        // insert Return + update Loan + update BookCopy + update Client (suspensão)
        returnBookDAO.insert(returnBook);

        return returnBook;
    }

    // BUSCA DEVOLUÇÃO DE UM EMPRÉSTIMO
    public Optional<ReturnBook> findByLoanId(int loanId) {
        Loan loan = loanDAO.findById(loanId);
        if (loan == null) return Optional.empty();
        return Optional.ofNullable(loan.getReturnBook());
    }
}