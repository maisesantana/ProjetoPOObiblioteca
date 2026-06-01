<%@ page import="br.com.atlas.model.Attendant" %>
<%@ page import="br.com.atlas.model.Book" %>
<%@ page import="br.com.atlas.model.BookCopy" %>
<%@ page import="br.com.atlas.model.Client" %>
<%@ page import="java.util.List" %>

<%
    Object user = session.getAttribute("userLogged");
    if(user == null || !(user instanceof Attendant)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
        return;
    }

    List<Book> books       = (List<Book>) request.getAttribute("books");
    Book selectedBook      = (Book) request.getAttribute("selectedBook");
    Client client          = (Client) request.getAttribute("client");
    String step            = (String) request.getAttribute("step");
    Integer copyId         = (Integer) request.getAttribute("copyId");
    String query           = (String) request.getAttribute("query");
    String msg             = request.getParameter("msg");
    String detail          = request.getParameter("detail");
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Empréstimo</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/attendant/loan.css"/>
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
          <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/loan">Empréstimo</a></li>
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/returnBook?type=return">Devolução</a></li>
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/returnBook?type=renewal">Renovação</a></li>
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/searchBooks">Buscar Livros</a></li>
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/activeLoans">Empréstimos Ativos</a></li>
        </ul>
        <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
          <i class="bi bi-box-arrow-right"></i> Sair
        </a>
      </div>
    </div>
  </nav>
</header>

