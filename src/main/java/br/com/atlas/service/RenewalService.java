package br.com.atlas.service;

import br.com.atlas.dao.LoanDAO;
import br.com.atlas.dao.RenewalDAO;
import br.com.atlas.model.Loan;
import br.com.atlas.model.Renewal;

import java.time.LocalDateTime;

public class RenewalService {

    private final LoanDAO loanDAO;
    private final RenewalDAO renewalDAO;

    public RenewalService(LoanDAO loanDAO, RenewalDAO renewalDAO) {
        this.loanDAO = loanDAO;
        this.renewalDAO = renewalDAO;
    }

    // REGISTRA RENOVAÇÃO
    public Renewal registerRenewal(int loanId) {

        Loan loan = loanDAO.findById(loanId);

        if (loan == null) {
            throw new IllegalArgumentException("Empréstimo não encontrado com ID: " + loanId);
        }

        if (!loan.isActive()) {
            throw new IllegalStateException("Este empréstimo já foi finalizado.");
        }

        if (!loan.canRenew()) {
            throw new IllegalStateException("Limite de renovações atingido.");
        }

        if (LocalDateTime.now().isAfter(loan.getExpectedReturnDate())) {
            throw new IllegalStateException("Não é possível renovar um empréstimo atrasado.");
        }

        // Atualiza a data de devolução esperada
        loan.setExpectedReturnDate(loan.getExpectedReturnDate().plusDays(8));

        Renewal renewal = new Renewal(
            loan.getExpectedReturnDate(),
            loan.getRenewalCount() + 1,
            loan
        );

        loan.addRenew(renewal);

        renewalDAO.insert(renewal);
        loanDAO.update(loan);

        return renewal;
    }
}