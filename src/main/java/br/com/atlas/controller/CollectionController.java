package br.com.atlas.controller;

import br.com.atlas.dao.BookDAO;
import br.com.atlas.model.Book;
import br.com.atlas.model.Collection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/searchBooks")
public class CollectionController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Pega o termo digitado no campo de busca do Figma
        String query = request.getParameter("query");
        
        BookDAO bookDao = new BookDAO();
        
        // 2. Busca os livros no banco (Pode ser por título como exemplo)
        List<Book> results = bookDao.findAll(); // No futuro, usar findByTitle(query)

        // 3. Usa o Model Collection para organizar os resultados
        Collection acervo = new Collection();
        acervo.setBooks(results);

        // 4. Envia para a página de resultados (Image 5 do Figma)
        request.setAttribute("acervo", acervo);
        request.setAttribute("termoBusca", query);
        request.getRequestDispatcher("views/search_results.jsp").forward(request, response);
    }
}