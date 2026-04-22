package br.com.atlas.controller;

import br.com.atlas.dao.BookDAO;
import br.com.atlas.model.Book;
import br.com.atlas.model.Publisher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/registerBook")
public class BookController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        /*
         * 1. Captura os dados do formulário
         * Certifique-se de que os nomes dos inputs no JSP batam com esses nomes.
         */
        String bookName = request.getParameter("bookName");
        String bookLocation = request.getParameter("bookLocation");
        int numberOfPages = Integer.parseInt(request.getParameter("numberOfPages"));
        String bookSubject = request.getParameter("bookSubject");
        String bookOrigin = request.getParameter("bookOrigin");
        int publisherId = Integer.parseInt(request.getParameter("publisherId"));

        // 2. Monta o objeto Publisher (Editora)
        Publisher publisher = new Publisher();
        publisher.setPublisherId(publisherId);

        // 3. Monta o objeto Book (usando o construtor da classe Book.java)
        Book newBook = new Book(0, bookName, bookLocation, numberOfPages, bookSubject, bookOrigin, publisher);

        // 4. Salva usando o DAO
        BookDAO bookDao = new BookDAO();
        bookDao.insert(newBook);

        /*
         * 5. Redirecionamento
         * Apontando para a pasta 'librarian' (Bibliotecário)
         */
        response.sendRedirect("views/librarian/success.jsp");
    }
}