package br.com.atlas.dao;

import br.com.atlas.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    private Connection conn;

    public BookDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Book book) throws SQLException {
        String sqlBook = "INSERT INTO Book (bookName, bookLocation, numberOfPages, publisher) VALUES (?, ?, ?, ?)";
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(sqlBook, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getBookName());
            stmt.setString(2, book.getBookLocation());
            stmt.setInt(3, book.getNumberOfPages());
            stmt.setString(4, book.getPublisher());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    book.setBookId(rs.getInt(1));
                }
            }

            saveAuthors(book);
            saveCategories(book);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void update(Book book) throws SQLException {
        String sqlBook = "UPDATE Book SET BookName=?, BookLocation=?, NumberOfPages=?, Publisher=? WHERE BookId=?";
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(sqlBook)) {
            stmt.setString(1, book.getBookName());
            stmt.setString(2, book.getBookLocation());
            stmt.setInt(3, book.getNumberOfPages());
            stmt.setString(4, book.getPublisher());
            stmt.setInt(5, book.getBookId());
            stmt.executeUpdate();

            try (PreparedStatement st1 = conn.prepareStatement("DELETE FROM BookAuthor WHERE bookId = ?");
                 PreparedStatement st2 = conn.prepareStatement("DELETE FROM BookCategory WHERE bookId = ?")) {
                st1.setInt(1, book.getBookId());
                st2.setInt(1, book.getBookId());
                st1.executeUpdate();
                st2.executeUpdate();
            }

            saveAuthors(book);
            saveCategories(book);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM Book";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book b = mapResultSet(rs);
                fillAuthorsAndCategories(b); // Importante para exibir na tabela
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Book findById(int bookId) throws SQLException {
        String sql = "SELECT * FROM Book WHERE BookId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book b = mapResultSet(rs);
                    fillAuthorsAndCategories(b);
                    return b;
                }
            }
        }
        return null;
    }

    public void delete(int bookId) throws SQLException {
        // Primeiro deletamos os vínculos para não dar erro de Chave Estrangeira
        try (PreparedStatement st1 = conn.prepareStatement("DELETE FROM BookAuthor WHERE bookId = ?");
             PreparedStatement st2 = conn.prepareStatement("DELETE FROM BookCategory WHERE bookId = ?");
             PreparedStatement st3 = conn.prepareStatement("DELETE FROM Book WHERE BookId = ?")) {
            
            st1.setInt(1, bookId);
            st2.setInt(1, bookId);
            st3.setInt(1, bookId);
            
            st1.executeUpdate();
            st2.executeUpdate();
            st3.executeUpdate();
        }
    }

    // MÉTODOS AUXILIARES PRIVADOS
    private void saveAuthors(Book book) throws SQLException {
        String sqlGetAuthor = "SELECT authorId FROM Author WHERE authorName = ?";
        String sqlInsAuthor = "INSERT INTO Author (authorName) VALUES (?)";
        String sqlLink = "INSERT INTO BookAuthor (bookId, authorId) VALUES (?, ?)";

        for (String name : book.getAuthors()) {
            int authorId = -1;
            try (PreparedStatement ps = conn.prepareStatement(sqlGetAuthor)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) authorId = rs.getInt(1);
                }
            }

            if (authorId == -1) {
                try (PreparedStatement ps = conn.prepareStatement(sqlInsAuthor, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, name);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) authorId = rs.getInt(1);
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlLink)) {
                ps.setInt(1, book.getBookId());
                ps.setInt(2, authorId);
                ps.executeUpdate();
            }
        }
    }

    private void saveCategories(Book book) throws SQLException {
        String sqlGetCat = "SELECT categoryId FROM Category WHERE categoryName = ?";
        String sqlInsCat = "INSERT INTO Category (categoryName) VALUES (?)";
        String sqlLink = "INSERT INTO BookCategory (bookId, categoryId) VALUES (?, ?)";

        for (String catName : book.getCategories()) {
            int catId = -1;
            try (PreparedStatement ps = conn.prepareStatement(sqlGetCat)) {
                ps.setString(1, catName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) catId = rs.getInt(1);
                }
            }

            if (catId == -1) {
                try (PreparedStatement ps = conn.prepareStatement(sqlInsCat, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, catName);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) catId = rs.getInt(1);
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlLink)) {
                ps.setInt(1, book.getBookId());
                ps.setInt(2, catId);
                ps.executeUpdate();
            }
        }
    }

    private void fillAuthorsAndCategories(Book b) throws SQLException {
        String sqlAuth = "SELECT a.authorName FROM Author a JOIN BookAuthor ba ON a.authorId = ba.authorId WHERE ba.bookId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlAuth)) {
            ps.setInt(1, b.getBookId());
            try (ResultSet rs = ps.executeQuery()) {
                List<String> authors = new ArrayList<>();
                while (rs.next()) authors.add(rs.getString("authorName"));
                b.setAuthors(authors);
            }
        }

        String sqlCat = "SELECT c.categoryName FROM Category c JOIN BookCategory bc ON c.categoryId = bc.categoryId WHERE bc.bookId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCat)) {
            ps.setInt(1, b.getBookId());
            try (ResultSet rs = ps.executeQuery()) {
                List<String> categories = new ArrayList<>();
                while (rs.next()) categories.add(rs.getString("categoryName"));
                b.setCategories(categories);
            }
        }
    }

    private Book mapResultSet(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setBookId(rs.getInt("BookId"));
        b.setBookName(rs.getString("BookName"));
        b.setBookLocation(rs.getString("BookLocation"));
        b.setNumberOfPages(rs.getInt("NumberOfPages"));
        b.setPublisher(rs.getString("Publisher"));
        return b;
    }

    /**
     * Busca livros pelo nome (ou parte do nome).
     * @param name Nome ou trecho do título do livro.
     * @return Lista de livros que coincidem com a busca.
     */
    public List<Book> findByName(String name) {
        List<Book> list = new ArrayList<>();
        // O operador LIKE com % permite encontrar o texto em qualquer parte do nome
        String sql = "SELECT * FROM Book WHERE BookName LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book b = mapResultSet(rs);
                    // Crucial: Carregar autores e categorias para cada resultado
                    fillAuthorsAndCategories(b); 
                    list.add(b);
                }
            }
        } catch (SQLException e) {
            // Log do erro para ajudar no debug
            System.err.println("Erro ao buscar livro por nome: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
}