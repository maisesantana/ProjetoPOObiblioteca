package br.com.atlas.controller;

// Importações das ferramentas do Java e das classes
import br.com.atlas.dao.ClientDAO;
import br.com.atlas.dao.PersonDAO;
import br.com.atlas.model.Client;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/registerClient")
public class ClientController extends HttpServlet {
	
	// O método doPost é chamado quando o formulário no JSP envia os dados via method="POST"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
      
    	/* * PARTE 1: CAPTURA DE DADOS
         * O objeto 'request' traz tudo o que o usuário digitou nas caixinhas do formulário.
         * O nome dentro do getParameter deve ser IGUAL ao 'name' do input no HTML.
         */
        String cpf = request.getParameter("cpf");
        String name = request.getParameter("name");
        String socialName = request.getParameter("socialName");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        
        // LocalDate.parse converte o texto da data (AAAA-MM-DD) para um objeto de data real do Java
        LocalDate birthDate = LocalDate.parse(request.getParameter("birthDate"));
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String rg = request.getParameter("rg");
        
        // Dados específicos do Cliente (isStudying, address, zipCode)
        boolean isStudying = request.getParameter("isStudying") != null;
        String address = request.getParameter("address");
        String zipCode = request.getParameter("zipCode");

        /*
         * PARTE 2: CRIAÇÃO DO OBJETO
         * Aqui a gente "empacota" todas as variáveis soltas dentro de um único objeto 'Client'.
         * Usamos o construtor feito no Model.
         */
        Client newClient = new Client(cpf, name, socialName, email, gender, 
                                    birthDate, password, phone, rg, 
                                    isStudying, address, zipCode);

        /*
         * PARTE 3: PERSISTÊNCIA (SALVAR NO BANCO)
         * Como Client herda de Person, precisamos de dois DAOs para salvar em duas tabelas diferentes.
         */
        PersonDAO personDao = new PersonDAO();
        ClientDAO clientDao = new ClientDAO();

        try {
            // Primeiro salvamos a "Pessoa" (Person)
            boolean personSaved = personDao.insert(newClient);
            
            if (personSaved) {
                // Se a pessoa foi salva, salvamos os detalhes do "Cliente" (Client)
                clientDao.insert(newClient);
                
                // Redirecionar para uma página de sucesso (ajustado para pastas em inglês se você as renomear)
                response.sendRedirect("views/clerk/success.jsp");
            } else {
                // Se falhou ao salvar a Pessoa, manda para uma página de erro
                response.sendRedirect("views/clerk/error.jsp?msg=person_save_error");
            }
        } catch (Exception e) {
            // Em caso de erro grave, imprime no console e redireciona
            e.printStackTrace();
            response.sendRedirect("views/clerk/error.jsp");
        }
    }
}