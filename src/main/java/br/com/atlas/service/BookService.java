package br.com.atlas.service;

import br.com.atlas.dao.BookDAO;
import br.com.atlas.model.Book;
import br.com.atlas.util.ConnectionDb;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class BookService {

    // VALIDAÇÃO CENTRALIZADA
    private void validateBook(Book book) {
        if (book.getBookName() == null || book.getBookName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do livro é obrigatório!");
        }
        if (book.getPublisher() == null || book.getPublisher().trim().isEmpty()) {
            throw new IllegalArgumentException("Editora é obrigatória!");
        }
        if (book.getNumberOfPages() <= 0) {
            throw new IllegalArgumentException("Número de páginas deve ser maior que zero!");
        }
        if (book.getAuthors() == null || book.getAuthors().isEmpty()) {
            throw new IllegalArgumentException("O livro deve ter pelo menos um autor!");
        }
    }

    // CREATE
    public void insert(Book book) {
        validateBook(book);

        try (Connection conn = ConnectionDb.getConexao()) {
            conn.setAutoCommit(false);
            try {
                new BookDAO(conn).insert(book);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar livro.", e);
        }
    }

    // READ ALL
    public List<Book> findAll() {
        try (Connection conn = ConnectionDb.getConexao()) {
            return new BookDAO(conn).findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar livros.", e);
        }
    }

    // READ BY ID
    public Optional<Book> findById(int bookId) {
        try (Connection conn = ConnectionDb.getConexao()) {
            return Optional.ofNullable(new BookDAO(conn).findById(bookId));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar livro por ID.", e);
        }
    }

    // READ BY NAME
    public List<Book> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório para busca!");
        }
        try (Connection conn = ConnectionDb.getConexao()) {
            return new BookDAO(conn).findByName(name);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar livros por nome.", e);
        }
    }

    // UPDATE
    public void update(Book book) {
        validateBook(book);

        try (Connection conn = ConnectionDb.getConexao()) {
            new BookDAO(conn).update(book);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar livro.", e);
        }
    }

    // DELETE
    public void delete(int bookId) {
        try (Connection conn = ConnectionDb.getConexao()) {
            new BookDAO(conn).delete(bookId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remover livro.", e);
        }
    }
}