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

        if (!client.canBorrow()) {
            throw new IllegalStateException("Cliente está suspenso e não pode realizar empréstimos.");
        }

        if (!bookCopy.isAvailable()) {
            throw new IllegalStateException("Exemplar não está disponível para empréstimo.");
        }

        Loan loan = new Loan(client, bookCopy);
        client.addLoan(loan);

        loanDAO.insert(loan); // o LoanDAO já atualiza disponibilidade do exemplar na transação

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

    // LISTA EMPRÉSTIMOS ATIVOS FORMATADOS (usado em relatórios)
    public List<String> listActiveLoansInfo() {
        return loanDAO.listActiveLoansInfo();
    }

    // VERIFICA SE HÁ EMPRÉSTIMOS ATIVOS
    public boolean hasActiveLoans() {
        return loanDAO.hasActiveLoans();
    }
}