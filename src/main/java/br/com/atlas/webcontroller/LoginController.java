package br.com.atlas.webcontroller;

import br.com.atlas.dao.LoginDAO;
import br.com.atlas.model.Administrator;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Librarian;
import br.com.atlas.util.ConnectionDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Captura os dados vindos do formulário (JSP)
        String cpf = request.getParameter("cpf");
        String senhaTexto = request.getParameter("password");

        // Validação básica de campos vazios
        if (cpf == null || cpf.trim().isEmpty() || senhaTexto == null || senhaTexto.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=empty_fields");
            return;
        }

        try (Connection conn = ConnectionDb.getConexao()) {
            // 2. Converte a senha para int (conforme o seu banco)
            int password = Integer.parseInt(senhaTexto);

            // 3. Chama o LoginDAO para fazer a busca em cascata
            LoginDAO loginDAO = new LoginDAO(conn);
            Optional<Employee> authUser = loginDAO.authenticate(cpf, password);

            if (authUser.isPresent()) {
                // LOGIN COM SUCESSO!
                Employee funcionario = authUser.get();

                // 4. Cria ou recupera a sessão e guarda o objeto logado
                HttpSession session = request.getSession();
                session.setAttribute("userLogged", funcionario);

                // 5. Redirecionamento baseado no cargo (Polimorfismo em ação!)
                // O Java descobre o tipo real do objeto aqui
                if (funcionario instanceof Administrator) {

                    // LOGS para diagnosticar (apenas UM if aqui!)
                    System.out.println("========== DIAGNÓSTICO ==========");
                    System.out.println("1. ContextPath: '" + request.getContextPath() + "'");
                    
                    String caminhoRedirecionamento = request.getContextPath() + "/view/admin/registerEmployee.jsp";
                    System.out.println("2. Tentando redirecionar para: " + caminhoRedirecionamento);
                    
                    // Tenta encontrar o arquivo fisicamente
                    String caminhoFisico = getServletContext().getRealPath("/view/admin/registerEmployee.jsp");
                    System.out.println("3. Caminho físico no servidor: " + caminhoFisico);
                    
                    // Verifica se o arquivo existe
                    java.io.File arquivo = new java.io.File(caminhoFisico);
                    System.out.println("4. O arquivo existe? " + arquivo.exists());
                    
                    // Lista o que tem na pasta /view
                    String pastaView = getServletContext().getRealPath("/view");
                    if(pastaView != null) {
                        java.io.File dirView = new java.io.File(pastaView);
                        if(dirView.exists() && dirView.isDirectory()) {
                            System.out.println("5. Conteúdo da pasta /view:");
                            for(String item : dirView.list()) {
                                System.out.println("   - " + item);
                            }
                        } else {
                            System.out.println("5. Pasta /view NÃO EXISTE!");
                        }
                    } else {
                        System.out.println("5. pastaView é NULL - o diretório /view não existe no contexto!");
                    }
                    
                    response.sendRedirect(caminhoRedirecionamento);

                } else if (funcionario instanceof Librarian) {

                    response.sendRedirect(
                        request.getContextPath()
                        + "/view/librarian/inventory.jsp"
                    );

                } else {

                    response.sendRedirect(
                        request.getContextPath()
                        + "/view/attendant/loan_panel.jsp"
                    );
                }

            } else {
                // Senha errada ou CPF não encontrado em nenhuma tabela
                response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=invalid_credentials");
            }

        } catch (NumberFormatException e) {
            // Caso o usuário digite letras onde deveria ser senha numérica
            response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=password_must_be_number");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=internal_error");
        }
    }

    /**
     * Opcional: Método para Logout
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate(); // Mata a sessão (destrói o crachá)
        response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=logged_out");
    }
}