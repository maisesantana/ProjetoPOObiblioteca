<%@ page import="br.com.atlas.model.Librarian" %>
  <%@ page import="java.util.List" %>
    <%@ page import="br.com.atlas.model.Book" %>

      <% Object user=session.getAttribute("userLogged"); if(user==null || !(user instanceof Librarian)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized" ); return; } List<Book>
        books = (List<Book>) request.getAttribute("books");
          String msg = request.getParameter("msg");
          %>

          <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

            <!DOCTYPE html>
            <html lang="pt-BR">

            <head>
              <meta charset="UTF-8" />
              <meta name="viewport" content="width=device-width, initial-scale=1.0" />
              <title>Atlas - Remover Livro</title>
              <link rel="preconnect" href="https://fonts.googleapis.com">
              <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
              <link
                href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap"
                rel="stylesheet">
              <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
              <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
                rel="stylesheet" />
              <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css" />
              <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/librarian/removeBook.css" />
              <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css" />
            </head>

            <body>
              <header>
                <nav class="navbar navbar-expand-lg atlas-navbar">
                  <div class="container-fluid">
                    <a class="navbar-brand" href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">
                      <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas" />
                    </a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                      data-bs-target="#navbarAtlas">
                      <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarAtlas">
                      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/view/librarian/registerBook.jsp">Cadastrar</a></li>
                        <li class="nav-item"><a class="nav-link active"
                            href="${pageContext.request.contextPath}/removeBook">Remover</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/addCopies">Exemplares</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/searchBooks">Buscar</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/manageAuthors">Autores</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/manageCategory">Categorias</a></li>
                        <li class="nav-item">
                          <a class="nav-link" href="${pageContext.request.contextPath}/bookList">Lista de livros</a>
                        </li>
                      </ul>
                      <a href="${pageContext.request.contextPath}/logout" class="btn-sair"><i
                          class="bi bi-box-arrow-right"></i> Sair</a>
                    </div>
                  </div>
                </nav>
              </header>

              <main class="container-fluid remove-container">
                <nav class="breadcrumb-custom">
                  <a href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a>
                  <span>/</span>
                  <span class="current">Remover livro</span>
                </nav>
                <section class="titulo-section">
                  <h1>Remover livro do acervo</h1>
                  <p>Busque pelo título para localizar e remover um livro</p>
                </section>

                <% if ("book_removed".equals(msg)) { %>
                  <div class="toast-success show" id="toastSuccess"><i class="bi bi-check-circle-fill"></i> Livro
                    removido com sucesso!</div>
                  <% } else if ("error".equals(msg)) { %>
                    <div class="toast-error show" id="toastError"><i class="bi bi-x-circle-fill"></i> Erro ao remover
                      livro.</div>
                    <% } %>

                      <section class="search-box">
                        <form action="${pageContext.request.contextPath}/removeBook" method="get">
                          <input type="text" name="query" placeholder="Digite o título do livro para buscar..."
                            value="${param.query != null ? param.query : ''}">
                          <button type="submit">Buscar</button>
                        </form>
                      </section>

                      <section class="books-list">
                        <% if (books !=null && !books.isEmpty()) { %>
                          <% for (Book book : books) { %>
                            <div class="book-card">
                              <div class="book-icon"><i class="bi bi-book"></i></div>
                              <div class="book-info">
                                <div class="book-details">
                                  <h3>
                                    <%= book.getBookName() %>
                                  </h3>
                                  <span class="book-author">Autor: <%= String.join(", ", book.getAuthors()) %></span>
              </div>
            </div>
            <form action=" ${pageContext.request.contextPath}/removeBook" method="post"
                                      onsubmit="return confirm('Tem certeza que deseja remover \'<%= book.getBookName() %>\'?')">
                                      <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                                      <input type="hidden" name="query" value="${param.query}">
                                      <button type="submit" class="btn-remove"><i class="bi bi-trash"></i>
                                        Remover</button>
                                      </form>
                                </div>
                                <% } %>
                                  <% } else if (books !=null) { %>
                                    <p class="text-muted text-center py-4">Nenhum livro encontrado.</p>
                                    <% } %>
                      </section>
              </main>

              <jsp:include page="/view/footer.jsp" />
              <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
              <script>
                ['toastSuccess', 'toastError'].forEach(id => {
                  const t = document.getElementById(id);
                  if (t) setTimeout(() => { t.style.opacity = '0'; t.style.transition = 'opacity 0.5s'; setTimeout(() => t.style.display = 'none', 500); }, 4000);
                });
              </script>
            </body>

            </html>