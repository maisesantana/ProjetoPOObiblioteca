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
    public void register(Person p) {}
    @Override
    public void remove(String cpf) {}
    @Override
    public void update(Person p) {}
}