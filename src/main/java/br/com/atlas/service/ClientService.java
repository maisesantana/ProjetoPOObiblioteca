package br.com.atlas.service;

import br.com.atlas.dao.ClientDAO;
import br.com.atlas.dao.LoanDAO;
import br.com.atlas.model.Client;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ClientService {

    private final ClientDAO dao;
    private final LoanDAO loanDAO;

    public ClientService(ClientDAO dao) {
        this.dao = dao;
        this.loanDAO = new LoanDAO();
    }

    private void validateClient(Client client) {
        if (client.getCpf() == null || client.getCpf().trim().isEmpty())
            throw new IllegalArgumentException("CPF é obrigatório!");
        if (client.getName() == null || client.getName().trim().isEmpty())
            throw new IllegalArgumentException("Nome é obrigatório!");
        if (client.getEmail() == null || client.getEmail().trim().isEmpty())
            throw new IllegalArgumentException("Email é obrigatório!");
        if (client.getGender() == '\0')
            throw new IllegalArgumentException("Gênero é obrigatório!");
        if (client.getBirthDate() == null)
            throw new IllegalArgumentException("Data de nascimento é obrigatória!");
        if (client.getAddress() == null || client.getAddress().trim().isEmpty())
            throw new IllegalArgumentException("Endereço é obrigatório!");
    }

    public void insert(Client client) throws SQLException {
        validateClient(client);
        dao.insert(client);
    }

    public List<Client> findAll() throws SQLException {
        return dao.findAll();
    }

    public Optional<Client> findByCpf(String cpf) throws SQLException {
        if (cpf == null || cpf.trim().isEmpty())
            throw new IllegalArgumentException("CPF é obrigatório para busca!");
        return Optional.ofNullable(dao.findByCpf(cpf));
    }

    public List<Client> findByName(String name) throws SQLException {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Nome é obrigatório para busca!");
        return dao.findByName(name);
    }

    public void update(Client client) throws SQLException {
        validateClient(client);
        Optional<Client> existing = findByCpf(client.getCpf());
        if (existing.isEmpty())
            throw new IllegalArgumentException("Nenhum cliente encontrado com o CPF: " + client.getCpf());
        dao.update(client);
    }

    public void delete(String cpf) throws SQLException {
        if (cpf == null || cpf.trim().isEmpty())
            throw new IllegalArgumentException("CPF é obrigatório para deletar!");

        Optional<Client> existing = findByCpf(cpf);
        if (existing.isEmpty())
            throw new IllegalArgumentException("Nenhum cliente encontrado com o CPF: " + cpf);

        // RF: não pode remover cliente com empréstimo ativo
        int activeLoans = loanDAO.countActiveLoans(cpf);
        if (activeLoans > 0)
            throw new IllegalStateException(
                "Não é possível remover o cliente pois possui " + activeLoans + " empréstimo(s) ativo(s)."
            );
        dao.delete(cpf);
    }

    public boolean checkClientStatus(Client client) {
        if (client.isSuspended()) return false;
        return client.getLoans().stream()
            .filter(loan -> loan.getReturnBook() != null)
            .noneMatch(loan -> loan.getReturnBook().isLate());
    }
}