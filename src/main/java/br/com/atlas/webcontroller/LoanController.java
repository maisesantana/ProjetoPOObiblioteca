package br.com.atlas.webcontroller;

import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.dao.BookDAO;
import br.com.atlas.dao.ClientDAO;
import br.com.atlas.dao.LoanDAO;
import br.com.atlas.model.Book;
import br.com.atlas.model.BookCopy;
import br.com.atlas.model.Client;
import br.com.atlas.service.LoanService;
import br.com.atlas.util.ConnectionDb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/loan")
public class LoanController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final LoanService loanService =
            new LoanService(
                    new LoanDAO(),
                    new ClientDAO(),
                    new BookCopyDAO()
            );

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            System.out.println("[LoanController] doGet called. URI=" + request.getRequestURI()
                    + " query=" + request.getQueryString());

            // Recebe o bookId vindo do ViewBook
            String bookIdParam = request.getParameter("bookId");

            /*
             * Se veio bookId:
             * carrega o livro e envia para a JSP.
             */
            if (bookIdParam != null && !bookIdParam.isBlank()) {

                int bookId = Integer.parseInt(bookIdParam);

                try (Connection conn = ConnectionDb.getConexao()) {

                    BookDAO bookDAO = new BookDAO(conn);

                    Book book = bookDAO.findById(bookId);

                    request.setAttribute("selectedBook", book);
                }
            }

            request.getRequestDispatcher(
                    "/view/attendant/loan.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            throw new ServletException(
                    "Erro ao abrir tela de empréstimo",
                    e
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {

            System.out.println("[LoanController] doPost called. URI=" + request.getRequestURI()
                    + " action=" + request.getParameter("action"));

            String action = request.getParameter("action");

            // Ação de busca de livros
            if ("searchBook".equals(action)) {
                searchBook(request, response);
                return;
            }

            // Ação de busca de cliente
            if ("searchClient".equals(action)) {
                searchClient(request, response);
                return;
            }

            // Ação de registrar empréstimo
            if (action == null || action.isBlank()) {
                registerLoanAction(request, response);
            }

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Busca livros por nome/autor e retorna com exemplares disponíveis
     */
    private void searchBook(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {

        String query = request.getParameter("q");
        System.out.println("[LoanController] searchBook query='" + query + "'");

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = ConnectionDb.getConexao()) {

            BookDAO bookDAO = new BookDAO(conn);
            BookCopyDAO copyDAO = new BookCopyDAO();

            // Busca livros
            List<Book> books = bookDAO.findByName(query);

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            for (Book book : books) {
                // Para cada livro, busca exemplares disponíveis
                List<BookCopy> availableCopies = copyDAO.findAll().stream()
                        .filter(copy -> copy.getBook().getBookId() == book.getBookId()
                                && copy.isAvailable())
                        .toList();

                if (!availableCopies.isEmpty()) {
                    if (!first) {
                        json.append(",");
                    }
                    first = false;

                    json.append("{");
                    json.append("\"bookId\":").append(book.getBookId()).append(",");
                    json.append("\"bookName\":\"").append(escapeJson(book.getBookName())).append("\",");
                    json.append("\"authors\":\"").append(escapeJson(String.join(", ", book.getAuthors()))).append("\",");
                    json.append("\"availableCopies\":").append(availableCopies.size()).append(",");
                    json.append("\"totalCopies\":").append(book.getCopies().size()).append(",");
                    json.append("\"copies\":[");

                    boolean firstCopy = true;
                    for (BookCopy copy : availableCopies) {
                        if (!firstCopy) {
                            json.append(",");
                        }
                        firstCopy = false;
                        json.append("{\"copyId\":").append(copy.getBookCopyId()).append("}");
                    }

                    json.append("]");
                    json.append("}");
                }
            }

            json.append("]");
            out.print(json.toString());
            out.flush();

        } catch (Exception e) {
            out.print("[]");
            out.flush();
        }
    }

    /**
     * Busca cliente por CPF e retorna informações
     */
    private void searchClient(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {

        String cpf = request.getParameter("cpf");

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = ConnectionDb.getConexao()) {

            ClientDAO clientDAO = new ClientDAO();

            Client client = clientDAO.findByCpf(cpf);

            StringBuilder json = new StringBuilder("{");

            if (client == null) {
                json.append("\"found\":false,");
                json.append("\"error\":\"Cliente não encontrado\"");
            } else {
                json.append("\"found\":true,");
                json.append("\"name\":\"").append(escapeJson(client.getName())).append("\",");
                json.append("\"cpf\":\"").append(escapeJson(client.getCpf())).append("\",");
                json.append("\"canBorrow\":").append(client.canBorrow());

                if (!client.canBorrow()) {
                    json.append(",\"suspensionReason\":\"Cliente está suspenso\"");
                }
            }

            json.append("}");
            out.print(json.toString());
            out.flush();

        } catch (Exception e) {
            StringBuilder errorJson = new StringBuilder("{");
            errorJson.append("\"found\":false,");
            errorJson.append("\"error\":\"Erro ao buscar cliente\"");
            errorJson.append("}");
            out.print(errorJson.toString());
            out.flush();
        }
    }

    /**
     * Registra o empréstimo
     */
    private void registerLoanAction(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {

        try {

            // CPF informado
            String cpf = request.getParameter("cpf");

            // Exemplar selecionado
            int copyId = Integer.parseInt(
                    request.getParameter("copyId")
            );

            // Regra de negócio
            loanService.registerLoan(cpf, copyId);

            response.sendRedirect(
                    request.getContextPath()
                    + "/loan?msg=success"
            );

        } catch (Exception e) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/loan?msg=error&detail="
                    + URLEncoder.encode(
                            e.getMessage(),
                            "UTF-8"
                    )
            );
        }
    }

    /**
     * Escapa caracteres especiais para JSON
     */
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}