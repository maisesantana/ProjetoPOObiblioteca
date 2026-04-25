package br.com.atlas.model;

import java.time.LocalDate;

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

    public Report generateReport(String type) {

        Report r = new Report(type);

        if (type.equalsIgnoreCase("monthly")) {
            System.out.println("Gerando relatório mensal...");
        } else if (type.equalsIgnoreCase("mostBorrowed")) {
            System.out.println("Gerando relatório de livros mais emprestados...");
        }

        return r;
    }
}