package br.com.atlas.service;

import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.dao.BookDAO;
import br.com.atlas.model.Book;
import br.com.atlas.model.BookCopy;
import br.com.atlas.util.ConnectionDb;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class BookCopyService {

    private final BookCopyDAO bookCopyDAO;

    public BookCopyService() {
        this.bookCopyDAO = new BookCopyDAO();
    }

    // ADICIONA MÚLTIPLOS EXEMPLARES DE UM LIVRO
    public void addCopies(int bookId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero!");
        }

        try (Connection conn = ConnectionDb.getConexao()) {
            Book book = new BookDAO(conn).findById(bookId);

            if (book == null) {
                throw new IllegalArgumentException("Livro não encontrado com ID: " + bookId);
            }

            BookCopyDAO copyDAO = new BookCopyDAO(conn);
            for (int i = 0; i < quantity; i++) {
                copyDAO.insert(new BookCopy(book));
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar exemplares.", e);
        }
    }

    // ADICIONA UM ÚNICO EXEMPLAR
    public void addSingleCopy(int bookId) {
        if (!bookCopyDAO.insertByBookId(bookId)) {
            throw new RuntimeException("Erro ao cadastrar exemplar para o livro ID: " + bookId);
        }
    }

    // READ ALL
    public List<BookCopy> findAll() {
        return bookCopyDAO.findAll();
    }

    // READ BY ID
    public Optional<BookCopy> findById(int copyId) {
        return Optional.ofNullable(bookCopyDAO.findById(copyId));
    }

    // DELETE
    public void delete(int copyId) {
        if (!bookCopyDAO.delete(copyId)) {
            throw new RuntimeException("Erro ao remover exemplar ID: " + copyId);
        }
    }
}