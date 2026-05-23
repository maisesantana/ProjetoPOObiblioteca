<%@ page import="br.com.atlas.model.Attendant" %>

<%
    Object user = session.getAttribute("userLogged");
    if(user == null || !(user instanceof Attendant)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
        return;
    }
    Attendant attendant = (Attendant) user;
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas — Painel do Atendente</title>

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@24,400,0,0"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/admTools.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
</head>

<body>

  <header>
    <nav class="navbar navbar-expand-lg atlas-navbar">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">
          <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas — Gestão de Biblioteca"/>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarAtlas">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarAtlas">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a class="nav-link active" href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">Início</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.request.contextPath}/clients">Clientes</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/loan.jsp">Empréstimo</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/returnBook.jsp">Devolução</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/renewal.jsp">Renovação</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.request.contextPath}/searchBooks">Buscar Livros</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/activeLoans.jsp">Empréstimos Ativos</a>
            </li>
          </ul>
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
            <i class="bi bi-box-arrow-right"></i> Sair
          </a>
        </div>
      </div>
    </nav>
  </header>

  <%-- Mensagens de feedback --%>
  <% String msg = request.getParameter("msg"); %>
  <% if (msg != null) { %>
    <div class="container mt-3">
      <% if ("loan_ok".equals(msg)) { %>
        <div class="alert alert-success alert-dismissible fade show">Empréstimo registrado com sucesso! <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
      <% } else if ("return_ok".equals(msg)) { %>
        <div class="alert alert-success alert-dismissible fade show">Devolução registrada com sucesso! <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
      <% } else if ("renewal_ok".equals(msg)) { %>
        <div class="alert alert-success alert-dismissible fade show">Renovação registrada com sucesso! <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
      <% } else if ("success".equals(msg)) { %>
        <div class="alert alert-success alert-dismissible fade show">Operação realizada com sucesso! <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
      <% } else if (msg.contains("error")) { %>
        <div class="alert alert-danger alert-dismissible fade show">Erro: <%= request.getParameter("detail") != null ? request.getParameter("detail") : "Tente novamente." %> <button type="button" class="btn-close" data-bs-dismiss="alert"></button></div>
      <% } %>
    </div>
  <% } %>

  <div class="container-fluid">
    <div class="row">
      <main class="col-12 pt-4 px-5">
        <section class="ferramentas-admin">

          <div class="ferramentas-titulo">
            <span>ATENDENTE</span>
            <h2>Olá, <%= attendant.getName() %>! O que deseja fazer?</h2>
          </div>

          <div class="row justify-content-center text-center g-5">

            <div class="col-lg-4 col-md-6">
              <a href="${pageContext.request.contextPath}/clients" class="tool-card">
                <div class="tool-icon">
                  <span class="material-symbols-rounded">group</span>
                </div>
                <h5>Gerenciar Clientes</h5>
                <p>Cadastre, edite e consulte clientes.</p>
              </a>
            </div>

            <div class="col-lg-4 col-md-6">
              <a href="${pageContext.request.contextPath}/view/attendant/loan.jsp" class="tool-card">
                <div class="tool-icon">
                  <span class="material-symbols-rounded">book_ribbon</span>
                </div>
                <h5>Registrar Empréstimo</h5>
                <p>Registre novos empréstimos de livros.</p>
              </a>
            </div>

            <div class="col-lg-4 col-md-6">
              <a href="${pageContext.request.contextPath}/view/attendant/returnBook.jsp" class="tool-card">
                <div class="tool-icon">
                  <span class="material-symbols-rounded">assignment_return</span>
                </div>
                <h5>Registrar Devolução</h5>
                <p>Registre a devolução de um livro emprestado.</p>
              </a>
            </div>

            <div class="col-lg-4 col-md-6">
              <a href="${pageContext.request.contextPath}/view/attendant/renewal.jsp" class="tool-card">
                <div class="tool-icon">
                  <span class="material-symbols-rounded">autorenew</span>
                </div>
                <h5>Registrar Renovação</h5>
                <p>Renove o prazo de um empréstimo ativo.</p>
              </a>
            </div>

            <div class="col-lg-4 col-md-6">
              <a href="${pageContext.request.contextPath}/searchBooks" class="tool-card">
                <div class="tool-icon">
                  <span class="material-symbols-rounded">search</span>
                </div>
                <h5>Pesquisar Livros</h5>
                <p>Consulte livros disponíveis no acervo.</p>
              </a>
            </div>

            <div class="col-lg-4 col-md-6">
              <a href="${pageContext.request.contextPath}/view/attendant/activeLoans.jsp" class="tool-card">
                <div class="tool-icon">
                  <span class="material-symbols-rounded">pending_actions</span>
                </div>
                <h5>Empréstimos Ativos</h5>
                <p>Visualize todos os empréstimos em aberto.</p>
              </a>
            </div>

          </div>
        </section>
      </main>
    </div>
  </div>
  <jsp:include page="/view/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
