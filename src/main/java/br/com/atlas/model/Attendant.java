package br.com.atlas.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Connection;
import br.com.atlas.dao.BookDAO;
import br.com.atlas.util.ConnectionDb;
import br.com.atlas.model.Book;
import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.dao.ClientDAO;
import br.com.atlas.dao.LoanDAO;
import br.com.atlas.dao.ReturnBookDAO;
import br.com.atlas.dao.RenewalDAO; // import necessário

public class Attendant extends Employee {

    public Attendant(String cpf, String name, String email, String gender,
            LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate, password);
    }

    @Override
    public void register(Person c) {

        if (!(c instanceof Client)) {
            throw new IllegalArgumentException("Somente clientes podem ser cadastrados!");
        }

        try {
            ClientDAO client = new ClientDAO();
            client.insert((Client)c);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar cliente", e);
        }
    }

    @Override
    public void remove(String cpf) {
        try {
            ClientDAO clientDao = new ClientDAO();
            clientDao.delete(cpf);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover cliente", e);
        }
    }

    @Override
    public void update(Person c) {
        if (!(c instanceof Client)) {
            throw new IllegalArgumentException("Somente clientes podem ser atualizados!");
        }

        try {
            ClientDAO clientDao = new ClientDAO();
            clientDao.update((Client)c);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
        }
    }

    public void registerLoan(String cpf, int copyId) {
        try {
            ClientDAO clientDao = new ClientDAO();
            BookCopyDAO copyDao = new BookCopyDAO();
            LoanDAO loanDao = new LoanDAO();

            Client c = clientDao.findByCpf(cpf);
            BookCopy bc = copyDao.findById(copyId);

            if (c == null) {
                throw new RuntimeException("Cliente não encontrado");
            }

            if (bc == null) {
                throw new RuntimeException("Exemplar não encontrado");
            }

            if (!c.canBorrow()) {
                throw new RuntimeException("Cliente não pode pegar livro emprestado");
            }

            if (!bc.isAvailable()) {
                throw new RuntimeException("Exemplar não disponível");
            }

            Loan l = new Loan(c, bc);

            c.addLoan(l);
            bc.setAvailable(false);

            loanDao.insert(l);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar empréstimo", e);
        }
    }

    public void registerRenewal(int loanId) {

        // instanciar os DAOs
        LoanDAO loanDAO = new LoanDAO();
        RenewalDAO renewalDAO = new RenewalDAO();

        Loan l = loanDAO.findById(loanId);

        if (l == null) {
            throw new RuntimeException("Empréstimo não encontrado");
        }

        if (!l.canRenew()) {
            throw new RuntimeException("Limite de renovações atingido");
        }

        if (LocalDateTime.now().isAfter(l.getExpectedReturnDate())) {
            throw new RuntimeException("Não é possível renovar um empréstimo atrasado");
        }

        // atualiza data
        l.setExpectedReturnDate(l.getExpectedReturnDate().plusDays(8));

        Renewal r = new Renewal(
            l.getExpectedReturnDate(),
            l.getRenewalCount() + 1, // usa contador do banco
            l
        );

        l.addRenew(r);

        // salva no banco
        renewalDAO.insert(r);
        loanDAO.update(l);
    }

    public void registerReturn(int loanId) {

        try {
            LoanDAO loanDAO = new LoanDAO();
            ReturnBookDAO returnDAO = new ReturnBookDAO();
            BookCopyDAO copyDAO = new BookCopyDAO();
            ClientDAO clientDAO = new ClientDAO();

            Loan l = loanDAO.findById(loanId);

            if (l == null) {
                throw new RuntimeException("Empréstimo não encontrado");
            }

            if (!l.isActive()) {
                throw new RuntimeException("Empréstimo já finalizado");
            }

            l.setActive(false);

            ReturnBook r = new ReturnBook(LocalDateTime.now(), l);
            l.setReturnBook(r);

            // SALVA DEVOLUÇÃO
            returnDAO.insert(r);

            if (r.isLate()) {
                int dias = r.calculateSuspensionDays();
                Client c = l.getClient();
                if (c.isSuspended()) {
                    c.setEndSuspensionDate(c.getEndSuspensionDate().plusDays(dias));
                } else {
                    c.setStartSuspensionDate(LocalDate.now());
                    c.setEndSuspensionDate(c.getStartSuspensionDate().plusDays(dias));
                }

                // salva atualização do cliente
                clientDAO.update(c);
            }

            // libera exemplar
            l.getBookCopy().setAvailable(true);
            copyDAO.update(l.getBookCopy());

            // salva atualização do empréstimo
            loanDAO.update(l);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar devolução", e);
        }
    }

    // Adicione isso tanto no Librarian quanto no Attendant
    public List<Book> searchBooks(String name) {
        try (Connection conn = ConnectionDb.getConexao()) {
            BookDAO dao = new BookDAO(conn);
            return dao.findByName(name);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar livros", e);
        }
    }
}