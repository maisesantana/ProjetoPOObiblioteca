package br.com.atlas.controller;

import br.com.atlas.dao.BookDAO;
import br.com.atlas.model.Book;
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
       String publisher = request.getParameter("publisher");

// Criando o livro conforme o Model (String publisher)
        Book newBook = new Book(bookName, bookLocation, numberOfPages, bookSubject, publisher);

        BookDAO bookDao = new BookDAO();
        bookDao.insert(newBook);

        /*
         * 5. Redirecionamento
         * Apontando para a pasta 'librarian' (Bibliotecário)
         */
        response.sendRedirect("views/librarian/success.jsp");
    }
}