package br.com.atlas.webcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import br.com.atlas.dao.AuthorDAO;
import br.com.atlas.model.Author;
import br.com.atlas.util.ConnectionDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/manageAuthors")
public class ManageAuthorsController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (session.getAttribute("userLogged") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?msg=session_expired");
            return;
        }

        String query = request.getParameter("query");
        String tab = request.getParameter("tab");

        // SE A ABA VIER VAZIA (CLIQUE INICIAL DA NAVBAR), FORÇA 'register'
        if (tab == null || tab.trim().isEmpty()) {
            tab = "register";
        }

        List<Author> authors = null;

        try (Connection conn = ConnectionDb.getConexao()) {
            AuthorDAO dao = new AuthorDAO(conn);
            if (query != null && !query.trim().isEmpty()) {
                authors = dao.findByName(query.trim());
            } else {
                authors = dao.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("authors", authors);
        // ADICIONA O ATRIBUTO TAB DIRETAMENTE NA REQUISIÇÃO PARA O JSP ENXERGAR
        request.setAttribute("activeTab", tab);

        request.getRequestDispatcher("/view/librarian/manageAuthors.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (session.getAttribute("userLogged") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?msg=session_expired");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("register".equals(action)) {

                String name = request.getParameter("authorName");
                if (name == null || name.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/manageAuthors?msg=name_empty&tab=register");
                    return;
                }

                try (Connection conn = ConnectionDb.getConexao()) {
                    new AuthorDAO(conn).insert(new Author(name.trim()));
                }

                response.sendRedirect(request.getContextPath() + "/manageAuthors?msg=author_added&tab=register");

            } else if ("update".equals(action)) {

                String idParam = request.getParameter("authorId");
                String name = request.getParameter("authorName");
                String query = request.getParameter("query");

                if (name == null || name.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/manageAuthors?msg=name_empty&tab=edit");
                    return;
                }

                int authorId = Integer.parseInt(idParam);

                try (Connection conn = ConnectionDb.getConexao()) {
                    new AuthorDAO(conn).update(new Author(authorId, name.trim()));
                }

                String redirect = request.getContextPath() + "/manageAuthors?msg=author_updated&tab=edit";
                if (query != null && !query.trim().isEmpty()) {
                    redirect += "&query=" + java.net.URLEncoder.encode(query, "UTF-8");
                }
                response.sendRedirect(redirect);

            } else if ("delete".equals(action)) {

                String idParam = request.getParameter("authorId");
                String query = request.getParameter("query");
                int authorId = Integer.parseInt(idParam);

                try (Connection conn = ConnectionDb.getConexao()) {
                    AuthorDAO dao = new AuthorDAO(conn);

                    // Verifica se há livros vinculados antes de remover
                    List<String> books = dao.findBooksByAuthor(authorId);
                    if (!books.isEmpty()) {
                        // Bloqueia a remoção e avisa
                        String redirect = request.getContextPath() + "/manageAuthors?msg=author_has_books&tab=edit";
                        if (query != null && !query.trim().isEmpty()) {
                            redirect += "&query=" + java.net.URLEncoder.encode(query, "UTF-8");
                        }
                        response.sendRedirect(redirect);
                        return;
                    }

                    dao.delete(authorId);
                }

                String redirect = request.getContextPath() + "/manageAuthors?msg=author_deleted&tab=edit";
                if (query != null && !query.trim().isEmpty()) {
                    redirect += "&query=" + java.net.URLEncoder.encode(query, "UTF-8");
                }
                response.sendRedirect(redirect);

            } else {
                response.sendRedirect(request.getContextPath() + "/manageAuthors?msg=error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/manageAuthors?msg=error");
        }
    }
}