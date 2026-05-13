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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/listEmployees.css"/>
</head>

<body>

  <!-- HEADER -->
  <header class="header-logo">
    <img src="${pageContext.request.contextPath}/assets/images/logo.png"
         alt="Atlas - Gestão de Biblioteca"/>
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
        Resultado da busca de funcionário
      </h1>
      <p>
        Lista de funcionários
      </p>
    </section>

    <!-- LISTA -->
    <section class="employees-list">

      <!-- CARD -->
      <div class="employee-card">
        <div class="employee-photo">
          <i class="bi bi-person"></i>
        </div>

        <div class="employee-info">
          <span class="employee-name">
            Nome: Érica Ellen
          </span>

          <span class="employee-role">
            Administradora
          </span>

          <a href="#"
             class="employee-view">
            Ver
          </a>
        </div>
      </div>

      <!-- CARD -->
      <div class="employee-card">
        <div class="employee-photo">
          <i class="bi bi-person"></i>
        </div>

        <div class="employee-info">
          <span class="employee-name">
            Nome: Maise
          </span>

          <span class="employee-role">
            Atendente
          </span>

          <a href="#"
             class="employee-view">
            Ver
          </a>
        </div>
      </div>

      <!-- CARD -->
      <div class="employee-card">
        <div class="employee-photo">
          <i class="bi bi-person"></i>
        </div>

        <div class="employee-info">
          <span class="employee-name">
            Nome: Miqueias
          </span>

          <span class="employee-role">
            Bibliotecário
          </span>

          <a href="#"
             class="employee-view">
            Ver
          </a>
        </div>
      </div>

      <!-- CARD -->
      <div class="employee-card">
        <div class="employee-photo">
          <i class="bi bi-person"></i>
        </div>

        <div class="employee-info">
          <span class="employee-name">
            Nome: Rayane
          </span>

          <span class="employee-role">
            Atendente
          </span>

          <a href="#"
             class="employee-view">
            Ver
          </a>
        </div>
      </div>

    </section>

    <!-- PAGINAÇÃO -->
    <div class="pagination-custom">
      <button>
        <i class="bi bi-chevron-left"></i>
      </button>

      <span>
        1/3
      </span>

      <button>
        <i class="bi bi-chevron-right"></i>
      </button>
    </div>
  </main>
  
  <!-- FOOTER -->
  <footer class="footer-custom">
    <p>
      © 2026 Atlas. Todos os direitos reservados.
    </p>
  </footer>
</body>
</html>