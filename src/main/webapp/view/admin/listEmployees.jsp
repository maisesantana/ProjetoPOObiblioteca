<%@ page import="br.com.atlas.model.Administrator" %>

<%
    Object user = session.getAttribute("userLogged");

    if(user == null || !(user instanceof Administrator)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp");
        return;
    }
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.List" %>
<%@ page import="br.com.atlas.model.Employee" %>
<%@ page import="br.com.atlas.model.Administrator" %>
<%@ page import="br.com.atlas.model.Attendant" %>
<%@ page import="br.com.atlas.model.Librarian" %>
<!--pega a lista enviada pelo controller-->
<% List<Employee> employees = (List<Employee>) request.getAttribute("employees"); %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Listar Funcionários</title>

  <!-- Fonte -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

  <!-- Bootstrap Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>

  <!-- CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/listEmployees.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
</head>

<body>

  <!-- HEADER -->
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
            <a class="nav-link"
               href="${pageContext.request.contextPath}/view/admin/adminPanel.jsp">
              Início
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/view/admin/registerEmployee.jsp">
              Cadastrar
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/manageEmployee">
              Gerenciar
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link active"
              href="${pageContext.request.contextPath}/listEmployees">
                Listar
            </a>
          </li>
        </ul>
        <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
          <i class="bi bi-box-arrow-right"></i> Sair
        </a>
      </div>
    </div>
  </nav>
    <div class="header-logo">
    <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas - Gestão de Biblioteca"/>
    </div>
  </header>

  <!-- CONTEÚDO -->
  <main class="container-fluid employees-container">

    <!-- Breadcrumb -->
    <nav class="breadcrumb-custom">
      <a href="${pageContext.request.contextPath}/view/admin/adminPanel.jsp">
        Início
      </a>
      <span>/</span>
      <span>
        Funcionários
      </span>
    </nav>

    <!-- Título -->
    <section class="titulo-section">
      <h1>
        Visualizar todos os funcionários registrados no sistema
      </h1>
      <p>
        Lista de funcionários
      </p>
    </section>

    <!-- LISTA -->
    <section class="employees-list">
    <% if(employees != null && !employees.isEmpty()){ %>
        <% for(Employee emp : employees){ %>
            <div class="employee-card">
            <div class="employee-photo">
                <i class="bi bi-person"></i>
            </div>
            <div class="employee-info">
                <h3>
                <%= emp.getName() %>
                </h3>
                <span>
                <% if(emp instanceof Administrator){ %>
                    Administrador
                <% } else if(emp instanceof Librarian){ %>
                    Bibliotecário
                <% } else { %>
                    Atendente
                <% } %>
                </span>
            </div>
           <a href="${pageContext.request.contextPath}/listEmployees?cpf=<%= URLEncoder.encode(emp.getCpf(), "UTF-8") %>" class="employee-view">
              Ver
            </a>
            </div>
        <% } %>
        <% } else { %>
                <p>
                    Nenhum funcionário encontrado.
                </p>
        <% } %>
    </section>
  </main>

  <!-- FOOTER -->
  <jsp:include page="/view/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>