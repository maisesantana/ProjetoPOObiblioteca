package br.com.atlas.model;

import java.sql.Connection;
import java.time.LocalDate;

import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.dao.BookDAO;
import br.com.atlas.dao.CategoryDAO;
import br.com.atlas.dao.AuthorDAO;
import br.com.atlas.util.ConnectionDb;

public class Librarian extends Employee {

    public Librarian(String cpf, String name, String email, char gender,
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

    public void registerCategory(String c) {
        try {
            Connection conn = ConnectionDb.getConexao();
            CategoryDAO cat = new CategoryDAO(conn);
            cat.insert(c);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar categoria", e);
        }
    }

    public void removeCategory(String c) {
        try {
            Connection conn = ConnectionDb.getConexao();
            CategoryDAO cat = new CategoryDAO(conn);
            cat.insert(c);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover categoria", e);
        }
    }

    public void editCategory(String c) {
        try {
            Connection conn = ConnectionDb.getConexao();
            CategoryDAO cat = new CategoryDAO(conn);
            cat.update(c);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar categoria", e);
        }
    }

    public void registerAuthor(String a) {
        try {
            Connection conn = ConnectionDb.getConexao();
            AuthorDAO cat = new AuthorDAO(conn);
            cat.insert(a);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar autor", e);
        }
    }

    public void removeAuthor(String a) {
        try {
            Connection conn = ConnectionDb.getConexao();
            AuthorDAO author = new AuthorDAO(conn);
            author.insert(a);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover autor", e);
        }
    }

    public void editAuthor(String a) {
        try {
            Connection conn = ConnectionDb.getConexao();
            AuthorDAO author = new AuthorDAO(conn);
            author.update(a);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar autor", e);
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