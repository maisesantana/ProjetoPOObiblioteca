<%@ page import="br.com.atlas.model.Administrator" %>

<%
    Object user = session.getAttribute("userLogged");

    if(user == null || !(user instanceof Administrator)){
        response.sendRedirect(request.getContextPath() + "/view/login.jsp");
        return;
    }
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Cadastrar Funcionário</title>

  <!-- Fonte menu -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&display=swap" rel="stylesheet">

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

  <!-- Bootstrap Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>

  <!-- Navbar -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/navbarAdm.css"/>

  <!-- CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/registerEmployee.css"/>
</head>

<body>

  <!-- NAVBAR -->
  <header>
    <nav class="navbar navbar-expand-lg atlas-navbar">
      <div class="container-fluid">

        <!-- Logo -->
        <a class="navbar-brand" href="#">
          <img src="${pageContext.request.contextPath}/assets/images/logo.png"
               alt="Atlas — Gestão de Biblioteca"/>
        </a>

        <!-- Toggler mobile -->
        <button class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarAtlas"
                aria-controls="navbarAtlas"
                aria-expanded="false"
                aria-label="Alternar navegação">
          <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Menu -->
        <div class="collapse navbar-collapse" id="navbarAtlas">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">

            <li class="nav-item">
              <a class="nav-link"
                 href="${pageContext.request.contextPath}/view/admin/adminPanel.jsp">
                Início
              </a>
            </li>

            <li class="nav-item">
              <a class="nav-link active"
                 aria-current="page"
                 href="${pageContext.request.contextPath}/view/admin/registerEmployee.jsp">
                Cadastrar
              </a>
            </li>

            <li class="nav-item">
              <a class="nav-link"
                 href="${pageContext.request.contextPath}/view/admin/editEmployee.jsp">
                Gerenciar
              </a>
            </li>

            <li class="nav-item">
              <a class="nav-link"
                 href="${pageContext.request.contextPath}/view/admin/listEmployees.jsp">
                Listar
              </a>
            </li>

          </ul>

          <!-- Botão sair -->
          <a href="${pageContext.request.contextPath}/logout"
             class="btn-sair">
            <i class="bi bi-box-arrow-right"></i>
            Sair
          </a>

        </div>
      </div>
    </nav>
  </header>

  <!-- CONTEÚDO -->
  <main class="container py-5">

    <!-- Breadcrumb -->
    <nav class="mb-4">
      <a href="${pageContext.request.contextPath}/view/admin/adminPanel.jsp">
        Início
      </a>

      <span>/</span>

      <span>
        Cadastrar funcionário
      </span>
    </nav>

    <!-- Título -->
    <div class="mb-4">
      <h1>
        Cadastrar Funcionário
      </h1>

      <p class="text-muted">
        Cadastre novos funcionários no sistema Atlas.
      </p>
    </div>

    <!-- Alertas -->
    <%
        String status = request.getParameter("status");
        String msg = request.getParameter("msg");

        if ("success".equals(status)) {
    %>

      <div class="alert alert-success">
        Funcionário cadastrado com sucesso.
      </div>

    <% } else if (msg != null) { %>

      <div class="alert alert-danger">

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

    <!-- Form -->
    <form action="<%= request.getContextPath() %>/manageEmployee"
          method="post"
          class="row g-4">

      <div class="col-md-6">
        <label for="nome"
               class="form-label">
          Nome completo
        </label>

        <input type="text"
               id="nome"
               name="name"
               class="form-control"
               placeholder="Insira o nome"
               required/>
      </div>

      <div class="col-md-6">
        <label for="cpf"
               class="form-label">
          CPF
        </label>

        <input type="text"
               id="cpf"
               name="cpf"
               class="form-control"
               placeholder="000.000.000-00"
               maxlength="14"
               required/>
      </div>

      <div class="col-md-6">
        <label for="email"
               class="form-label">
          Email
        </label>

        <input type="email"
               id="email"
               name="email"
               class="form-control"
               placeholder="Insira o e-mail"
               required/>
      </div>

      <div class="col-md-6">
        <label for="dataNascimento"
               class="form-label">
          Data de nascimento
        </label>

        <input type="date"
               id="dataNascimento"
               name="birthDate"
               class="form-control"
               required/>
      </div>

      <div class="col-12">
        <label class="form-label d-block">
          Sexo
        </label>

        <div class="form-check form-check-inline">
          <input class="form-check-input"
                 type="radio"
                 name="gender"
                 value="masculino"
                 checked>

          <label class="form-check-label">
            Masculino
          </label>
        </div>

        <div class="form-check form-check-inline">
          <input class="form-check-input"
                 type="radio"
                 name="gender"
                 value="feminino">

          <label class="form-check-label">
            Feminino
          </label>
        </div>

        <div class="form-check form-check-inline">
          <input class="form-check-input"
                 type="radio"
                 name="gender"
                 value="outro">

          <label class="form-check-label">
            Outro
          </label>
        </div>
      </div>

      <div class="col-md-6">
        <label for="senha"
               class="form-label">
          Criar senha
        </label>

        <input type="password"
               id="senha"
               name="password"
               class="form-control"
               placeholder="Senha numérica"
               pattern="\d*"
               required/>
      </div>

      <div class="col-md-6">
        <label for="confirmarSenha"
               class="form-label">
          Confirmar senha
        </label>

        <input type="password"
               id="confirmarSenha"
               name="confirmPassword"
               class="form-control"
               placeholder="Repita a senha"
               pattern="\d*"
               required/>
      </div>

      <div class="col-12">
        <label class="form-label d-block">
          Cargo
        </label>

        <div class="form-check form-check-inline">
          <input class="form-check-input"
                 type="radio"
                 name="role"
                 value="atendente"
                 checked>

          <label class="form-check-label">
            Atendente
          </label>
        </div>

        <div class="form-check form-check-inline">
          <input class="form-check-input"
                 type="radio"
                 name="role"
                 value="bibliotecario">

          <label class="form-check-label">
            Bibliotecário
          </label>
        </div>

        <div class="form-check form-check-inline">
          <input class="form-check-input"
                 type="radio"
                 name="role"
                 value="administrador">

          <label class="form-check-label">
            Administrador
          </label>
        </div>
      </div>

      <div class="col-12">
        <button type="submit"
                class="btn btn-primary px-4">
          Finalizar cadastro
        </button>
      </div>

    </form>

  </main>

  <!-- Bootstrap JS -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>