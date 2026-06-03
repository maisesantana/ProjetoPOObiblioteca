package br.com.atlas.webcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.dao.BookDAO;
import br.com.atlas.dao.CategoryDAO;
import br.com.atlas.model.Book;
import br.com.atlas.model.BookCopy;
import br.com.atlas.model.Category;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Librarian;
import br.com.atlas.util.ConnectionDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Polimorfismo: implementa Gerenciavel operando sobre a entidade Livro.
 * cadastrar() → cadastra Livro
 * atualizar() → atualiza Livro
 * remover()   → remove Livro
 */
public class LibrarianController extends HttpServlet implements Gerenciavel {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("userLogged");

        if (!(user instanceof Librarian)) {
            response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=access_denied");
            return;
        }

        String action = request.getParameter("action");

        if ("registerBook".equals(action)) {
            cadastrar(request, response);
        } else if ("updateBook".equals(action)) {
            atualizar(request, response);
        } else if ("deleteBook".equals(action)) {
            remover(request, response);
        } else if ("registerCopy".equals(action)) {
            try {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                try (Connection conn = ConnectionDb.getConexao()) {
                    new BookCopyDAO(conn).insertByBookId(bookId);
                }
                response.sendRedirect(request.getContextPath() + "/view/librarian/inventory.jsp?msg=copy_added");
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/view/librarian/registerBook.jsp?msg=error");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/view/librarian/registerBook.jsp?msg=invalid_action");
        }
    }

    // POLIMORFISMO: cadastrar() operando sobre Livro
    @Override
    public void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name           = request.getParameter("name");
            String publisher      = request.getParameter("publisher");
            int pages             = Integer.parseInt(request.getParameter("pages"));
            String shelf          = request.getParameter("shelf");
            String rack           = request.getParameter("rack");
            String authorsRaw     = request.getParameter("authors");
            String categoryIdStr  = request.getParameter("categoryId");
            String newCategoryName = request.getParameter("newCategoryName");
            int copies            = Integer.parseInt(request.getParameter("copies"));

            List<String> authors = Arrays.stream(authorsRaw.split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

            try (Connection conn = ConnectionDb.getConexao()) {
                conn.setAutoCommit(false);
                try {
                    BookDAO bookDAO = new BookDAO(conn);
                    CategoryDAO categoryDAO = new CategoryDAO(conn);

                    Book book = new Book();
                    book.setBookName(name);
                    book.setPublisher(publisher);
                    book.setNumberOfPages(pages);
                    book.setBookLocation(shelf + "-" + rack);
                    book.setAuthors(authors);

                    if ("outra".equals(categoryIdStr)) {
                        if (newCategoryName == null || newCategoryName.isBlank()) {
                            response.sendRedirect(request.getContextPath()
                                + "/view/librarian/registerBook.jsp?msg=error&detail=Nome+da+nova+categoria+obrigatorio");
                            return;
                        }
                        String nomeLimpo = newCategoryName.trim();
                        Category existing = categoryDAO.findByName(nomeLimpo);
                        if (existing != null) {
                            book.setCategories(List.of(existing.getCategoryName()));
                        } else {
                            Category nova = new Category(nomeLimpo);
                            categoryDAO.insert(nova);
                            book.setCategories(List.of(nova.getCategoryName()));
                        }
                    } else if (categoryIdStr != null && !categoryIdStr.isBlank()) {
                        Category category = categoryDAO.findAll().stream()
                                .filter(c -> String.valueOf(c.getCategoryId()).equals(categoryIdStr))
                                .findFirst().orElse(null);
                        if (category != null) {
                            book.setCategories(List.of(category.getCategoryName()));
                        }
                    }

                    bookDAO.insert(book);

                    if (copies > 0) {
                        BookCopyDAO copyDAO = new BookCopyDAO(conn);
                        for (int i = 0; i < copies; i++) {
                            copyDAO.insert(new BookCopy(book));
                        }
                    }

                    conn.commit();
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            }

            response.sendRedirect(request.getContextPath() + "/view/librarian/registerBook.jsp?msg=book_added");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/view/librarian/registerBook.jsp?msg=error");
        }
    }

    // POLIMORFISMO: atualizar() operando sobre Livro
    @Override
    public void atualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int bookId    = Integer.parseInt(request.getParameter("bookId"));
            String name   = request.getParameter("name");
            String publisher = request.getParameter("publisher");
            int pages     = Integer.parseInt(request.getParameter("pages"));
            String location = request.getParameter("location");
            String authorsRaw = request.getParameter("authors");

            List<String> authors = Arrays.stream(authorsRaw.split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

            try (Connection conn = ConnectionDb.getConexao()) {
                Book book = new Book();
                book.setBookId(bookId);
                book.setBookName(name);
                book.setPublisher(publisher);
                book.setNumberOfPages(pages);
                book.setBookLocation(location);
                book.setAuthors(authors);
                new BookDAO(conn).update(book);
            }

            response.sendRedirect(request.getContextPath() + "/bookList?msg=update_success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/bookList?msg=error");
        }
    }

    // POLIMORFISMO: remover() operando sobre Livro
    @Override
    public void remover(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            try (Connection conn = ConnectionDb.getConexao()) {
                new BookDAO(conn).delete(bookId);
            }
            response.sendRedirect(request.getContextPath() + "/removeBook?msg=book_removed");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/removeBook?msg=error");
        }
    }
}