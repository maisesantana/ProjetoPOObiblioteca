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

@WebServlet("/bookList")
public class BookListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");
        
        // Validação de sessão robusta para funcionários do Atlas
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=session_expired");
            return;
        }

        List<Book> bookList = null;

        try (Connection conn = ConnectionDb.getConexao()) {
            BookDAO bookDAO = new BookDAO(conn);
            
            bookList = bookDAO.findAll();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Define o atributo exatamente como "books" para o JSP ler
        request.setAttribute("books", bookList);
        request.getRequestDispatcher("/view/librarian/bookList.jsp").forward(request, response);
    }
}