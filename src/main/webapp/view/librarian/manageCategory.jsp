<%@ page import="br.com.atlas.model.Librarian" %>
<%@ page import="br.com.atlas.model.Category" %>
<%@ page import="java.util.List" %>

<% Object user = session.getAttribute("userLogged");
   if (user == null || !(user instanceof Librarian)) {
       response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
       return;
   }
   List<Category> categories = (List<Category>) request.getAttribute("categories");
   String msg = request.getParameter("msg");

   String activeTab = "register";
   if ((request.getParameter("query") != null && !request.getParameter("query").trim().isEmpty())
       || "category_updated".equals(msg)
       || "category_deleted".equals(msg)) {
       activeTab = "list";
   }
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
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/librarian/manageCategory.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css" />
</head>

<body>
  <input type="hidden" id="serverActiveTab" value="<%= activeTab %>">

  <header>
    <nav class="navbar navbar-expand-lg atlas-navbar">
      <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">
          <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas" />
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
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/searchBooks">Buscar</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/manageAuthors">Autores</a></li>
            <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/manageCategory">Categorias</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/bookList">Lista de livros</a></li>
          </ul>
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair"><i class="bi bi-box-arrow-right"></i> Sair</a>
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

    <%-- Notificações --%>
    <% if ("category_added".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg">
        <i class="bi bi-check-circle-fill"></i> Categoria cadastrada com sucesso!
      </div>
    <% } else if ("category_updated".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg">
        <i class="bi bi-check-circle-fill"></i> Alterações salvas com sucesso!
      </div>
    <% } else if ("category_deleted".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg">
        <i class="bi bi-check-circle-fill"></i> Categoria removida com sucesso!
      </div>
    <% } else if ("error".equals(msg)) { %>
      <div class="toast-msg toast-error show" id="toastMsg">
        <i class="bi bi-x-circle-fill"></i> Ocorreu um erro. Tente novamente.
      </div>
    <% } %>

    <div class="tabs">
      <button type="button" class="tab-btn" data-target="register">Cadastrar categoria</button>
      <button type="button" class="tab-btn" data-target="list">Categorias cadastradas</button>
    </div>

    <%-- ABA: CADASTRAR --%>
    <div id="panel-register" class="tab-panel">
      <div class="form-card">
        <form action="${pageContext.request.contextPath}/manageCategory" method="post">
          <input type="hidden" name="action" value="insert">
          <label for="categoryName">Nome da categoria</label>
          <input type="text" id="categoryName" name="categoryName" placeholder="Ex: Romance, Computação, Suspense..." required>
          <button type="submit" class="btn-primary-atlas">Cadastrar</button>
        </form>
      </div>
    </div>

    <%-- ABA: LISTA --%>
    <div id="panel-list" class="tab-panel">
      <section class="search-box">
        <form action="${pageContext.request.contextPath}/manageCategory" method="get">
          <input type="text" name="query" placeholder="Buscar categoria por nome exato..." value="${param.query != null ? param.query : ''}">
          <button type="submit">Buscar</button>
        </form>
      </section>

      <div class="categories-list">
        <% if (categories != null && !categories.isEmpty()) { %>
          <% for (Category cat : categories) { %>
            <div class="category-card" id="card-<%= cat.getCategoryId() %>">
              <div class="category-icon"><i class="bi bi-tags"></i></div>
              <div class="category-info">
                <span class="category-name" id="name-display-<%= cat.getCategoryId() %>"><%= cat.getCategoryName() %></span>
                <button type="button" class="btn-edit" id="btn-edit-<%= cat.getCategoryId() %>" data-id="<%= cat.getCategoryId() %>">Editar</button>
                <form action="${pageContext.request.contextPath}/manageCategory" method="post" class="edit-form" id="form-edit-<%= cat.getCategoryId() %>">
                  <input type="hidden" name="action" value="update">
                  <input type="hidden" name="categoryId" value="<%= cat.getCategoryId() %>">
                  <input type="hidden" name="query" value="${param.query != null ? param.query : ''}">
                  <input type="text" name="categoryName" value="<%= cat.getCategoryName() %>" required>
                  <button type="submit" class="btn-save">Salvar</button>
                  <button type="button" class="btn-cancel" data-id="<%= cat.getCategoryId() %>">Cancelar</button>
                </form>
              </div>
              <form action="${pageContext.request.contextPath}/manageCategory" method="post" onsubmit="return confirm('Tem certeza que deseja remover?')">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="categoryId" value="<%= cat.getCategoryId() %>">
                <input type="hidden" name="query" value="${param.query != null ? param.query : ''}">
                <button type="submit" class="btn-delete-link"><i class="bi bi-trash"></i></button>
              </form>
            </div>
          <% } %>
        <% } else if (categories != null) { %>
          <p class="text-muted text-center py-4">Nenhuma categoria encontrada.</p>
        <% } %>
      </div>
    </div>

  </main>

  <jsp:include page="/view/footer.jsp" />
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    document.addEventListener("DOMContentLoaded", function () {

      function switchTab(tabName) {
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.tab-panel').forEach(p => {
          p.classList.remove('active');
          p.style.display = 'none';
        });

        const targetBtn = document.querySelector('.tab-btn[data-target="' + tabName + '"]');
        const targetPanel = document.getElementById('panel-' + tabName);

        if (targetBtn && targetPanel) {
          targetBtn.classList.add('active');
          targetPanel.classList.add('active');
          targetPanel.style.display = 'block';
        }
      }

      const serverTab = (document.getElementById('serverActiveTab')?.value || '').trim();
      switchTab(serverTab !== '' ? serverTab : 'register');

      document.querySelectorAll('.tab-btn').forEach(button => {
        button.addEventListener('click', function () {
          switchTab(this.getAttribute('data-target'));
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

      const toast = document.getElementById('toastMsg');
      if (toast) {
        setTimeout(() => {
          toast.style.opacity = '0';
          toast.style.transition = 'opacity 0.5s';
          setTimeout(() => toast.style.display = 'none', 500);
        }, 4000);
      }
    });
  </script>
</body>

</html>