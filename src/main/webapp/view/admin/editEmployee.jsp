<%@ page import="br.com.atlas.model.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%
    Employee employeeFound = (Employee) request.getAttribute("employeeFound");
    if (employeeFound == null) {
        response.sendRedirect(request.getContextPath() + "/view/admin/manageEmployee.jsp?msg=error");
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Atualizar Funcionário</title>

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&display=swap" rel="stylesheet">

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/registerEmployee.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
</head>
<body>

<header>
  <nav class="navbar navbar-expand-lg atlas-navbar">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">
        <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas"/>
      </a>
      <button class="navbar-toggler" type="button"
              data-bs-toggle="collapse" data-bs-target="#navbarAtlas"
              aria-controls="navbarAtlas" aria-expanded="false"
              aria-label="Abrir menu">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarAtlas">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/view/admin/adminPanel.jsp">Início</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/view/admin/registerEmployee.jsp">Cadastrar</a>
          </li>
          <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/manageEmployee">Gerenciar</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/listEmployees">Listar</a>
          </li>
        </ul>
        <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
          <i class="bi bi-box-arrow-right"></i> Sair
        </a>
      </div>
    </div>
  </nav>
</header>

<div class="logo-header">
  <a href="${pageContext.request.contextPath}/view/admin/adminPanel.jsp">
    <img src="${pageContext.request.contextPath}/assets/images/logo.png"
         alt="Atlas - Gestão de Biblioteca"
         class="logo-atlas"/>
  </a> 
</div>

  <main class="container py-5">

    <nav class="mb-4">
      <a href="${pageContext.request.contextPath}/view/admin/adminPanel.jsp">Início</a>
      <span>/</span>
      <span>Editar funcionário</span>
    </nav>

    <div class="mb-4">
      <h1>Editar Funcionário</h1>
      <p class="text-muted">Atualize os dados do funcionário.</p>
    </div>

    <%
      String msg = request.getParameter("msg");
      if (msg != null && !msg.isBlank()) {
    %>
      <div class="alert alert-<%= "success".equals(msg) ? "success" : "danger" %>">
        <%= "success".equals(msg) ? "Alteração realizada com sucesso." : "Ocorreu um erro. Tente novamente." %>
      </div>
    <%
      }
    %>

    <form action="${pageContext.request.contextPath}/manageEmployee" method="post" novalidate class="row g-4">
      <input type="hidden" name="action" value="update"/>
      <input type="hidden" name="cpf" value="${employeeFound.cpf}"/>
      <input type="hidden" name="birthDate" value="${employeeFound.birthDate}"/>

      <div class="col-md-6">
        <label for="nome" class="form-label">Nome completo</label>
        <input type="text" id="nome" name="name" class="form-control" placeholder="Insira o nome" value="${employeeFound.name}"/>
      </div>

      <div class="col-12">
        <label class="form-label d-block">Sexo</label>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="gender" id="masculino" value="M" <%= employeeFound.getGender() == 'M' ? "checked" : "" %> />
          <label class="form-check-label" for="masculino">Masculino</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="gender" id="feminino" value="F" <%= employeeFound.getGender() == 'F' ? "checked" : "" %> />
          <label class="form-check-label" for="feminino">Feminino</label>
        </div>
      </div>

      <div class="col-md-6">
        <label for="email" class="form-label">Email</label>
        <input type="email" id="email" name="email" class="form-control" placeholder="Insira o e-mail" value="${employeeFound.email}"/>
      </div>

      <div class="col-12">
        <label class="form-label d-block">Função</label>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="role" id="atendente" value="atendente" <%= !(employeeFound instanceof Librarian) && !(employeeFound instanceof Administrator) ? "checked" : "" %> />
          <label class="form-check-label" for="atendente">Atendente</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="role" id="bibliotecario" value="bibliotecario" <%= employeeFound instanceof Librarian ? "checked" : "" %> />
          <label class="form-check-label" for="bibliotecario">Bibliotecário</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="role" id="administrador" value="administrador" <%= employeeFound instanceof Administrator ? "checked" : "" %> />
          <label class="form-check-label" for="administrador">Administrador</label>
        </div>
      </div>

      <div class="col-md-6">
        <label for="senha" class="form-label">Nova senha (opcional)</label>
        <div class="position-relative">
          <input type="password" id="senha" name="password" class="form-control" placeholder="Senha numérica" />
          <button type="button" class="btn btn-outline-secondary position-absolute end-0 top-50 translate-middle-y me-2" onclick="togglePwd('senha', this)" aria-label="Mostrar/ocultar senha">
            <i class="bi bi-eye-slash"></i>
          </button>
        </div>
      </div>

      <div class="col-md-6">
        <label for="confirmarSenha" class="form-label">Confirmar nova senha</label>
        <div class="position-relative">
          <input type="password" id="confirmarSenha" name="confirmPassword" class="form-control" placeholder="Senha numérica" />
          <button type="button" class="btn btn-outline-secondary position-absolute end-0 top-50 translate-middle-y me-2" onclick="togglePwd('confirmarSenha', this)" aria-label="Mostrar/ocultar confirmação">
            <i class="bi bi-eye-slash"></i>
          </button>
        </div>
      </div>

      <div class="col-12 d-grid gap-2">
        <button type="submit" class="btn btn-primary px-4">Atualizar funcionário</button>
        <a href="${pageContext.request.contextPath}/manageEmployee" class="btn btn-secondary px-4">Voltar</a>
      </div>
    </form>

  </main>
</div>

<jsp:include page="/view/footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
  function togglePwd(fieldId, btn) {
    const field = document.getElementById(fieldId);
    const icon  = btn.querySelector('i');
    if (field.type === 'password') {
      field.type = 'text';
      icon.classList.replace('bi-eye-slash', 'bi-eye');
    } else {
      field.type = 'password';
      icon.classList.replace('bi-eye', 'bi-eye-slash');
    }
  }
</script>

</body>
</html>