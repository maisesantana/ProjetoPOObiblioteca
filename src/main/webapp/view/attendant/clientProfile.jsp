<%@ page import="br.com.atlas.model.Attendant" %>
<%@ page import="br.com.atlas.model.Client" %>

<%
    Object user = session.getAttribute("userLogged");
    if(user == null || !(user instanceof Attendant)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
        return;
    }
    Client client = (Client) request.getAttribute("client");
    if (client == null) {
        response.sendRedirect(request.getContextPath() + "/clients");
        return;
    }
    String msg = request.getParameter("msg");
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Perfil do Cliente</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/attendant/clientProfile.css"/>
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
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.request.contextPath}/activeLoans">Empréstimos Ativos</a>
            </li>
          </ul>
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair"><i class="bi bi-box-arrow-right"></i> Sair</a>
        </div>
      </div>
    </nav>
  </header>

  <main class="container-fluid profile-container">
    <nav class="breadcrumb-custom">
      <a href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">Início</a>
      <span>/</span>
      <a href="${pageContext.request.contextPath}/clients">Clientes</a>
      <span>/</span>
      <span><%= client.getName() %></span>
    </nav>

    <% if ("update_success".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg"><i class="bi bi-check-circle-fill"></i> Cliente atualizado com sucesso!</div>
    <% } %>

    <div class="profile-header">
      <div class="profile-avatar"><i class="bi bi-person"></i></div>
      <div>
        <div class="profile-name"><%= client.getName() %></div>
        <% if (client.isSuspended()) { %>
          <span class="profile-status-suspended">Suspenso até <%= client.getEndSuspensionDate() %></span>
        <% } else { %>
          <span class="profile-status-ok">Ativo</span>
        <% } %>
      </div>
    </div>

    <div class="details-grid">
      <div class="detail-card"><div class="label">CPF</div><div class="value"><%= client.getCpf() %></div></div>
      <div class="detail-card"><div class="label">Email</div><div class="value"><%= client.getEmail() %></div></div>
      <div class="detail-card"><div class="label">Gênero</div><div class="value"><%= client.getGender() == 'M' ? "Masculino" : client.getGender() == 'F' ? "Feminino" : "Outro" %></div></div>
      <div class="detail-card"><div class="label">Data de Nascimento</div><div class="value"><%= client.getBirthDate() %></div></div>
      <div class="detail-card full-width"><div class="label">Endereço</div><div class="value"><%= client.getAddress() %></div></div>
    </div>

    <div class="action-btns">
      <a href="${pageContext.request.contextPath}/clients?action=edit&cpf=<%= client.getCpf() %>" class="btn-edit-profile">
        <i class="bi bi-pencil"></i> Editar
      </a>
      <form action="${pageContext.request.contextPath}/clients" method="post"
            class="form-delete-client"
            data-name="<%= client.getName() %>">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="cpf" value="<%= client.getCpf() %>">
        <button type="submit" class="btn-delete-profile"><i class="bi bi-trash"></i> Remover</button>
      </form>
    </div>

    <a href="${pageContext.request.contextPath}/clients" class="btn-back">
      <i class="bi bi-arrow-left"></i> Voltar para clientes
    </a>
  </main>

  <jsp:include page="/view/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    document.addEventListener("DOMContentLoaded", function() {
      const toast = document.getElementById('toastMsg');
      if (toast) setTimeout(() => { toast.style.opacity='0'; toast.style.transition='opacity 0.5s'; setTimeout(()=>toast.style.display='none',500); }, 4000);

      document.querySelectorAll('.form-delete-client').forEach(form => {
        form.addEventListener('submit', function(e) {
          if (!confirm('Tem certeza que deseja remover o cliente ' + this.getAttribute('data-name') + '?')) {
            e.preventDefault();
          }
        });
      });
    });
  </script>
</body>
</html>
