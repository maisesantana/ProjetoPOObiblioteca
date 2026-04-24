package br.com.atlas.controller;

import br.com.atlas.dao.ClientDAO;
import br.com.atlas.model.Client;
import br.com.atlas.util.ConnectionDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;

@WebServlet("/registerClient")
public class ClientController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // PARTE 1: CAPTURA DE DADOS
        String cpf     = request.getParameter("cpf");
        String name    = request.getParameter("name");
        String email   = request.getParameter("email");
        String gender  = request.getParameter("gender");
        String address = request.getParameter("address");
        LocalDate birthDate = LocalDate.parse(request.getParameter("birthDate"));

        // PARTE 2: CRIAÇÃO DO OBJETO
        Client newClient = new Client(cpf, name, email, gender, birthDate, address);

        // PARTE 3: PERSISTÊNCIA
        try (Connection connection = ConnectionDb.getConexao()) {

            if (connection == null) {
                response.sendRedirect("view/error.jsp");
                return;
            }

            ClientDAO clientDao = new ClientDAO(connection);

            // Verifica se CPF já existe antes de inserir
            if (clientDao.findByCpf(cpf) != null) {
                response.sendRedirect("view/error.jsp?msg=cpf_existente");
                return;
            }

            clientDao.insert(newClient);
            response.sendRedirect("view/success.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("view/error.jsp");
        }
    }
}