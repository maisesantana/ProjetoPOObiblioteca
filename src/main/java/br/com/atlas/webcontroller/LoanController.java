package br.com.atlas.webcontroller;

import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.dao.BookDAO;
import br.com.atlas.dao.ClientDAO;
import br.com.atlas.dao.LoanDAO;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Book;
import br.com.atlas.model.BookCopy;
import br.com.atlas.model.Client;
import br.com.atlas.service.LoanService;
import br.com.atlas.util.ConnectionDb;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;

public class LoanController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final LoanService loanService =
            new LoanService(new LoanDAO(), new ClientDAO(), new BookCopyDAO());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");
        if (user == null || !(user instanceof Attendant)) {
            response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
            return;
        }

        String step    = request.getParameter("step");
        String query   = request.getParameter("query");
        String bookId  = request.getParameter("bookId");
        String cpf     = request.getParameter("cpf");

        try {
            // VINDO DA VIEWBOOK — vai direto para o passo 2
            if (bookId != null && !bookId.isBlank() && step == null) {
                try (Connection conn = ConnectionDb.getConexao()) {
                    Book book = new BookDAO(conn).findById(Integer.parseInt(bookId));
                    BookCopyDAO copyDAO = new BookCopyDAO();
                    List<BookCopy> available = copyDAO.findAll().stream()
                        .filter(c -> c.getBook().getBookId() == book.getBookId() && c.isAvailable())
                        .toList();
                    available.forEach(book::addCopy);
                    request.setAttribute("selectedBook", book);
                    request.setAttribute("step", "selectBook");
                }
                request.getRequestDispatcher("/view/attendant/loan.jsp").forward(request, response);
                return;
            }

            // PASSO 1 — busca de livros (GET com query)
            if (query != null && !query.isBlank()) {
                try (Connection conn = ConnectionDb.getConexao()) {
                    BookDAO bookDAO = new BookDAO(conn);
                    BookCopyDAO copyDAO = new BookCopyDAO();
                    List<Book> books = bookDAO.findByName(query);

                    // Para cada livro, verifica se tem exemplar disponível
                    for (Book book : books) {
                        List<BookCopy> available = copyDAO.findAll().stream()
                            .filter(c -> c.getBook().getBookId() == book.getBookId() && c.isAvailable())
                            .toList();
                        available.forEach(book::addCopy);
                    }
                    // Remove livros sem exemplar disponível
                    books = books.stream().filter(b -> !b.getCopies().isEmpty()).toList();
                    request.setAttribute("books", books);
                    request.setAttribute("query", query);
                }
            }

            // PASSO 2 — livro selecionado, mostra detalhes e campo de CPF
            if ("selectBook".equals(step) && bookId != null) {
                try (Connection conn = ConnectionDb.getConexao()) {
                    Book book = new BookDAO(conn).findById(Integer.parseInt(bookId));
                    BookCopyDAO copyDAO = new BookCopyDAO();
                    List<BookCopy> available = copyDAO.findAll().stream()
                        .filter(c -> c.getBook().getBookId() == book.getBookId() && c.isAvailable())
                        .toList();
                    available.forEach(book::addCopy);
                    request.setAttribute("selectedBook", book);
                    request.setAttribute("step", "selectBook");
                }
            }

            // PASSO 3 — livro + cliente selecionados, mostra confirmação
            // PASSO 3 — livro + cliente selecionados, mostra confirmação
            if ("confirm".equals(step) && bookId != null && cpf != null) {
                try (Connection conn = ConnectionDb.getConexao()) {
                    Book book = new BookDAO(conn).findById(Integer.parseInt(bookId));
                    BookCopyDAO copyDAO = new BookCopyDAO();
                    List<BookCopy> available = copyDAO.findAll().stream()
                        .filter(c -> c.getBook().getBookId() == book.getBookId() && c.isAvailable())
                        .toList();
                    available.forEach(book::addCopy);

                    Client client = new ClientDAO().findByCpf(cpf);

                    // Verifica limite de empréstimos
                    int activeLoans = new LoanDAO().countActiveLoans(cpf);
                    request.setAttribute("activeLoans", activeLoans);

                    request.setAttribute("selectedBook", book);
                    request.setAttribute("client", client);
                    request.setAttribute("copyId", available.isEmpty() ? null : available.get(0).getBookCopyId());
                    request.setAttribute("step", "confirm");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/view/attendant/loan.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("userLogged");
        if (user == null || !(user instanceof Attendant)) {
            response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
            return;
        }

        try {
            String cpf    = request.getParameter("cpf");
            int copyId    = Integer.parseInt(request.getParameter("copyId"));
            loanService.registerLoan(cpf, copyId);
            response.sendRedirect(request.getContextPath() + "/loan?msg=success");

        } catch (IllegalStateException | IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath()
                + "/loan?msg=error&detail="
                + URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/loan?msg=error");
        }
    }
}