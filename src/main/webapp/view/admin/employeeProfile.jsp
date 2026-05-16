<%@ page import="br.com.atlas.model.Administrator" %>
<%@ page import="br.com.atlas.model.Librarian" %>
<%@ page import="br.com.atlas.model.Attendant" %>
<%@ page import="br.com.atlas.model.Employee" %>

<%
    Object user = session.getAttribute("userLogged");

    if(user == null || !(user instanceof Administrator)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp");
        return;
    }

    Employee employee =
        (Employee) request.getAttribute("employee");

    /*
     Se não existir funcionário enviado pelo controller,
     volta para a listagem para evitar erro 500.
    */
    if(employee == null){
        response.sendRedirect(request.getContextPath() + "/listEmployees");
        return;
    }
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Perfil do Funcionário</title>

  <!-- GOOGLE FONTS -->
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

  <!-- INTER -->
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

  <!-- MERRIWEATHER -->
  <link href="https://fonts.googleapis.com/css2?family=Merriweather:wght@700&display=swap" rel="stylesheet">

  <!-- BOOTSTRAP -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

  <!-- BOOTSTRAP ICONS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>

  <!-- CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/employeeProfile.css"/>
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
</header>

<!-- CONTEÚDO -->
<main class="profile-page">
  <section class="profile-card">

    <!-- TOPO -->
    <div class="profile-top">

      <div class="logo-area">
        <div class="logo-box">
          <img src="${pageContext.request.contextPath}/assets/images/cuttedLogo.png" alt="Atlas"/>
        </div>
      </div>

      <div class="title-area">
        <h1>ATLAS</h1>
        <span>GESTÃO DE BIBLIOTECA</span>
      </div>

    </div>

    <!-- DADOS -->
    <div class="profile-info">

      <div class="info-group">
        <label>Nome funcionário</label>
        <p><%= employee.getName() %></p>
      </div>

      <div class="info-group">
        <label>Email</label>
        <p><%= employee.getEmail() %></p>
      </div>

      <div class="info-group">
        <label>CPF</label>
        <p><%= employee.getCpf() %></p>
      </div>

      <div class="info-group">
        <label>Cargo</label>
        <p>
          <% if(employee instanceof Administrator){ %>
              Administrador
          <% } else if(employee instanceof Librarian){ %>
              Bibliotecário
          <% } else if(employee instanceof Attendant){ %>
              Atendente
          <% } %>
        </p>
      </div>

      <div class="info-group">
        <label>Sexo</label>
        <p>
          <% 
              /*
               Verifica o sexo salvo no banco.
               O banco salva como CHAR maiúsculo:
               F = Feminino
               M = Masculino
              */
          %>

          <% char gender = Character.toUpperCase(employee.getGender()); %>
          <% if(gender == 'F'){ %>
              Feminino
          <% } else if(gender == 'M'){ %>
              Masculino
          <% } else { %>
              Não informado
          <% } %>
        </p>
      </div>

      <div class="info-group">
        <label>Data de nascimento</label>
        <p><%= employee.getBirthDate() %></p>
      </div>

    </div>
  </section>
</main>

<!-- FOOTER -->
<jsp:include page="/view/footer.jsp"/>

<!-- BOOTSTRAP -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>