<main class="loan-page">
  <div class="container loan-container">

    <div class="breadcrumb-custom">
      <a href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">Início</a>
      <span>/</span>
      <span class="current-page">Empréstimo</span>
    </div>

    <div class="title-section">
      <h1>Realizar empréstimo</h1>
      <p>Busque um livro, selecione e informe o CPF do cliente para concluir.</p>
    </div>

    <%-- Notificações --%>
    <% if ("success".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg">
        <i class="bi bi-check-circle-fill"></i> Empréstimo realizado com sucesso!
      </div>
    <% } else if ("error".equals(msg)) { %>
      <div class="toast-msg toast-error show" id="toastMsg">
        <i class="bi bi-x-circle-fill"></i>
        <%= detail != null ? detail : "Erro ao realizar empréstimo." %>
      </div>
    <% } %>

    <%-- PASSO 1: Busca de livros --%>
    <div class="loan-card">
      <h3><i class="bi bi-search"></i> Buscar Livro</h3>
      <form action="${pageContext.request.contextPath}/loan" method="get" class="search-book-box">
        <input type="text" name="query" class="form-control"
               placeholder="Digite o nome do livro ou autor"
               value="<%= query != null ? query : "" %>"/>
        <button type="submit" class="btn-search-book">Buscar</button>
      </form>

      <%-- Resultados da busca --%>
      <% if (books != null && !books.isEmpty()) { %>
        <div class="books-result-list">
          <% for (Book book : books) { %>
            <div class="book-result-card">
              <div class="book-result-info">
                  <div class="book-result-name"><%= book.getBookName() %></div>
                    <div class="book-result-author">Autor: <%= String.join(", ", book.getAuthors()) %></div>
                <% if (book.getCopies().isEmpty()) { %>
                  <div class="book-result-copies unavailable">Nenhum exemplar disponível</div>
                <% } else { %>
                  <div class="book-result-copies"><%= book.getCopies().size() %> exemplar(es)     disponível(eis)</div>
                <% } %>
              </div>
              <% if (book.getCopies().isEmpty()) { %>
                <span class="btn-select-book disabled">Indisponível</span>
              <% } else { %>
                <a href="${pageContext.request.contextPath}/loan?step=selectBook&bookId=<%= book.getBookId() %>&query=<%= query != null ? java.net.URLEncoder.encode(query, "UTF-8") : "" %>"
                  class="btn-select-book">Selecionar</a>
              <% } %>
            </div>
          <% } %>
        </div>
      <% } else if (books != null) { %>
        <p class="text-muted mt-3">Nenhum livro disponível encontrado.</p>
      <% } %>
    </div>

    <%-- PASSO 2: Livro selecionado — informar CPF --%>
    <% if ("selectBook".equals(step) && selectedBook != null) { %>
      <div class="loan-card mt-4">
        <h3><i class="bi bi-book"></i> Livro Selecionado</h3>
        <div class="selected-book-info visible">
          <div class="book-detail-row"><span class="label">Nome</span><span class="value"><%= selectedBook.getBookName() %></span></div>
          <div class="book-detail-row"><span class="label">Autor</span><span class="value"><%= String.join(", ", selectedBook.getAuthors()) %></span></div>
          <div class="book-detail-row"><span class="label">Disponíveis</span><span class="value"><%= selectedBook.getCopies().size() %></span></div>
        </div>

        <form action="${pageContext.request.contextPath}/loan" method="get" class="client-search mt-4">
          <input type="hidden" name="step" value="confirm">
          <input type="hidden" name="bookId" value="<%= selectedBook.getBookId() %>">
          <h3><i class="bi bi-person"></i> Informar Cliente</h3>
          <div class="client-search-row">
            <input type="text" name="cpf" class="form-control" placeholder="Digite o CPF do cliente" required/>
            <button type="submit" class="btn-check">Verificar</button>
          </div>
        </form>
      </div>
    <% } %>

    <%-- PASSO 3: Confirmação do empréstimo --%>
    <% if ("confirm".equals(step) && selectedBook != null && client != null) { %>
      <div class="loan-card mt-4">
        <h3><i class="bi bi-check2-circle"></i> Confirmar Empréstimo</h3>

        <div class="selected-book-info visible">
          <div class="book-detail-row"><span class="label">Livro</span><span class="value"><%= selectedBook.getBookName() %></span></div>
          <div class="book-detail-row"><span class="label">Cliente</span><span class="value"><%= client.getName() %></span></div>
          <div class="book-detail-row"><span class="label">CPF</span><span class="value"><%= client.getCpf() %></span></div>
          <div class="book-detail-row">
            <span class="label">Status</span>
            <span class="value">
              <% if (client.isSuspended()) { %>
                <span class="status-suspended"><i class="bi bi-x-circle"></i> Suspenso até <%= client.getEndSuspensionDate() %></span>
              <% } else { %>
                <span class="status-ok"><i class="bi bi-check-circle"></i> Liberado</span>
              <% } %>
            </span>
          </div>
        </div>

        <% Integer activeLoans = (Integer) request.getAttribute("activeLoans"); %>
          <% if (client.isSuspended()) { %>
            <div class="toast-msg toast-error show mt-3">
              <i class="bi bi-x-circle-fill"></i> Cliente suspenso — não é possível realizar empréstimo.
            </div>
          <% } else if (activeLoans != null && activeLoans >= 3) { %>
            <div class="toast-msg toast-error show mt-3">
              <i class="bi bi-x-circle-fill"></i> Cliente já possui 3 empréstimos ativos — limite máximo atingido.
            </div>
          <% } else if (copyId != null) { %>
            <form action="${pageContext.request.contextPath}/loan" method="post" class="mt-4">
              <input type="hidden" name="cpf" value="<%= client.getCpf() %>">
              <input type="hidden" name="copyId" value="<%= copyId %>">
              <button type="submit" class="btn-confirm">
                <i class="bi bi-check-lg"></i> Confirmar Empréstimo
              </button>
            </form>
          <% } else { %>
            <div class="toast-msg toast-error show mt-3">
              <i class="bi bi-x-circle-fill"></i> Nenhum exemplar disponível para este livro.
            </div>
          <% } %>

        <a href="${pageContext.request.contextPath}/loan?step=selectBook&bookId=<%= selectedBook.getBookId() %>"
           class="btn-back mt-3 d-inline-block">
          <i class="bi bi-arrow-left"></i> Voltar
        </a>
      </div>
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
