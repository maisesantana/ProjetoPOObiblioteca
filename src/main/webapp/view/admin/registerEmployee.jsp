<%@ page import="br.com.atlas.model.Administrator" %>

<%
    Object user = session.getAttribute("userLogged");

    if(user == null || !(user instanceof Administrator)){

        response.sendRedirect(
            request.getContextPath() + "/view/login.jsp"
        );

        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Atlas - Cadastrar Funcionário</title>
</head>
<body>

  <header>
    <a href="#">
      <img src="logo.png" alt="Atlas - Gestão de Biblioteca" />
    </a>
  </header>

  <main>
    <nav>
      <a href="#">início</a>
      <span>/</span>
      <span>Cadastrar funcionário</span>
    </nav>

    <h1>Cadastrar Funcionário</h1>
    <p>Cadastra-se para acessar.</p>

    <%
        String status = request.getParameter("status");
        String msg = request.getParameter("msg");
        if ("success".equals(status)) {
    %>
        <div style="color: green;">Funcionário cadastrado com sucesso.</div>
    <% } else if (msg != null) { %>
        <div style="color: red;">
            <% if ("erro_ao_cadastrar_funcionario".equals(msg)) { %>
                Erro ao cadastrar funcionário. Verifique os dados e tente novamente.
            <% } else if ("empty_fields".equals(msg)) { %>
                Preencha todos os campos antes de enviar.
            <% } else if ("password_mismatch".equals(msg)) { %>
                As senhas não conferem.
            <% } else if ("password_format_error".equals(msg)) { %>
                A senha deve ser numérica.
            <% } else if ("birthdate_format_error".equals(msg)) { %>
                A data de nascimento deve estar no formato correto.
            <% } else if ("invalid_gender".equals(msg)) { %>
                Sexo inválido selecionado.
            <% } else if ("invalid_role".equals(msg)) { %>
                Cargo inválido selecionado.
            <% } else { %>
                Erro: <%= msg %>
            <% } %>
        </div>
    <% } %>

    <%-- action bate com @WebServlet("/manageEmployee") --%>
    <form action="<%= request.getContextPath() %>/manageEmployee" method="post">

      <div>
        <label for="nome">Nome completo</label>
        <input type="text" id="nome" name="name" placeholder="insira o nome" required />
      </div>

      <div>
        <label for="cpf">CPF</label>
        <input type="text" id="cpf" name="cpf" placeholder="*** *** ***-**" maxlength="14" required />
      </div>

      <div>
        <label for="email">Email</label>
        <input type="email" id="email" name="email" placeholder="insira o e-mail" required />
      </div>

      <div>
        <label for="dataNascimento">Data de nascimento</label>
        <input type="date" id="dataNascimento" name="birthDate" required />
      </div>

      <fieldset>
        <legend>Sexo</legend>
        <label>
          <%-- name="gender" já estava certo --%>
          <input type="radio" name="gender" value="masculino" checked /> Masculino
        </label>
        <label>
          <input type="radio" name="gender" value="feminino" /> Feminino
        </label>
        <label>
          <input type="radio" name="gender" value="outro" /> Outro
        </label>
      </fieldset>

      <div>
        <label for="senha">Criar senha</label>
        <%-- senha numérica pois o Servlet faz Integer.parseInt() --%>
        <input type="password" id="senha" name="password" placeholder="senha numérica" pattern="\d*" required />
      </div>

      <div>
        <label for="confirmarSenha">Confirmar senha</label>
        <%-- name="confirmPassword" bate com getParameter("confirmPassword") --%>
        <input type="password" id="confirmarSenha" name="confirmPassword" placeholder="repita a senha numérica" pattern="\d*" required />
      </div>

      <fieldset>
        <legend>Cargo</legend>
        <%-- name="role" bate com getParameter("role") --%>
        <label>
          <input type="radio" name="role" value="atendente" checked /> Atendente
        </label>
        <label>
          <input type="radio" name="role" value="bibliotecario" /> Bibliotecário
        </label>
      </fieldset>

      <button type="submit">Finalizar cadastro</button>

    </form>
  </main>

  <footer>
    <p>© 2026 Atlas. Todos os direitos reservados.</p>
  </footer>

</body>
</html>