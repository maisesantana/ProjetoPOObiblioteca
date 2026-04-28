package br.com.atlas.model;

import java.time.LocalDate;

import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.dao.BookDAO;

public class Librarian extends Employee {

    public Librarian(String cpf, String name, String email, String gender,
                    LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate, password);
    }

    public void registerBook(Book b) {
        try {
            BookDAO bookDAO = new BookDAO();
            bookDAO.insert(b);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar livro", e);
        }
    }

    public void removeBook(int bookId) {
        try {
            BookDAO bookDAO = new BookDAO();
            bookDAO.delete(bookId);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover livro", e);
        }
    }

    public void updateBook(Book b) {
        try {
            BookDAO bookDAO = new BookDAO();
            bookDAO.update(b);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar livro", e);
        }
    }

    public void addCopies(int bookId, int quantity) {
        try {
            BookDAO bookDAO = new BookDAO();
            BookCopyDAO copyDAO = new BookCopyDAO();

            Book book = bookDAO.findById(bookId);

            if (book == null) {
                throw new RuntimeException("Livro não encontrado");
            }

            for (int i = 0; i < quantity; i++) {
                BookCopy copy = new BookCopy(book);
                copyDAO.insert(copy);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar exemplares", e);
        }
    }

    public void registerCopy(int bookId) {
        try {
            BookCopyDAO copyDAO = new BookCopyDAO();
            copyDAO.insertByBookId(bookId);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar exemplar", e);
        }
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