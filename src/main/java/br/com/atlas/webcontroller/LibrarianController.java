package br.com.atlas.webcontroller;

import br.com.atlas.model.Librarian;
import br.com.atlas.model.Book;
import br.com.atlas.model.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;

@WebServlet("/librarian/action")
public class LibrarianController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("userLogged");

        if (!(user instanceof Librarian)) {
            response.sendRedirect("../login.jsp?msg=access_denied");
            return;
        }

        Librarian librarian = (Librarian) user;
        String action = request.getParameter("action");

        try {
            if ("registerBook".equals(action)) {
                Book book = new Book();
                book.setBookName(request.getParameter("name"));
                book.setPublisher(request.getParameter("publisher"));
                book.setNumberOfPages(Integer.parseInt(request.getParameter("pages")));

                // Como não tem AuthorDAO, pegamos a String da tela e transformamos em Lista
                String authorsRaw = request.getParameter("authors"); // ex: "Autor A, Autor B"
                book.setAuthors(Arrays.asList(authorsRaw.split(",")));

                librarian.registerBook(book);
                response.sendRedirect("inventory.jsp?msg=book_added");

            } else if ("registerCopy".equals(action)) {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                librarian.registerCopy(bookId);
                response.sendRedirect("inventory.jsp?msg=copy_added");
            }

        } catch (Exception e) {
            response.sendRedirect("inventory.jsp?msg=error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lógica simplificada para o Relatório PDF
        String action = request.getParameter("action");
        if ("report".equals(action)) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=relatorio_atlas.pdf");

            // Aqui você integraria com iText ou Jasper
            // Exemplo: PDFService.generate(response.getOutputStream(), data);

            try {
                response.getOutputStream().write("Conteúdo do PDF viria aqui".getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}