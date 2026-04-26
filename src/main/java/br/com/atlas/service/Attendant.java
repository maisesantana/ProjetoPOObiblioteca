package br.com.atlas.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.atlas.model.BookCopy;
import br.com.atlas.model.Client;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Loan;
import br.com.atlas.model.Manage;
import br.com.atlas.model.Person;
import br.com.atlas.model.Renewal;
import br.com.atlas.model.ReturnBook;

public class Attendant extends Employee {

    public Attendant(String cpf, String name, String email, String gender,
            LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate, password);
    }

    @Override
    public void register(Person c, Manage m) {
        if (c instanceof Client) { //Só insere se for instancia de cliente!!
            m.addClient((Client) c); //faz conversao de pessoa pra cliente
        }
    }

    @Override
    public void remove(Person c, Manage m) {
        if (c instanceof Client) { //Só remove se for instancia de cliente!!
            m.removeClient((Client) c); //faz conversao de pessoa pra cliente
        }
    }

    @Override
    public void update(Person c,  Manage m) {
        if (c instanceof Client) {
            for (Person rc : m.getPeople()) { //rc = real client, um cliente q ja existe
                //se o cpf do cliente q existe bater com o do novo objeto passado 
                //por parametro, ele vai editar a pessoa existente.
                if (rc.getCpf().equals(c.getCpf())) {
                
                    rc.setName(c.getName());
                    rc.setEmail(c.getEmail());
                    rc.setGender(c.getGender());
                    rc.setBirthDate(c.getBirthDate());

                    break; //sai do loop qnd encontra!
                }
            }
        }
    }

    public void registerLoan(Client c, BookCopy bc) {
        //ver se o cliente pode pegar emprestado
        if (!c.canBorrow()) {
            System.out.println("O cliente não pode pegar livro emprestado!");
            return;
        }
        //ver se exemplar ta disponivel
        if (!bc.isAvailable()) {
            System.out.println("Exemplar não disponível");
            return;
        }

        Loan l = new Loan(c, bc);
        c.addLoan(l);
        bc.setAvailable(false);
        System.out.println("Empréstimo feito.");
    }

    public void registerRenewal(Loan l) {
        if (l.canRenew()) {
            System.out.println("Máximo de renovações efetuadas. Não é posível renovar!");
            return;
        }
        
        if (LocalDateTime.now().isAfter(l.getExpectedReturnDate())) {
            System.out.println("Não é possível renovar, pois o empréstimo já passou da data de devolução.");
            return;
        }

        //aki ele renova a data
        l.setExpectedReturnDate(l.getExpectedReturnDate().plusDays(8));
        Renewal r = new Renewal(l.getExpectedReturnDate(), l.getRenewals().size() + 1, l);
        l.addRenew(r);
        System.out.println("Renovação concluída!");
    }

    public void registerReturnBook(Loan l) {
        if (!l.isActive()) {
            System.out.println("O empréstimo já foi concluido.");
            return;
        }

        l.setActive(false);
        ReturnBook r = new ReturnBook(LocalDateTime.now(), l);
        if (r.isLate()) {

            l.setReturnBook(r);
            if (l.getClient().isSuspended()) {
                l.getClient().setEndSuspensionDate(l.getClient().getStartSuspensionDate().plusDays(r.calculateSuspensionDays()));
                System.out.println("O cliente recebeu a devida suspensão.");
                return;
            }
            
            l.getClient().setStartSuspensionDate(LocalDate.now());
            l.getClient().setEndSuspensionDate(l.getClient().getStartSuspensionDate().plusDays(r.calculateSuspensionDays()));
            System.out.println("O cliente recebeu a devida suspensão.");
            return;
        }
        l.setReturnBook(r);
    }
}