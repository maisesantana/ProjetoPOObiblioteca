package br.com.atlas.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.atlas.model.Book;
import br.com.atlas.model.BookCopy;
import br.com.atlas.model.Collection;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Loan;
import br.com.atlas.model.Manage;
import br.com.atlas.model.Person;
import br.com.atlas.model.Report;
import br.com.atlas.model.Client;

public class Librarian extends Employee {

    public Librarian(String cpf, String name, String email, String gender,
                    LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate, password);
    }

    public void registerBook(Book b, Collection c) {
        c.addBook(b);
    }

    public void removeBook(Book b, Collection c) {
        c.removeBook(b);
    }

    public void updateBook(Book b, Collection c) {

        for (Book rb : c.getBooks()) {

            if (rb.getBookId() == b.getBookId()) {

                rb.setBookName(b.getBookName());
                rb.setBookLocation(b.getBookLocation());
                rb.setNumberOfPages(b.getNumberOfPages());
                rb.setBookSubject(b.getBookSubject());
                rb.setPublisher(b.getPublisher());

                // autores
                rb.getAuthors().clear();
                rb.getAuthors().addAll(b.getAuthors());

                // categorias
                rb.getCategories().clear();
                rb.getCategories().addAll(b.getCategories());

                break;
            }
        }
    }

    public void registerCopy(BookCopy bc, Book b) {
        b.addCopy(bc);
    }

        /*ATENÇÃO! POR TER DEIXADO EMPLOYEE COMO ABSTRATA, 
    FOI NECESSARIO, PRA FAZER O POLIMORFISMO, QUE TODAS 
    AS CLASSES IMPLEMENTASSEM OS SEGUINTES MÉTODOS. PORÉM,
    BIBLIOTECÁRIO NÃO REGISTRAS PESSOAS, POR ISSO OS METODOS
    ESTÃO VAZIOS.*/
    @Override
    public void register(Person p) {

    }
    @Override
    public void remove(String cpf) {

    }
    @Override
    public void update(Person p) {

    }

    public Report generateReport(String type, Manage m) {

        if (type.equalsIgnoreCase("mensal")) {
            System.out.println("Gerando relatório mensal...");
            return generateMonthlyReport(m); // ← retorna direto
        } else if (type.equalsIgnoreCase("mais emprestados")) {
            System.out.println("Gerando relatório de livros mais emprestados...");
            return generateMostBorrowedReport(m); // ← retorna direto
        } else {
            System.out.println("O tipo de relatório é inválido.");
            return null; // ou lançar uma exceção
        }
    }

    public Report generateMonthlyReport(Manage m) {

        Report r = new Report("mensal");
        int total = 0;

        for (Client c : m.getClients()) {
            for (Loan l : c.getLoans()) {
                // ve se o empréstimo é do mês atual
                if (l.getLoanDate().getMonthValue() == LocalDate.now().getMonthValue()) {
                    total++;
                }
            }
        }
        System.out.println("Total de empréstimos no mês: " + total);
        return r;
    }

    public Report generateMostBorrowedReport(Manage m) {

        Report r = new Report("maisEmprestados");

        List<Book> booksChecked = new ArrayList<>(); // evita contar o mesmo livro várias vezes

        // percorre todos os clientes
        for (Client c : m.getClients()) {
            // percorre os empréstimos de cada cliente
            for (Loan l : c.getLoans()) {
                Book b = l.getBookCopy().getBook(); // pega o livro do empréstimo
                // se ainda não contou tal livro
                if (!booksChecked.contains(b)) {
                    int count = 0;
                    // percorre tudo de novo pra contar quantas vezes aparece
                    for (Client c2 : m.getClients()) {
                        for (Loan l2 : c2.getLoans()) {
                            if (l2.getBookCopy().getBook().equals(b)) {
                                count++;
                            }
                        }
                    }
                    System.out.println(b.getBookName() + ": " + count);
                    booksChecked.add(b); // marca como contado
                }
            }
        }
        return r;
    }
}