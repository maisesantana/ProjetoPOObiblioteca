package br.com.atlas.webcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import br.com.atlas.dao.BookDAO;
import br.com.atlas.model.Book;
import br.com.atlas.util.ConnectionDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/removeBook")
public class RemoveBookController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET: busca livros pelo título e exibe a lista para o usuário escolher qual remover
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?msg=session_expired");
            return;
        }

        String query = request.getParameter("query");
        List<Book> bookList = null;

        if (query != null && !query.trim().isEmpty()) {
            try (Connection conn = ConnectionDb.getConexao()) {
                BookDAO bookDAO = new BookDAO(conn);
                bookList = bookDAO.findByName(query.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("books", bookList);
        request.getRequestDispatcher("/view/librarian/removeBook.jsp").forward(request, response);
    }

    // POST: recebe o bookId e remove do banco, depois redireciona com msg de sucesso
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?msg=session_expired");
            return;
        }

        String bookIdParam = request.getParameter("bookId");
        String query = request.getParameter("query");

        try {
            int bookId = Integer.parseInt(bookIdParam);

            try (Connection conn = ConnectionDb.getConexao()) {
                conn.setAutoCommit(false);
                try {
                    BookDAO bookDAO = new BookDAO(conn);

                    // Remove vínculos de empréstimo ativo antes de deletar
                    // O BookDAO.delete() já cuida de BookAuthor e BookCategory
                    // Precisamos remover BookCopy antes (FK)
                    try (java.sql.PreparedStatement ps =
                            conn.prepareStatement("DELETE FROM BookCopy WHERE BookId = ?")) {
                        ps.setInt(1, bookId);
                        ps.executeUpdate();
                    }

                    bookDAO.delete(bookId);
                    conn.commit();
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            }

            // Redireciona para a mesma busca com msg de sucesso
            String redirectQuery = (query != null && !query.trim().isEmpty())
                ? "?msg=book_removed&query=" + java.net.URLEncoder.encode(query, "UTF-8")
                : "?msg=book_removed";

            response.sendRedirect(request.getContextPath() + "/removeBook" + redirectQuery);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/removeBook?msg=error");
        }
    }
}