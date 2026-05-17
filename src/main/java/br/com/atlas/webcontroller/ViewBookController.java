package br.com.atlas.webcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

@WebServlet("/viewBook")
public class ViewBookController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?msg=session_expired");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/searchBooks");
            return;
        }

        try {
            int bookId = Integer.parseInt(idParam);

            try (Connection conn = ConnectionDb.getConexao()) {
                BookDAO bookDAO = new BookDAO(conn);
                Book book = bookDAO.findById(bookId);

                if (book == null) {
                    response.sendRedirect(request.getContextPath() + "/searchBooks");
                    return;
                }

                // Carrega os exemplares do livro
                String sqlCopies = "SELECT BookCopyId, StatusAvailable FROM BookCopy WHERE BookId = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlCopies)) {
                    ps.setInt(1, bookId);
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

                request.setAttribute("book", book);
                request.getRequestDispatcher("/view/librarian/viewBook.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/searchBooks");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/searchBooks");
        }
    }
}