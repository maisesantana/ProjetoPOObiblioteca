<%@ page import="br.com.atlas.model.Employee" %>

<%
    Object userLogged = session.getAttribute("userLogged");
    if (userLogged == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page import="java.util.List" %>
<%@ page import="br.com.atlas.model.Book" %>

<% List<Book> books = (List<Book>) request.getAttribute("books"); %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Buscar Livros</title>

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/librarian/searchBooks.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
</head>

<body>

  <header>
    <nav class="navbar navbar-expand-lg atlas-navbar">
      <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">
          <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas"/>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarAtlas">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarAtlas">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/registerBook.jsp">Cadastrar</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/removeBook.jsp">Remover</a>
            </li>
            <li class="nav-item">
              <a class="nav-link active" href="${pageContext.request.contextPath}/searchBooks">Buscar</a>
            </li>
          </ul>
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
            <i class="bi bi-box-arrow-right"></i> Sair
          </a>
        </div>
      </div>
    </nav>
  </header>

  <main class="container-fluid books-container">

    <nav class="breadcrumb-custom">
      <a href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a>
      <span>/</span>
      <a href="${pageContext.request.contextPath}/searchBooks">Livros</a>
    </nav>

    <section class="titulo-section">
      <h1>Visualizar e buscar livros no acervo do sistema</h1>
      <p>Consulta de Acervo</p>
    </section>

    <section class="search-box">
      <form action="${pageContext.request.contextPath}/searchBooks" method="get">
        <input type="text" name="query"
               placeholder="Digite o título do livro ou autor para buscar..."
               value="${param.query != null ? param.query : ''}">
        <button type="submit">Buscar</button>
      </form>
    </section>

    <section class="books-list">
      <% if (books != null && !books.isEmpty()) { %>
        <% for (Book book : books) { %>
          <div class="book-card">
            <div class="book-icon">
              <i class="bi bi-book"></i>
            </div>
            <div class="book-info">
              <div class="book-details">
                <h3><%= book.getBookName() %></h3>
                <span class="book-author">Autor: <%= String.join(", ", book.getAuthors()) %></span>
              </div>
              <span class="book-copies">
                <%= book.getCopies().size() %> un.
              </span>
            </div>
            <a href="${pageContext.request.contextPath}/viewBook?id=<%= book.getBookId() %>" class="book-view">
              Ver
            </a>
          </div>
        <% } %>
      <% } else { %>
        <p class="text-muted text-center py-4">Nenhum livro encontrado no acervo.</p>
      <% } %>
    </section>

  </main>

  <jsp:include page="/view/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>