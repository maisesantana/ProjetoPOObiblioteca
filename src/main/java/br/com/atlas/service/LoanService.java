package br.com.atlas.service;

import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.dao.ClientDAO;
import br.com.atlas.dao.LoanDAO;
import br.com.atlas.model.BookCopy;
import br.com.atlas.model.Client;
import br.com.atlas.model.Loan;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LoanService {

    private static final int MAX_LOANS = 6; // RF19: máximo 6 empréstimos simultâneos

    private final LoanDAO loanDAO;
    private final ClientDAO clientDAO;
    private final BookCopyDAO bookCopyDAO;

    public LoanService(LoanDAO loanDAO, ClientDAO clientDAO, BookCopyDAO bookCopyDAO) {
        this.loanDAO = loanDAO;
        this.clientDAO = clientDAO;
        this.bookCopyDAO = bookCopyDAO;
    }

    // REGISTRA NOVO EMPRÉSTIMO
    public Loan registerLoan(String cpf, int copyId) throws SQLException {

        Client client = clientDAO.findByCpf(cpf);
        if (client == null) {
            throw new IllegalArgumentException("Cliente não encontrado com CPF: " + cpf);
        }

        BookCopy bookCopy = bookCopyDAO.findById(copyId);
        if (bookCopy == null) {
            throw new IllegalArgumentException("Exemplar não encontrado com ID: " + copyId);
        }

        // RF27: cliente suspenso não pode pegar emprestado
        if (!client.canBorrow()) {
            throw new IllegalStateException("Cliente está suspenso e não pode realizar empréstimos.");
        }

        // RF19: limite de 6 empréstimos simultâneos
        int activeLoans = loanDAO.countActiveLoans(cpf);
        if (activeLoans >= MAX_LOANS) {
            throw new IllegalStateException(
                "Cliente já possui " + MAX_LOANS + " empréstimos ativos. Devolva um livro antes de realizar novo empréstimo."
            );
        }

        if (!bookCopy.isAvailable()) {
            throw new IllegalStateException("Exemplar não está disponível para empréstimo.");
        }

        Loan loan = new Loan(client, bookCopy);
        client.addLoan(loan);

        loanDAO.insert(loan);

        return loan;
    }

    // READ BY ID
    public Optional<Loan> findById(int loanId) {
        return Optional.ofNullable(loanDAO.findById(loanId));
    }

    // READ ALL
    public List<Loan> findAll() {
        return loanDAO.findAll();
    }

    // LISTA EMPRÉSTIMOS ATIVOS FORMATADOS
    public List<String> listActiveLoansInfo() {
        return loanDAO.listActiveLoansInfo();
    }

    // VERIFICA SE HÁ EMPRÉSTIMOS ATIVOS
    public boolean hasActiveLoans() {
        return loanDAO.hasActiveLoans();
    }
}