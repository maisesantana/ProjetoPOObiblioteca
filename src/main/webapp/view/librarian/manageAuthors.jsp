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
<%@ page import="br.com.atlas.model.Author" %>

<%
    List<Author> authors = (List<Author>) request.getAttribute("authors");
    String msg           = request.getParameter("msg");
    String activeTab     = request.getParameter("tab");
    if (activeTab == null) activeTab = "register";
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Gerenciar Autores</title>

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/librarian/manageAuthors.css"/>
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
              <a class="nav-link" href="${pageContext.request.contextPath}/removeBook">Remover</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.request.contextPath}/searchBooks">Buscar</a>
            </li>
            <li class="nav-item">
              <a class="nav-link active" href="${pageContext.request.contextPath}/manageAuthors">Autores</a>
            </li>
          </ul>
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
            <i class="bi bi-box-arrow-right"></i> Sair
          </a>
        </div>
      </div>
    </nav>
  </header>

  <main class="container-fluid authors-container">

    <nav class="breadcrumb-custom">
      <a href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a>
      <span>/</span>
      <span class="current">Gerenciar autores</span>
    </nav>

    <section class="titulo-section">
      <h1>Gerenciar autores do acervo</h1>
      <p>Cadastre novos autores ou edite os já existentes</p>
    </section>

    <%-- Notificações --%>
    <% if ("author_added".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg">
        <i class="bi bi-check-circle-fill"></i> Autor cadastrado com sucesso!
      </div>
    <% } else if ("author_updated".equals(msg)) { %>
      <div class="toast-msg toast-success show" id="toastMsg">
        <i class="bi bi-check-circle-fill"></i> Autor atualizado com sucesso!
      </div>
    <% } else if ("error".equals(msg)) { %>
      <div class="toast-msg toast-error show" id="toastMsg">
        <i class="bi bi-x-circle-fill"></i> Ocorreu um erro. Tente novamente.
      </div>
    <% } else if ("name_empty".equals(msg)) { %>
      <div class="toast-msg toast-error show" id="toastMsg">
        <i class="bi bi-x-circle-fill"></i> O nome do autor não pode estar vazio.
      </div>
    <% } %>

    <%-- Abas: Removido totalmente os atributos onclick daqui para evitar erros no editor --%>
    <div class="tabs">
      <button type="button" class="tab-btn <%= "register".equals(activeTab) ? "active" : "" %>" data-target="register">Cadastrar autor</button>
      <button type="button" class="tab-btn <%= "edit".equals(activeTab) ? "active" : "" %>" data-target="edit">Editar autores</button>
    </div>

    <%-- ABA: CADASTRAR --%>
    <div class="tab-panel <%= "register".equals(activeTab) ? "active" : "" %>" id="panel-register">
      <div class="form-card">
        <form action="${pageContext.request.contextPath}/manageAuthors" method="post">
          <input type="hidden" name="action" value="register">
          <div class="mb-3">
            <label for="authorName">Nome do autor</label>
            <input type="text" id="authorName" name="authorName"
                   placeholder="Ex: Machado de Assis" required>
          </div>
          <button type="submit" class="btn-primary-atlas">Cadastrar autor</button>
        </form>
      </div>
    </div>

    <%-- ABA: EDITAR --%>
    <div class="tab-panel <%= "edit".equals(activeTab) ? "active" : "" %>" id="panel-edit">

      <section class="search-box">
        <form action="${pageContext.request.contextPath}/manageAuthors" method="get">
          <input type="hidden" name="tab" value="edit">
          <input type="text" name="query"
                 placeholder="Digite o nome do autor para buscar..."
                 value="${param.query != null ? param.query : ''}">
          <button type="submit">Buscar</button>
        </form>
      </section>

      <section class="authors-list">
        <% if (authors != null && !authors.isEmpty()) { %>
          <% for (Author author : authors) { %>
            <div class="author-card" id="card-<%= author.getAuthorId() %>">
              <div class="author-icon">
                <i class="bi bi-person"></i>
              </div>
              <div class="author-info">

                <%-- Visualização normal --%>
                <div class="author-display" id="display-<%= author.getAuthorId() %>">
                  <span class="author-name"><%= author.getAuthorName() %></span>
                  <button type="button" class="btn-edit" 
                          data-id="<%= author.getAuthorId() %>" 
                          data-name="<%= author.getAuthorName().replace("\"", "&quot;") %>">
                    <i class="bi bi-pencil"></i> Editar
                  </button>
                </div>

                <%-- Formulário de edição inline --%>
                <form class="edit-form" id="form-<%= author.getAuthorId() %>"
                      action="${pageContext.request.contextPath}/manageAuthors" method="post">
                  <input type="hidden" name="action" value="update">
                  <input type="hidden" name="authorId" value="<%= author.getAuthorId() %>">
                  <input type="hidden" name="tab" value="edit">
                  <input type="hidden" name="query" value="${param.query}">
                  <input type="text" name="authorName"
                         id="input-<%= author.getAuthorId() %>" required>
                  <button type="submit" class="btn-save">Salvar</button>
                  <button type="button" class="btn-cancel" data-id="<%= author.getAuthorId() %>">Cancelar</button>
                </form>

              </div>
            </div>
          <% } %>
        <% } else if (authors != null) { %>
          <p class="text-muted text-center py-4">Nenhum autor encontrado.</p>
        <% } %>
      </section>
    </div>

  </main>

  <jsp:include page="/view/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

  <script>
    document.addEventListener("DOMContentLoaded", function() {
      
      // Gerenciamento de Abas via Event Listeners do JavaScript (Zero código inline no HTML)
      document.querySelectorAll('.tab-btn').forEach(button => {
        button.addEventListener('click', function() {
          const tabName = this.getAttribute('data-target');
          
          document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
          document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));
          
          document.querySelector('#panel-' + tabName).classList.add('active');
          this.classList.add('active');
        });
      });

      // Evento de clique para os botões Editar
      document.querySelectorAll('.btn-edit').forEach(button => {
        button.addEventListener('click', function() {
          const id = this.getAttribute('data-id');
          const currentName = this.getAttribute('data-name');
          
          document.getElementById('display-' + id).style.style.display = 'none';
          
          const form = document.getElementById('form-' + id);
          form.classList.add('show');
          
          const input = document.getElementById('input-' + id);
          input.value = currentName;
          input.focus();
        });
      });

      // Evento de clique para os botões Cancelar
      document.querySelectorAll('.btn-cancel').forEach(button => {
        button.addEventListener('click', function() {
          const id = this.getAttribute('data-id');
          document.getElementById('display-' + id).style.style.display = 'flex';
          document.getElementById('form-' + id).classList.remove('show');
        });
      });

      // Auto-esconde notificação toast
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