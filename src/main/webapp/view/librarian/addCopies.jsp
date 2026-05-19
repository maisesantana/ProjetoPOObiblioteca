<%@ page import="br.com.atlas.model.Librarian" %>
<%@ page import="java.util.List" %>
<%@ page import="br.com.atlas.model.Book" %>

<%
    Object user = session.getAttribute("userLogged");
    if(user == null || !(user instanceof Librarian)){
        response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=unauthorized");
        return;
    }
    List<Book> books = (List<Book>) request.getAttribute("books");
    String msg = request.getParameter("msg");
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Adicionar Exemplares</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/librarian/addCopies.css"/>
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
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/registerBook.jsp">Cadastrar</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/removeBook">Remover</a></li>
            <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/addCopies">Exemplares</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/searchBooks">Buscar</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/manageAuthors">Autores</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/manageCategory">Categorias</a></li>
          </ul>
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair"><i class="bi bi-box-arrow-right"></i> Sair</a>
        </div>
      </div>
    </nav>
  </header>

  <main class="container-fluid copies-container">
    <nav class="breadcrumb-custom">
      <a href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a>
      <span>/</span>
      <span class="current">Adicionar Exemplares</span>
    </nav>
    <section class="titulo-section">
      <h1>Adicionar Exemplares ao Acervo</h1>
      <p>Busque pelo livro para expandir a quantidade de cópias disponíveis</p>
    </section>

    <div class="toast-custom toast-success <%= "copies_added".equals(msg) ? "show" : "" %>" id="toastSuccess">
      <i class="bi bi-check-circle-fill"></i> Exemplares adicionados com sucesso!
    </div>
    <div class="toast-custom toast-error <%= "error".equals(msg) ? "show" : "" %>" id="toastError">
      <i class="bi bi-exclamation-circle-fill"></i> Erro ao processar a inclusão dos exemplares.
    </div>

    <section class="search-box">
      <form action="${pageContext.request.contextPath}/addCopies" method="get">
        <input type="text" name="query" placeholder="Digite o título do livro para buscar..."
               value="${param.query != null ? param.query : ''}" required>
        <button type="submit">Buscar Livro</button>
      </form>
    </section>

    <section class="books-list">
      <% if (books != null && !books.isEmpty()) { %>
        <% for (Book book : books) { %>
          <div class="book-card">
            <div class="book-icon"><i class="bi bi-plus-square-fill"></i></div>
            <div class="book-info">
              <div class="book-details">
                <h3><%= book.getBookName() %></h3>
                <span class="book-author">Autor: <%= String.join(", ", book.getAuthors()) %></span>
              </div>
            </div>
            <form action="${pageContext.request.contextPath}/addCopies" method="post" class="add-copies-form">
              <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
              <input type="hidden" name="query" value="${param.query}">
              <div class="input-group-custom">
                <input type="number" name="quantity" min="1" max="50" value="1" required>
                <button type="submit" class="btn-add-action"><i class="bi bi-plus-lg"></i> Adicionar</button>
              </div>
            </form>
          </div>
        <% } %>
      <% } else if (books != null) { %>
        <p class="text-muted text-center py-4">Nenhum livro encontrado para o termo pesquisado.</p>
      <% } %>
    </section>
  </main>

  <jsp:include page="/view/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    ['toastSuccess','toastError'].forEach(id => {
      const t = document.getElementById(id);
      if (t && t.classList.contains('show')) setTimeout(() => { t.style.opacity='0'; t.style.transition='opacity 0.5s'; setTimeout(()=>t.style.display='none',500); }, 4000);
    });
  </script>
</body>
</html>
