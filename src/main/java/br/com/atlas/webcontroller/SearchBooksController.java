package br.com.atlas.webcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import br.com.atlas.dao.BookDAO;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Book;
import br.com.atlas.model.BookCopy;
import br.com.atlas.model.Librarian;
import br.com.atlas.util.ConnectionDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/searchBooks")
public class SearchBooksController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");
        if (user == null || !(user instanceof Librarian || user instanceof Attendant)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?msg=session_expired");
            return;
        }

        String query = request.getParameter("query");
        List<Book> bookList = null;

        try (Connection conn = ConnectionDb.getConexao()) {
            BookDAO bookDAO = new BookDAO(conn);

            if (query != null && !query.trim().isEmpty()) {
                bookList = bookDAO.findByName(query.trim());
            } else {
                bookList = bookDAO.findAll();
            }

            // Carrega os exemplares (copies) de cada livro encontrado
            if (bookList != null) {
                String sqlCopies = "SELECT BookCopyId, StatusAvailable FROM BookCopy WHERE BookId = ?";
                for (Book book : bookList) {
                    try (PreparedStatement ps = conn.prepareStatement(sqlCopies)) {
                        ps.setInt(1, book.getBookId());
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                BookCopy copy = new BookCopy();
                                copy.setBookCopyId(rs.getInt("BookCopyId"));
                                copy.setAvailable(rs.getBoolean("StatusAvailable"));
                                copy.setBook(book);
                                book.addCopy(copy);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
           // e.printStackTrace();
        }

        request.setAttribute("books", bookList);
        request.getRequestDispatcher("/view/librarian/searchBooks.jsp").forward(request, response);
    }
}