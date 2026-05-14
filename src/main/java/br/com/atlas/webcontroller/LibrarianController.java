package br.com.atlas.webcontroller;

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
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/librarian/action")
public class LibrarianController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("userLogged");

        if (!(user instanceof Librarian)) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=access_denied");
            return;
        }

        String action = request.getParameter("action");

        // ===== DEBUG: imprime TUDO que chegou do formulário =====
        System.out.println("========== DEBUG LibrarianController ==========");
        System.out.println("action        = " + action);
        System.out.println("name          = " + request.getParameter("name"));
        System.out.println("publisher     = " + request.getParameter("publisher"));
        System.out.println("pages         = " + request.getParameter("pages"));
        System.out.println("authors       = " + request.getParameter("authors"));
        System.out.println("categoryId    = " + request.getParameter("categoryId"));
        System.out.println("newCategory   = " + request.getParameter("newCategoryName"));
        System.out.println("copies        = " + request.getParameter("copies"));
        System.out.println("shelf         = " + request.getParameter("shelf"));
        System.out.println("rack          = " + request.getParameter("rack"));
        System.out.println("===============================================");
        // ========================================================

        try {
            if ("registerBook".equals(action)) {

                String name      = request.getParameter("name");
                String publisher = request.getParameter("publisher");
                int pages        = Integer.parseInt(request.getParameter("pages"));
                String shelf     = request.getParameter("shelf");
                String rack      = request.getParameter("rack");
                String authorsRaw      = request.getParameter("authors");
                String categoryIdStr   = request.getParameter("categoryId");
                String newCategoryName = request.getParameter("newCategoryName");
                int copies = Integer.parseInt(request.getParameter("copies"));

                List<String> authors = Arrays.stream(authorsRaw.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

                try (Connection conn = ConnectionDb.getConexao()) {
                    conn.setAutoCommit(false);
                    try {
                        BookDAO bookDAO         = new BookDAO(conn);
                        CategoryDAO categoryDAO = new CategoryDAO(conn);

                        Book book = new Book();
                        book.setBookName(name);
                        book.setPublisher(publisher);
                        book.setNumberOfPages(pages);
                        book.setBookLocation(shelf + "-" + rack);
                        book.setAuthors(authors);

                        System.out.println("bookLocation setado = " + book.getBookLocation());
                        System.out.println("authors setados     = " + book.getAuthors());

                        // Resolve categoria
                        if ("outra".equals(categoryIdStr)) {
                            String nomeLimpo = newCategoryName.trim();
                            Category existing = categoryDAO.findByName(nomeLimpo);
                            if (existing != null) {
                                book.setCategories(List.of(existing.getCategoryName()));
                            } else {
                                Category nova = new Category(nomeLimpo);
                                categoryDAO.insert(nova);
                                System.out.println("Nova categoria inserida ID = " + nova.getCategoryId());
                                book.setCategories(List.of(nova.getCategoryName()));
                            }
                        } else if (categoryIdStr != null && !categoryIdStr.isBlank()) {
                            Category category = categoryDAO.findAll().stream()
                                .filter(c -> String.valueOf(c.getCategoryId()).equals(categoryIdStr))
                                .findFirst().orElse(null);
                            if (category != null) {
                                book.setCategories(List.of(category.getCategoryName()));
                                System.out.println("Categoria selecionada = " + category.getCategoryName());
                            } else {
                                System.out.println("AVISO: categoria não encontrada pelo ID = " + categoryIdStr);
                            }
                        }

                        System.out.println("categories setadas = " + book.getCategories());

                        // Insere livro
                        bookDAO.insert(book);
                        System.out.println("bookId gerado = " + book.getBookId());

                        // Insere exemplares
                        if (copies > 0) {
                            BookCopyDAO copyDAO = new BookCopyDAO(conn);
                            for (int i = 0; i < copies; i++) {
                                copyDAO.insert(new BookCopy(book));
                            }
                            System.out.println("Exemplares inseridos = " + copies);
                        }

                        conn.commit();
                        System.out.println("COMMIT realizado com sucesso!");

                    } catch (Exception e) {
                        conn.rollback();
                        System.out.println("ROLLBACK executado! Erro: " + e.getMessage());
                        e.printStackTrace();
                        throw e;
                    }
                }

                response.sendRedirect(request.getContextPath()
                    + "/view/librarian/registerBook.jsp?msg=book_added");

            } else if ("registerCopy".equals(action)) {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                try (Connection conn = ConnectionDb.getConexao()) {
                    new BookCopyDAO(conn).insertByBookId(bookId);
                }
                response.sendRedirect(request.getContextPath()
                    + "/view/librarian/inventory.jsp?msg=copy_added");
            } else {
                response.sendRedirect(request.getContextPath()
                    + "/view/librarian/inventory.jsp?msg=invalid_action");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath()
                + "/view/librarian/registerBook.jsp?msg=error&detail=" + e.getMessage());
        }
    }
}