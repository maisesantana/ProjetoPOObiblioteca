package br.com.atlas.webcontroller;

import br.com.atlas.dao.ClientDAO;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Client;
import br.com.atlas.model.Employee;
import br.com.atlas.service.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Polimorfismo: implementa Gerenciavel operando sobre a entidade Cliente.
 * cadastrar() → cadastra Cliente
 * atualizar() → atualiza Cliente
 * remover()   → remove Cliente
 */
public class ClientController extends HttpServlet implements Gerenciavel {

    private final ClientService clientService = new ClientService(new ClientDAO());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("userLogged");

        if (!(user instanceof Attendant)) {
            response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=access_denied");
            return;
        }

        String action = request.getParameter("action");
        String query  = request.getParameter("query");

        if ("profile".equals(action)) {
            String cpf = request.getParameter("cpf");
            try {
                Client client = clientService.findByCpf(cpf)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
                request.setAttribute("client", client);
                request.getRequestDispatcher("/view/attendant/clientProfile.jsp").forward(request, response);
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/clients?msg=not_found");
            }
            return;
        }

        if ("register".equals(action)) {
            request.getRequestDispatcher("/view/attendant/registerClient.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(action)) {
            String cpf = request.getParameter("cpf");
            try {
                Client client = clientService.findByCpf(cpf)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
                request.setAttribute("client", client);
                request.getRequestDispatcher("/view/attendant/editClient.jsp").forward(request, response);
            } catch (Exception e) {
                response.sendRedirect(request.getContextPath() + "/clients?msg=not_found");
            }
            return;
        }

        try {
            List<Client> clients;
            if (query != null && !query.trim().isEmpty()) {
                Client byExactCpf = clientService.findByCpf(query.trim()).orElse(null);
                if (byExactCpf != null) {
                    clients = List.of(byExactCpf);
                } else {
                    clients = clientService.findByName(query.trim());
                }
            } else {
                clients = clientService.findAll();
            }
            request.setAttribute("clients", clients);
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/view/attendant/clients.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("userLogged");

        if (!(user instanceof Attendant)) {
            response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=access_denied");
            return;
        }

        String action = request.getParameter("action");

        if ("register".equals(action)) {
            cadastrar(request, response);
        } else if ("update".equals(action)) {
            atualizar(request, response);
        } else if ("delete".equals(action)) {
            remover(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/clients");
        }
    }

    // POLIMORFISMO: cadastrar() operando sobre Cliente
    @Override
    public void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Client client = mapClientFromRequest(request);
            clientService.insert(client);
            response.sendRedirect(request.getContextPath() + "/clients?msg=register_success");
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath()
                + "/clients?msg=error&detail=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/clients?msg=error");
        }
    }

    // POLIMORFISMO: atualizar() operando sobre Cliente
    @Override
    public void atualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Client client = mapClientFromRequest(request);
            clientService.update(client);
            response.sendRedirect(request.getContextPath()
                + "/clients?action=profile&cpf=" + client.getCpf() + "&msg=update_success");
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath()
                + "/clients?msg=error&detail=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/clients?msg=error");
        }
    }

    // POLIMORFISMO: remover() operando sobre Cliente
    @Override
    public void remover(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cpf = request.getParameter("cpf");
            clientService.delete(cpf);
            response.sendRedirect(request.getContextPath() + "/clients?msg=delete_success");
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.sendRedirect(request.getContextPath()
                + "/clients?msg=error&detail=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/clients?msg=error");
        }
    }

    private Client mapClientFromRequest(HttpServletRequest request) {
        String cpf      = request.getParameter("cpf");
        String name     = request.getParameter("name");
        String email    = request.getParameter("email");
        char gender     = request.getParameter("gender").charAt(0);
        LocalDate birth = LocalDate.parse(request.getParameter("birthDate"));
        String address  = request.getParameter("address");
        return new Client(cpf, name, email, gender, birth, address);
    }
}