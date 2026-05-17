<%@ page import="br.com.atlas.model.Administrator" %>

<%
    Object user = session.getAttribute("userLogged");
    if (user == null || !(user instanceof Administrator)) {
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
        return;
    }
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Gerenciar Funcionário</title>
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
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarAtlas">
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
    <div class="logo-header">
      <a href="${pageContext.request.contextPath}/view/admin/adminPanel.jsp">
        <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas - Gestão de Biblioteca" class="logo-atlas"/>
      </a>
    </div>
  </header>

  <main class="container py-5">
    <nav class="mb-4">
      <a href="${pageContext.request.contextPath}/view/admin/adminPanel.jsp">Início</a>
      <span>/</span>
      <span>Gerenciar funcionário</span>
    </nav>

    <div class="mb-4">
      <h1>Gerenciar Funcionários</h1>
      <p class="text-muted">Use o CPF para localizar um funcionário e editar ou remover o cadastro.</p>
    </div>

    <%
      String msg = request.getParameter("msg");
      String debug = request.getParameter("debug");
      if (msg != null && !msg.isBlank()) {
        String alertClass = "danger";
        if ("removed".equals(msg)) {
          alertClass = "success";
        }
    %>
      <div class="alert alert-<%= alertClass %>">
        <%
          switch (msg) {
            case "not_found":
              out.print("Funcionário não encontrado.");
              break;
            case "error":
              out.print("Ocorreu um erro. Tente novamente.");
              break;
            case "removed":
              out.print("Funcionário removido com sucesso.");
              break;
            default:
              out.print(msg);
          }
          if (debug != null && !debug.isBlank()) {
              out.print("<br/>Detalhe: " + debug);
          }
        %>
      </div>
    <%
      }
    %>

    <form action="${pageContext.request.contextPath}/manageEmployee" method="get" class="row g-3 mb-4">
      <input type="hidden" name="action" value="search"/>
      <div class="col-md-8">
        <label for="cpf" class="form-label">CPF</label>
        <input type="text" id="cpf" name="cpf" class="form-control" placeholder="000.000.000-00" required/>
      </div>
      <div class="col-md-4 align-self-end">
        <button type="submit" class="btn btn-primary w-100">Buscar</button>
      </div>
    </form>

    <%
      Object employeeFound = request.getAttribute("employeeFound");
      if (employeeFound != null) {
        br.com.atlas.model.Employee employee = (br.com.atlas.model.Employee) employeeFound;
        String roleLabel = "Atendente";
        if (employee instanceof br.com.atlas.model.Administrator) {
          roleLabel = "Administrador";
        } else if (employee instanceof br.com.atlas.model.Librarian) {
          roleLabel = "Bibliotecário";
        }
    %>
      <div class="card">
        <div class="card-body">
          <h5 class="card-title">Funcionário encontrado</h5>
          <p class="card-text"><strong>CPF:</strong> <%= employee.getCpf() %></p>
          <p class="card-text"><strong>Nome:</strong> <%= employee.getName() %></p>
          <p class="card-text"><strong>Email:</strong> <%= employee.getEmail() %></p>
          <p class="card-text"><strong>Função:</strong> <%= roleLabel %></p>
          <a href="<%= request.getContextPath() %>/manageEmployee?action=edit&cpf=<%= employee.getCpf() %>" class="btn btn-warning me-2">Editar</a>
          <form action="<%= request.getContextPath() %>/manageEmployee" method="post" style="display:inline;">
            <input type="hidden" name="action" value="remove"/>
            <input type="hidden" name="cpf" value="<%= employee.getCpf() %>"/>
            <button type="submit" class="btn btn-danger">Remover</button>
          </form>
        </div>
      </div>
    <%
      }
    %>
  </main>

  <jsp:include page="/view/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
