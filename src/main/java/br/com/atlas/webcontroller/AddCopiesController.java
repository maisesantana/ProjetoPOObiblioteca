package br.com.atlas.webcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.dao.BookDAO;
import br.com.atlas.model.Book;
import br.com.atlas.model.BookCopy;
import br.com.atlas.util.ConnectionDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/addCopies")
public class AddCopiesController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object userLogged = session.getAttribute("userLogged");
        if (userLogged == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String query = request.getParameter("query");
        List<Book> bookList = null;

        if (query != null) {
            try (Connection conn = ConnectionDb.getConexao()) {
                BookDAO bookDAO = new BookDAO(conn);
                if (!query.trim().isEmpty()) {
                    bookList = bookDAO.findByName(query.trim());
                } else {
                    bookList = bookDAO.findAll();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
            }
        }

        request.setAttribute("books", bookList);
        request.getRequestDispatcher("/view/librarian/addCopies.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object userLogged = session.getAttribute("userLogged");
        if (userLogged == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String query = request.getParameter("query");

        try (Connection conn = ConnectionDb.getConexao()) {
            conn.setAutoCommit(false);
            try {
                BookCopyDAO copyDAO = new BookCopyDAO(conn);
                Book book = new Book();
                book.setBookId(bookId);

                for (int i = 0; i < quantity; i++) {
                    copyDAO.insert(new BookCopy(book));
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
           // e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/addCopies?query=" + query + "&msg=error");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/addCopies?query=" + query + "&msg=copies_added");
    }
}