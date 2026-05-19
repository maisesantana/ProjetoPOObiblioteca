<%@ page import="br.com.atlas.model.Attendant" %>
<%@ page import="br.com.atlas.model.Client" %>
<%@ page import="java.util.List" %>

<%
    Object user = session.getAttribute("userLogged");
    if(user == null || !(user instanceof Attendant)){
        response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=unauthorized");
        return;
    }
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    String msg = request.getParameter("msg");
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Clientes</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/attendant/clients.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
</head>
<body>
  <header>
    <nav class="navbar navbar-expand-lg atlas-navbar">
      <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">
          <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas"/>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarAtlas">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarAtlas">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">Início</a></li>
            <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/clients">Clientes</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/loan.jsp">Empréstimo</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/returnBook.jsp">Devolução</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/renewal.jsp">Renovação</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/searchBooks">Buscar Livros</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/activeLoans.jsp">Empréstimos Ativos</a></li>
          </ul>
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair"><i class="bi bi-box-arrow-right"></i> Sair</a>
        </div>
      </div>
    </nav>
  </header>

  <main class="container-fluid clients-container">
    <nav class="breadcrumb-custom">
      <a href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">Início</a>
      <span>/</span>
      <span class="current">Clientes</span>
    </nav>
    <section class="titulo-section">
      <h1>Gerenciar Clientes</h1>
      <p>Busque, cadastre e gerencie os clientes da biblioteca</p>
    </section>

    <% if ("register_success".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg"><i class="bi bi-check-circle-fill"></i> Cliente cadastrado com sucesso!</div>
    <% } else if ("delete_success".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg"><i class="bi bi-check-circle-fill"></i> Cliente removido com sucesso!</div>
    <% } else if ("update_success".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg"><i class="bi bi-check-circle-fill"></i> Cliente atualizado com sucesso!</div>
    <% } else if ("not_found".equals(msg)) { %>
      <div class="toast-msg toast-error show" id="toastMsg"><i class="bi bi-x-circle-fill"></i> Cliente não encontrado.</div>
    <% } else if ("error".equals(msg)) { %>
      <div class="toast-msg toast-error show" id="toastMsg"><i class="bi bi-x-circle-fill"></i> Ocorreu um erro. Tente novamente.</div>
    <% } %>

    <div class="search-register">
      <form action="${pageContext.request.contextPath}/clients" method="get">
        <input type="text" name="query" placeholder="Buscar por nome ou CPF..."
               value="${param.query != null ? param.query : ''}">
        <button type="submit" class="btn-search">Buscar</button>
      </form>
      <a href="${pageContext.request.contextPath}/clients?action=register" class="btn-register">
        <i class="bi bi-person-plus"></i> Cadastrar
      </a>
    </div>

    <div class="clients-list">
      <% if (clients != null && !clients.isEmpty()) { %>
        <% for (Client client : clients) { %>
          <div class="client-card">
            <div class="client-icon"><i class="bi bi-person"></i></div>
            <div class="client-info">
              <div>
                <div class="client-name"><%= client.getName() %></div>
                <div class="client-cpf">CPF: <%= client.getCpf() %></div>
              </div>
              <div class="client-actions">
                <% if (client.isSuspended()) { %>
                  <span class="client-status-suspended">Suspenso</span>
                <% } else { %>
                  <span class="client-status-ok">Ativo</span>
                <% } %>
                <a href="${pageContext.request.contextPath}/clients?action=profile&cpf=<%= client.getCpf() %>" class="btn-view">Ver</a>
              </div>
            </div>
          </div>
        <% } %>
      <% } else if (clients != null) { %>
        <p class="text-muted text-center py-4">Nenhum cliente encontrado.</p>
      <% } else { %>
        <p class="text-muted text-center py-4">Nenhum cliente cadastrado ainda.</p>
      <% } %>
    </div>
  </main>

  <jsp:include page="/view/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    const toast = document.getElementById('toastMsg');
    if (toast) setTimeout(() => { toast.style.opacity='0'; toast.style.transition='opacity 0.5s'; setTimeout(()=>toast.style.display='none',500); }, 4000);
  </script>
</body>
</html>
