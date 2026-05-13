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
  <title>Atlas — Painel do Administrador</title>

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&display=swap" rel="stylesheet">

  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">

  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@24,400,0,0"/>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>

  <!-- Ferramentas -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/admTools.css"/>
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
            <a class="nav-link active"
               href="${pageContext.request.contextPath}/view/admin/adminHome.jsp">
              Início
            </a>
          </li>

          <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/view/admin/registerEmployee.jsp">
              Cadastrar
            </a>
          </li>

          <!-- ✅ GERENCIAR CORRIGIDO -->
          <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/manageEmployee">
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

        <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
          <i class="bi bi-box-arrow-right"></i> Sair
        </a>

      </div>
    </div>

  </nav>
</header>

<div class="container-fluid">
  <main class="col-12 pt-4 px-5">

    <section class="ferramentas-admin">

      <div class="ferramentas-titulo">
        <span>FERRAMENTAS</span>
        <h2>Veja o que você pode fazer</h2>
      </div>

      <div class="row justify-content-center text-center g-5">

        <div class="col-lg-4 col-md-6">
          <a href="${pageContext.request.contextPath}/view/admin/registerEmployee.jsp"
             class="tool-card">
            <div class="tool-icon">
              <span class="material-symbols-rounded">person_add</span>
            </div>
            <h5>Cadastrar funcionários</h5>
            <p>Cadastre novos funcionários no sistema Atlas.</p>
          </a>
        </div>

        <!-- ✅ GERENCIAR CORRIGIDO -->
        <div class="col-lg-4 col-md-6">
          <a href="${pageContext.request.contextPath}/manageEmployee"
             class="tool-card">
            <div class="tool-icon">
              <span class="material-symbols-rounded">manage_accounts</span>
            </div>
            <h5>Gerenciar funcionários</h5>
            <p>Gerencie edição e remoção de funcionários.</p>
          </a>
        </div>

        <div class="col-lg-4 col-md-6">
          <a href="${pageContext.request.contextPath}/view/admin/listEmployees.jsp"
             class="tool-card">
            <div class="tool-icon">
              <span class="material-symbols-rounded">groups</span>
            </div>
            <h5>Listar funcionários</h5>
            <p>Visualize todos os funcionários cadastrados.</p>
          </a>
        </div>

      </div>

    </section>

  </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>