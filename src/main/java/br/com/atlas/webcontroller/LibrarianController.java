package br.com.atlas.webcontroller;

import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.dao.BookDAO;
import br.com.atlas.dao.CategoryDAO;
import br.com.atlas.model.Book;
import br.com.atlas.model.Category;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Librarian;
import br.com.atlas.service.BookCopyService;
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

    private final BookCopyService bookCopyService = new BookCopyService();

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

        try {
            if ("registerBook".equals(action)) {

                // Lê os parâmetros do formulário
                String name      = request.getParameter("name");
                String publisher = request.getParameter("publisher");
                int pages        = Integer.parseInt(request.getParameter("pages"));
                String shelf     = request.getParameter("shelf");
                String rack      = request.getParameter("rack");
                String authorsRaw      = request.getParameter("authors");
                String categoryIdStr   = request.getParameter("categoryId");
                String newCategoryName = request.getParameter("newCategoryName");
                int copies = Integer.parseInt(request.getParameter("copies"));

                // Validações básicas
                if (name == null || name.isBlank() || publisher == null || publisher.isBlank()
                        || authorsRaw == null || authorsRaw.isBlank()
                        || shelf == null || shelf.isBlank() || rack == null || rack.isBlank()) {
                    response.sendRedirect(request.getContextPath()
                        + "/view/librarian/registerBook.jsp?msg=error&detail=Preencha+todos+os+campos");
                    return;
                }

                List<String> authors = Arrays.stream(authorsRaw.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

                // Tudo numa única conexão e transação
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

                        // Resolve categoria dentro da mesma conexão
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
                                categoryDAO.insert(nova); // salva na mesma transação
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

                        // Insere o livro (autores e categorias vinculados dentro do BookDAO)
                        bookDAO.insert(book);

                        // Insere exemplares na mesma transação
                        if (copies > 0) {
                            BookCopyDAO copyDAO = new BookCopyDAO(conn);
                            for (int i = 0; i < copies; i++) {
                                copyDAO.insert(new br.com.atlas.model.BookCopy(book));
                            }
                        }

                        conn.commit(); // confirma tudo de uma vez

                    } catch (Exception e) {
                        conn.rollback();
                        throw e;
                    }
                }

                response.sendRedirect(request.getContextPath()
                    + "/view/librarian/registerBook.jsp?msg=book_added");

            } else if ("registerCopy".equals(action)) {

                int bookId = Integer.parseInt(request.getParameter("bookId"));
                bookCopyService.addSingleCopy(bookId);
                response.sendRedirect(request.getContextPath()
                    + "/view/librarian/inventory.jsp?msg=copy_added");

            } else {
                response.sendRedirect(request.getContextPath()
                    + "/view/librarian/inventory.jsp?msg=invalid_action");
            }

        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath()
                + "/view/librarian/registerBook.jsp?msg=error&detail=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath()
                + "/view/librarian/registerBook.jsp?msg=error");
        }
    }
}