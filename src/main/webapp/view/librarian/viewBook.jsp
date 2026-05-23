<%@ page import="br.com.atlas.model.Attendant" %>
<%@ page import="br.com.atlas.model.Librarian" %>
<%@ page import="br.com.atlas.model.Book" %>

<%
    Object user = session.getAttribute("userLogged");
    if(user == null || !(user instanceof Librarian || user instanceof Attendant)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
        return;
    }
    Book book = (Book) request.getAttribute("book");
    if (book == null) {
        response.sendRedirect(request.getContextPath() + "/searchBooks");
        return;
    }
    int totalCopies     = book.getCopies().size();
    int availableCopies = book.totalAvailableCopies();
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - <%= book.getBookName() %></title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
  <style>
    *{margin:0;padding:0;box-sizing:border-box}
    body{background:#fff;font-family:'Plus Jakarta Sans',sans-serif;color:#1B1B1B;min-height:100vh;display:flex;flex-direction:column}
    main{flex:1}
    .view-container{max-width:700px;margin:0 auto;padding:2rem 1.5rem 4rem}
    .breadcrumb-custom{display:flex;align-items:center;gap:.5rem;margin-bottom:2rem;font-size:.9rem}
    .breadcrumb-custom a{text-decoration:none;color:#6E7BF2}
    .breadcrumb-custom span{color:#7C7C7C}
    .book-cover{display:flex;justify-content:center;margin-bottom:1.5rem}
    .book-cover-placeholder{width:130px;height:185px;background:#ECECEC;border-radius:10px;display:flex;align-items:center;justify-content:center;font-size:4rem;color:#999}
    .book-title-section{text-align:center;margin-bottom:2rem}
    .book-title-section h1{font-size:1.4rem;font-weight:700;color:#222}
    .details-grid{display:grid;grid-template-columns:1fr 1fr 1fr;gap:1rem;margin-bottom:1.5rem}
    .detail-card{background:#F3F3F3;border-radius:12px;padding:1rem 1.2rem}
    .detail-card .label{font-size:.78rem;font-weight:600;color:#8C8C8C;margin-bottom:.3rem;text-transform:uppercase;letter-spacing:.4px}
    .detail-card .value{font-size:1rem;font-weight:600;color:#1B1B1B}
    .btn-back{display:inline-block;margin-top:2rem;color:#6E7BF2;text-decoration:none;font-weight:600;font-size:.95rem}
    .btn-back:hover{opacity:.75}
    @media(max-width:600px){.details-grid{grid-template-columns:1fr 1fr}}
  </style>
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
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/addCopies">Exemplares</a></li>
            <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/searchBooks">Buscar</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/manageAuthors">Autores</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/manageCategory">Categorias</a></li>
          </ul>
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair"><i class="bi bi-box-arrow-right"></i> Sair</a>
        </div>
      </div>
    </nav>
  </header>

  <main class="container-fluid view-container">
    <nav class="breadcrumb-custom">
      <a href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a>
      <span>/</span>
      <a href="${pageContext.request.contextPath}/searchBooks">Livros</a>
      <span>/</span>
      <span style="color:#1B1B1B"><%= book.getBookName() %></span>
    </nav>
    <div class="book-cover">
      <div class="book-cover-placeholder"><i class="bi bi-book"></i></div>
    </div>
    <div class="book-title-section"><h1>Resumo do livro</h1></div>
    <div class="details-grid">
      <div class="detail-card"><div class="label">Nome</div><div class="value"><%= book.getBookName() %></div></div>
      <div class="detail-card"><div class="label">Páginas</div><div class="value"><%= book.getNumberOfPages() %></div></div>
      <div class="detail-card"><div class="label">Autor</div><div class="value"><%= String.join(", ", book.getAuthors()) %></div></div>
      <div class="detail-card"><div class="label">Exemplares</div><div class="value"><%= totalCopies %></div></div>
      <div class="detail-card"><div class="label">Editora</div><div class="value"><%= book.getPublisher() != null ? book.getPublisher() : "-" %></div></div>
      <div class="detail-card"><div class="label">Categoria</div><div class="value"><%= String.join(", ", book.getCategories()) %></div></div>
    </div>
    <a href="${pageContext.request.contextPath}/searchBooks" class="btn-back">
      <i class="bi bi-arrow-left"></i> Voltar para busca
    </a>
  </main>

  <jsp:include page="/view/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
