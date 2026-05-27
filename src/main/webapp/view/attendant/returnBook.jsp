<%@ page import="br.com.atlas.model.Attendant" %>
<%@ page import="br.com.atlas.model.Client" %>
<%@ page import="br.com.atlas.model.Loan" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>

<%
    Object user = session.getAttribute("userLogged");
    if(user == null || !(user instanceof Attendant)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
        return;
    }

    Client client    = (Client) request.getAttribute("client");
    List<Loan> loans = (List<Loan>) request.getAttribute("loans");
    String errorMsg  = (String) request.getAttribute("errorMsg");
    String msg       = request.getParameter("msg");
    String cpfParam  = request.getParameter("cpf");
    String type      = request.getParameter("type"); // "return" ou "renewal"
    if (type == null) type = "return";

    boolean isReturn  = "return".equals(type);
    String actionUrl  = isReturn
        ? request.getContextPath() + "/returnBook"
        : request.getContextPath() + "/renewal";
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas — <%= isReturn ? "Devolução" : "Renovação" %></title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/attendant/returnBook.css"/>
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
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/clients">Clientes</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/loan.jsp">Empréstimo</a></li>
            <li class="nav-item"><a class="nav-link <%= isReturn ? "active" : "" %>" href="${pageContext.request.contextPath}/returnBook?type=return">Devolução</a></li>
            <li class="nav-item"><a class="nav-link <%= !isReturn ? "active" : "" %>" href="${pageContext.request.contextPath}/returnBook?type=renewal">Renovação</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/searchBooks">Buscar Livros</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/activeLoans.jsp">Empréstimos Ativos</a></li>
          </ul>
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
            <i class="bi bi-box-arrow-right"></i> Sair
          </a>
        </div>
      </div>
    </nav>
  </header>

  <main class="container-fluid return-container">

    <nav class="breadcrumb-custom">
      <a href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">Início</a>
      <span>/</span>
      <span class="current"><%= isReturn ? "Devolução" : "Renovação" %></span>
    </nav>

    <section class="titulo-section">
      <h1><%= isReturn ? "Registrar Devolução" : "Registrar Renovação" %></h1>
      <p>Informe o CPF do cliente para localizar os empréstimos ativos.</p>
    </section>

    <%-- Notificações --%>
    <% if ("return_success".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg">
        <i class="bi bi-check-circle-fill"></i> Devolução registrada com sucesso!
      </div>
    <% } else if ("renewal_success".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg">
        <i class="bi bi-check-circle-fill"></i> Renovação registrada com sucesso!
      </div>
    <% } else if ("error".equals(msg)) { %>
      <div class="toast-msg toast-error show" id="toastMsg">
        <i class="bi bi-x-circle-fill"></i>
        <%= request.getParameter("detail") != null ? request.getParameter("detail") : "Erro ao processar." %>
      </div>
    <% } %>

    <% if (errorMsg != null) { %>
      <div class="toast-msg toast-error show" id="errorToast">
        <i class="bi bi-x-circle-fill"></i> <%= errorMsg %>
      </div>
    <% } %>

    <%-- Busca por CPF — mantém o type na URL --%>
    <form action="${pageContext.request.contextPath}/returnBook" method="get" class="search-box">
      <input type="hidden" name="type" value="<%= type %>"/>
      <input type="text" name="cpf" placeholder="Digite o CPF do cliente..."
             value="<%= cpfParam != null ? cpfParam : "" %>" required/>
      <button type="submit" class="btn-search">Buscar</button>
    </form>

    <%-- Dados do cliente --%>
    <% if (client != null) { %>
      <div class="client-info-bar">
        <i class="bi bi-person-circle"></i>
        <div>
          <strong><%= client.getName() %></strong>
          <span>CPF: <%= client.getCpf() %></span>
        </div>
        <% if (client.isSuspended()) { %>
          <span class="status-suspended">Suspenso até <%= client.getEndSuspensionDate() %></span>
        <% } else { %>
          <span class="status-ok"><i class="bi bi-check-circle"></i> Ativo</span>
        <% } %>
      </div>
    <% } %>

    <%-- Lista de empréstimos ativos --%>
    <% if (loans != null && !loans.isEmpty()) { %>
      <div class="loans-list">
        <% for (Loan loan : loans) {
            boolean isLate = LocalDateTime.now().isAfter(loan.getExpectedReturnDate());
            String bookName = (loan.getBookCopy().getBook() != null && loan.getBookCopy().getBook().getBookName() != null)
                ? loan.getBookCopy().getBook().getBookName()
                : "Exemplar #" + loan.getBookCopy().getBookCopyId();
        %>
          <div class="loan-card">
            <div class="loan-header">
              <div class="loan-book-name"><i class="bi bi-book"></i> <%= bookName %></div>
              <% if (isLate) { %>
                <span class="status-late"><i class="bi bi-clock-history"></i> Atrasado</span>
              <% } else { %>
                <span class="status-ok"><i class="bi bi-check-circle"></i> No prazo</span>
              <% } %>
            </div>

            <div class="details-grid">
              <div class="detail-card">
                <div class="label">ID do Empréstimo</div>
                <div class="value">#<%= loan.getLoanId() %></div>
              </div>
              <div class="detail-card">
                <div class="label">Exemplar</div>
                <div class="value">#<%= loan.getBookCopy().getBookCopyId() %></div>
              </div>
              <div class="detail-card">
                <div class="label">Data do Empréstimo</div>
                <div class="value"><%= loan.getLoanDate().toLocalDate() %></div>
              </div>
              <div class="detail-card">
                <div class="label">Previsão de Devolução</div>
                <div class="value"><%= loan.getExpectedReturnDate().toLocalDate() %></div>
              </div>
              <div class="detail-card">
                <div class="label">Renovações</div>
                <div class="value"><%= loan.getRenewalCount() %> de 3</div>
              </div>
            </div>

            <% if (isReturn && isLate) { %>
              <div class="late-warning">
                <i class="bi bi-exclamation-triangle-fill"></i>
                Devolução atrasada! O cliente receberá suspensão de 2 dias por dia de atraso.
              </div>
            <% } %>

            <%-- Botão de ação: Devolver OU Renovar --%>
            <form action="<%= actionUrl %>" method="post">
              <input type="hidden" name="loanId" value="<%= loan.getLoanId() %>">
              <input type="hidden" name="cpf"    value="<%= client.getCpf() %>">
              <input type="hidden" name="type"   value="<%= type %>">
              <% if (isReturn) { %>
                <button type="submit" class="btn-confirm btn-return">
                  <i class="bi bi-arrow-return-left"></i> Confirmar Devolução
                </button>
              <% } else { %>
                <button type="submit" class="btn-confirm btn-renewal"
                  <%= (loan.getRenewalCount() >= 3 || isLate) ? "disabled title='Renovação não permitida'" : "" %>>
                  <i class="bi bi-arrow-repeat"></i> Confirmar Renovação
                </button>
                <% if (loan.getRenewalCount() >= 3) { %>
                  <span class="renewal-blocked">Limite de renovações atingido</span>
                <% } else if (isLate) { %>
                  <span class="renewal-blocked">Não é possível renovar com atraso</span>
                <% } %>
              <% } %>
            </form>

          </div>
        <% } %>
      </div>
    <% } %>

  </main>

  <jsp:include page="/view/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    ['toastMsg','errorToast'].forEach(id => {
      const el = document.getElementById(id);
      if (el) setTimeout(() => { el.style.opacity='0'; el.style.transition='opacity 0.5s'; setTimeout(()=>el.style.display='none',500); }, 4000);
    });
  </script>
</body>
</html>
