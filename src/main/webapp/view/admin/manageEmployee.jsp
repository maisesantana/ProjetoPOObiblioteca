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
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/registerEmployee.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/manageEmployee.css"/>
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
            case "cannot_remove_self":
              out.print("Você não pode remover sua própria conta.");
              break;
            case "cannot_remove_admin":
              out.print("Não é permitido remover outro administrador.");
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

    <form action="${pageContext.request.contextPath}/manageEmployee" method="get" class="mb-4" style="max-width: 360px;">
      <input type="hidden" name="action" value="search"/>
      <div class="mb-3">
        <label for="cpf" class="form-label">CPF</label>
        <input type="text" id="cpf" name="cpf" class="form-control" placeholder="00000000000" required maxlength="11" pattern="\d{11}"/>
      </div>
      <button type="submit" class="btn btn-primary w-100">Buscar</button>
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

      <div class="employee-card">

        <div class="d-flex align-items-center gap-3">
          <div class="employee-avatar">
            <i class="bi bi-person-fill"></i>
          </div>
          <div>
            <p class="employee-name"><%= employee.getName() %></p>
            <p class="employee-role"><%= roleLabel %></p>
          </div>
        </div>

        <div class="info-row">
          <div class="info-item">
            <span class="info-label">CPF</span>
            <span class="info-value"><%= employee.getCpf() %></span>
          </div>
          <div class="info-item">
            <span class="info-label">E-mail</span>
            <span class="info-value"><%= employee.getEmail() %></span>
          </div>
        </div>

        <div class="employee-actions">
          <a href="<%= request.getContextPath() %>/manageEmployee?action=edit&cpf=<%= employee.getCpf() %>" class="btn-edit-emp">
            Atualizar funcionário
          </a>
          <% if (!(employee instanceof br.com.atlas.model.Administrator)) { %>
            <button type="button" class="btn-remove-emp" data-bs-toggle="modal" data-bs-target="#modalRemover">
              Remover funcionário
            </button>
          <% } %>
        </div>
      </div>

      <div class="modal fade" id="modalRemover" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Confirmar remoção</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
              Tem certeza que deseja remover <strong><%= employee.getName() %></strong>?
              <br/><span class="text-muted">Esta ação não pode ser desfeita.</span>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
              <form action="<%= request.getContextPath() %>/manageEmployee" method="post" style="display:inline;">
                <input type="hidden" name="action" value="remove"/>
                <input type="hidden" name="cpf" value="<%= employee.getCpf() %>"/>
                <button type="submit" class="btn btn-danger">Sim, remover</button>
              </form>
            </div>
          </div>
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