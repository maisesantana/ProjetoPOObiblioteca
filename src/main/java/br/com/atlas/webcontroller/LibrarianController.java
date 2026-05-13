package br.com.atlas.webcontroller;

import br.com.atlas.model.Book;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Librarian;
import br.com.atlas.service.BookCopyService;
import br.com.atlas.service.BookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;

@WebServlet("/librarian/action")
public class LibrarianController extends HttpServlet {

    private final BookService bookService         = new BookService();
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

                Book book = new Book();
                book.setBookName(request.getParameter("name"));
                book.setPublisher(request.getParameter("publisher"));
                book.setNumberOfPages(Integer.parseInt(request.getParameter("pages")));

                String authorsRaw = request.getParameter("authors"); // ex: "Autor A, Autor B"
                book.setAuthors(Arrays.asList(authorsRaw.split(",")));

                bookService.insert(book);
                response.sendRedirect("registerBook.jsp?msg=book_added");

            } else if ("registerCopy".equals(action)) {

                int bookId = Integer.parseInt(request.getParameter("bookId"));
                bookCopyService.addSingleCopy(bookId);
                response.sendRedirect("inventory.jsp?msg=copy_added");

            } else {
                response.sendRedirect("inventory.jsp?msg=invalid_action");
            }

        } catch (IllegalArgumentException e) {
            response.sendRedirect("inventory.jsp?msg=error&detail=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("inventory.jsp?msg=error");
        }
    }
}