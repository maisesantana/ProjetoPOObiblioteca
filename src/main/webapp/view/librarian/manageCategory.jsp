<%@ page import="br.com.atlas.model.Librarian" %>
  <%@ page import="java.util.List" %>
    <%@ page import="br.com.atlas.model.Category" %>

      <% Object user=session.getAttribute("userLogged"); if(user==null || !(user instanceof Librarian)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized" ); return; } List<Category>
        categories = (List<Category>) request.getAttribute("categories");
          String msg = request.getParameter("msg");
          String activeTab = (request.getParameter("query") != null
          || "category_updated".equals(msg)
          || "category_deleted".equals(msg)) ? "list" : "register";
          %>

          <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

            <!DOCTYPE html>
            <html lang="pt-BR">

            <head>
              <meta charset="UTF-8" />
              <meta name="viewport" content="width=device-width, initial-scale=1.0" />
              <title>Atlas - Gerenciar Categorias</title>
              <link rel="preconnect" href="https://fonts.googleapis.com">
              <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
              <link
                href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap"
                rel="stylesheet">
              <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
              <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
                rel="stylesheet" />
              <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css" />
              <link rel="stylesheet"
                href="${pageContext.request.contextPath}/assets/css/librarian/manageCategory.css" />
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
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/removeBook">Remover</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/addCopies">Exemplares</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/searchBooks">Buscar</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/manageAuthors">Autores</a></li>
                        <li class="nav-item"><a class="nav-link active"
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

              <main class="container-fluid categories-container">
                <nav class="breadcrumb-custom">
                  <a href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a>
                  <span>/</span>
                  <span class="current">Gerenciar Categorias</span>
                </nav>
                <section class="titulo-section">
                  <h1>Gerenciar Categorias</h1>
                  <p>Cadastre novas categorias ou faça alterações nas já existentes</p>
                </section>

                <div class="toast-msg toast-success <%= " category_added".equals(msg) ? "show" : "" %>"><i
                    class="bi bi-check-circle-fill"></i> Categoria cadastrada com sucesso!</div>
                <div class="toast-msg toast-success <%= " category_updated".equals(msg) ? "show" : "" %>"><i
                    class="bi bi-check-circle-fill"></i> Alterações salvas com sucesso!</div>
                <div class="toast-msg toast-success <%= " category_deleted".equals(msg) ? "show" : "" %>"><i
                    class="bi bi-check-circle-fill"></i> Categoria removida com sucesso!</div>
                <div class="toast-msg toast-error <%= " error".equals(msg) ? "show" : "" %>"><i
                    class="bi bi-exclamation-circle-fill"></i> Erro ao processar a requisição.</div>

                <div class="tabs">
                  <button type="button" class="tab-btn <%= " register".equals(activeTab) ? "active" : "" %>"
                    data-target="register">Cadastrar categoria</button>
                  <button type="button" class="tab-btn <%= " list".equals(activeTab) ? "active" : "" %>"
                    data-target="list">Categorias cadastradas</button>
                </div>

                <div id="panel-register" class="tab-panel <%= " register".equals(activeTab) ? "active" : "" %>">
                  <div class="form-card">
                    <form action="${pageContext.request.contextPath}/manageCategory" method="post">
                      <input type="hidden" name="action" value="insert">
                      <label for="categoryName">Nome da categoria</label>
                      <input type="text" id="categoryName" name="categoryName"
                        placeholder="Ex: Romance, Computação, Suspense..." required>
                      <button type="submit" class="btn-primary-atlas">Cadastrar</button>
                    </form>
                  </div>
                </div>

                <div id="panel-list" class="tab-panel <%= " list".equals(activeTab) ? "active" : "" %>">
                  <section class="search-box">
                    <form action="${pageContext.request.contextPath}/manageCategory" method="get">
                      <input type="text" name="query" placeholder="Buscar categoria por nome exato..."
                        value="${param.query != null ? param.query : ''}">
                      <button type="submit">Buscar</button>
                    </form>
                  </section>
                  <div class="categories-list">
                    <% if (categories !=null && !categories.isEmpty()) { %>
                      <% for (Category cat : categories) { %>
                        <div class="category-card" id="card-<%= cat.getCategoryId() %>">
                          <div class="category-icon"><i class="bi bi-tags"></i></div>
                          <div class="category-info">
                            <span class="category-name" id="name-display-<%= cat.getCategoryId() %>">
                              <%= cat.getCategoryName() %>
                            </span>
                            <button type="button" class="btn-edit" id="btn-edit-<%= cat.getCategoryId() %>"
                              data-id="<%= cat.getCategoryId() %>">Editar</button>
                            <form action="${pageContext.request.contextPath}/manageCategory" method="post"
                              class="edit-form" id="form-edit-<%= cat.getCategoryId() %>">
                              <input type="hidden" name="action" value="update">
                              <input type="hidden" name="categoryId" value="<%= cat.getCategoryId() %>">
                              <input type="hidden" name="query" value="${param.query != null ? param.query : ''}">
                              <input type="text" name="categoryName" value="<%= cat.getCategoryName() %>" required>
                              <button type="submit" class="btn-save">Salvar</button>
                              <button type="button" class="btn-cancel"
                                data-id="<%= cat.getCategoryId() %>">Cancelar</button>
                            </form>
                          </div>
                          <form action="${pageContext.request.contextPath}/manageCategory" method="post"
                            onsubmit="return confirm('Tem certeza que deseja remover?')">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="categoryId" value="<%= cat.getCategoryId() %>">
                            <input type="hidden" name="query" value="${param.query != null ? param.query : ''}">
                            <button type="submit" class="btn-delete-link"><i class="bi bi-trash"></i></button>
                          </form>
                        </div>
                        <% } %>
                          <% } else if (categories !=null) { %>
                            <p class="text-muted text-center py-4">Nenhuma categoria encontrada.</p>
                            <% } %>
                  </div>
                </div>
              </main>

              <jsp:include page="/view/footer.jsp" />
              <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
              <script>
                document.addEventListener("DOMContentLoaded", function () {
                  document.querySelectorAll('.tab-btn').forEach(b => {
                    b.addEventListener('click', function () {
                      const t = this.getAttribute('data-target');
                      document.querySelectorAll('.tab-btn').forEach(x => x.classList.remove('active'));
                      document.querySelectorAll('.tab-panel').forEach(x => x.classList.remove('active'));
                      this.classList.add('active');
                      document.getElementById('panel-' + t).classList.add('active');
                    });
                  });
                  document.querySelectorAll('.btn-edit').forEach(b => {
                    b.addEventListener('click', function () {
                      const id = this.getAttribute('data-id');
                      document.getElementById('name-display-' + id).style.display = 'none';
                      document.getElementById('btn-edit-' + id).style.display = 'none';
                      document.getElementById('form-edit-' + id).classList.add('show');
                    });
                  });
                  document.querySelectorAll('.btn-cancel').forEach(b => {
                    b.addEventListener('click', function () {
                      const id = this.getAttribute('data-id');
                      document.getElementById('name-display-' + id).style.display = 'inline';
                      document.getElementById('btn-edit-' + id).style.display = 'inline';
                      document.getElementById('form-edit-' + id).classList.remove('show');
                    });
                  });
                  document.querySelectorAll('.toast-msg').forEach(t => {
                    if (t.classList.contains('show')) setTimeout(() => { t.style.opacity = '0'; t.style.transition = 'opacity 0.5s'; setTimeout(() => t.style.display = 'none', 500); }, 4000);
                  });
                });
              </script>
            </body>

            </html>