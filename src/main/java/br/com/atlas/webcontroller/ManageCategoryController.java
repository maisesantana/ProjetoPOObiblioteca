package br.com.atlas.webcontroller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.atlas.dao.CategoryDAO;
import br.com.atlas.model.Category;
import br.com.atlas.util.ConnectionDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/manageCategory")
public class ManageCategoryController extends HttpServlet {
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
        List<Category> categoryList = new ArrayList<>();

        try (Connection conn = ConnectionDb.getConexao()) {
            CategoryDAO categoryDAO = new CategoryDAO(conn);
            if (query != null && !query.trim().isEmpty()) {
                Category cat = categoryDAO.findByName(query.trim());
                if (cat != null) {
                    categoryList.add(cat);
                }
            } else {
                categoryList = categoryDAO.findAll();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("categories", categoryList);
        request.getRequestDispatcher("/view/librarian/manageCategory.jsp").forward(request, response);
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

        String action = request.getParameter("action");
        String query = request.getParameter("query") != null ? request.getParameter("query") : "";

        try (Connection conn = ConnectionDb.getConexao()) {
            CategoryDAO categoryDAO = new CategoryDAO(conn);

            if ("insert".equals(action)) {
                String name = request.getParameter("categoryName");
                if (name != null && !name.trim().isEmpty()) {
                    categoryDAO.insert(new Category(name.trim()));
                    response.sendRedirect(request.getContextPath() + "/manageCategory?msg=category_added");
                    return;
                }
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("categoryId"));
                String name = request.getParameter("categoryName");
                if (name != null && !name.trim().isEmpty()) {
                    categoryDAO.update(new Category(id, name.trim()));
                    response.sendRedirect(
                            request.getContextPath() + "/manageCategory?query=" + query + "&msg=category_updated");
                    return;
                }
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("categoryId"));
                categoryDAO.delete(id);
                response.sendRedirect(
                        request.getContextPath() + "/manageCategory?query=" + query + "&msg=category_deleted");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/manageCategory?query=" + query + "&msg=error");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/manageCategory");
    }
}