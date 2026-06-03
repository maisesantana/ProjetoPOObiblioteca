<%@ page import="br.com.atlas.model.Employee" %>
<% Object userLogged=session.getAttribute("userLogged"); if (userLogged==null) {
  response.sendRedirect(request.getContextPath() + "/index.jsp" ); return; } %>

  <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
    <%@ page import="java.util.List" %>
      <%@ page import="br.com.atlas.model.Author" %>

        <% List<Author> authors = (List<Author>) request.getAttribute("authors");
            String msg = request.getParameter("msg");
            String activeTab = (String) request.getAttribute("activeTab");
            if (activeTab == null) {
            activeTab = request.getParameter("tab");
            }
            if (activeTab == null || activeTab.trim().isEmpty()) {
            activeTab = "register";
            }
            activeTab = activeTab.trim();
            %>

            <!DOCTYPE html>
            <html lang="pt-BR">

            <head>
              <meta charset="UTF-8" />
              <meta name="viewport" content="width=device-width, initial-scale=1.0" />
              <title>Atlas - Gerenciar Autores</title>
              <link rel="preconnect" href="https://fonts.googleapis.com">
              <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
              <link
                href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap"
                rel="stylesheet">
              <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
              <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
                rel="stylesheet" />
              <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css" />
              <link rel="stylesheet"
                href="${pageContext.request.contextPath}/assets/css/librarian/manageAuthors.css" />
              <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css" />
              <style>
                /* Garante que o card de autor siga o mesmo comportamento flexível de categorias */
                .author-card {
                  display: flex;
                  align-items: center;
                  width: 100%;
                  margin-bottom: 1rem;
                }
                .author-icon {
                  flex-shrink: 0;
                }
                .author-info {
                  flex-grow: 1;
                  background-color: #F3F4F6; /* Cor cinza do card */
                  border-radius: 0.5rem;
                  padding: 1rem 1.5rem;
                  margin-left: 1rem;
                  margin-right: 1rem;
                }
                .author-display {
                  display: flex;
                  justify-content: space-between;
                  align-items: center;
                  width: 100%;
                }
                .btn-edit {
                  background: none;
                  border: none;
                  color: #4F46E5;
                  font-weight: 500;
                  cursor: pointer;
                  padding: 0;
                }
                .btn-delete-link {
                  background: none;
                  border: none;
                  color: #DC2626;
                  font-size: 1.25rem;
                  cursor: pointer;
                  padding: 0.5rem;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                  flex-shrink: 0;
                }
                .edit-form {
                  display: none;
                  width: 100%;
                  gap: 0.5rem;
                  align-items: center;
                }
                .edit-form.show {
                  display: flex;
                }
              </style>
            </head>

            <body>
              <input type="hidden" id="serverActiveTab" value="<%= activeTab %>">

              <header>
                <nav class="navbar navbar-expand-lg atlas-navbar">
                  <div class="container-fluid">
                    <a class="navbar-brand"
                      href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">
                      <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas" />
                    </a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                      data-bs-target="#navbarAtlas">
                      <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarAtlas">
                      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a>
                        </li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/view/librarian/registerBook.jsp">Cadastrar</a>
                        </li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/removeBook">Remover</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/addCopies">Exemplares</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/searchBooks">Buscar</a></li>
                        <li class="nav-item"><a class="nav-link active"
                            href="${pageContext.request.contextPath}/manageAuthors?tab=register">Autores</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/manageCategory">Categorias</a></li>
                        <li class="nav-item"><a class="nav-link"
                            href="${pageContext.request.contextPath}/bookList">Lista de livros</a></li>
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
                    <% } else if ("author_deleted".equals(msg)) { %>
                      <div class="toast-msg toast-success show" id="toastMsg">
                        <i class="bi bi-check-circle-fill"></i> Autor removido com sucesso!
                      </div>
                    <% } else if ("author_has_books".equals(msg)) { %>
                      <div class="toast-msg toast-error show" id="toastMsg">
                        <i class="bi bi-x-circle-fill"></i> Não é possível apagar um autor que possui livros vinculados.
                      </div>
                    <% } else if ("error".equals(msg)) { %>
                      <div class="toast-msg toast-error show" id="toastMsg">
                        <i class="bi bi-x-circle-fill"></i> Ocorreu um erro. Tente novamente.
                      </div>
                    <% } else if ("name_empty".equals(msg)) { %>
                      <div class="toast-msg toast-error show" id="toastMsg">
                        <i class="bi bi-x-circle-fill"></i> O nome do autor não pode estar vazio.
                      </div>
                    <% } else if ("author_exists".equals(msg)) { %>
                      <div class="toast-msg toast-error show" id="toastMsg">
                        <i class="bi bi-x-circle-fill"></i> Já existe um autor cadastrado com esse nome.
                      </div>
                    <% } %>

                                <div class="tabs">
                                  <button type="button" class="tab-btn" data-target="register">Cadastrar autor</button>
                                  <button type="button" class="tab-btn" data-target="edit">Editar autores</button>
                                </div>

                                <%-- ABA: CADASTRAR --%>
                                  <div class="tab-panel" id="panel-register">
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
                                    <div class="tab-panel" id="panel-edit">
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
                                        <% if (authors !=null && !authors.isEmpty()) { %>
                                          <% for (Author author : authors) { %>

                                            <div class="author-card" id="card-<%= author.getAuthorId() %>">
                                              <div class="author-icon">
                                                <i class="bi bi-person"></i>
                                              </div>
                                              
                                              <div class="author-info">
                                                <%-- Visualização normal (Texto simples "Editar", sem o ícone de lápis) --%>
                                                  <div class="author-display" id="display-<%= author.getAuthorId() %>">
                                                    <span class="author-name">
                                                      <%= author.getAuthorName() %>
                                                    </span>
                                                    <button type="button" class="btn-edit"
                                                      data-id="<%= author.getAuthorId() %>"
                                                      data-name="<%= author.getAuthorName().replace("\"", "&quot;") %>">
                                                      Editar
                                                    </button>
                                                  </div>

                                                  <%-- Formulário de edição inline --%>
                                                    <form class="edit-form" id="form-<%= author.getAuthorId() %>"
                                                      action="${pageContext.request.contextPath}/manageAuthors" method="post">
                                                      <input type="hidden" name="action" value="update">
                                                      <input type="hidden" name="authorId" value="<%= author.getAuthorId() %>">
                                                      <input type="hidden" name="tab" value="edit">
                                                      <input type="hidden" name="query" value="${param.query}">
                                                      <input type="text" name="authorName" id="input-<%= author.getAuthorId() %>" required>
                                                      <button type="submit" class="btn-save">Salvar</button>
                                                      <button type="button" class="btn-cancel" data-id="<%= author.getAuthorId() %>">Cancelar</button>
                                                    </form>
                                              </div>

                                              <%-- Lixeira Vermelha Isolada à Direita (Exatamente idêntica a Categorias) --%>
                                              <form action="${pageContext.request.contextPath}/manageAuthors" method="post" style="margin: 0;" onsubmit="return confirm('Tem certeza que deseja apagar o autor <%= author.getAuthorName() %>?');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="authorId" value="<%= author.getAuthorId() %>">
                                                <input type="hidden" name="tab" value="edit">
                                                <input type="hidden" name="query" value="${param.query}">
                                                <button type="submit" class="btn-delete-link" title="Apagar Autor">
                                                  <i class="bi bi-trash"></i>
                                                </button>
                                              </form>
                                            </div>

                                            <% } %>
                                              <% } else if (authors !=null) { %>
                                                <p class="text-muted text-center py-4">Nenhum autor encontrado.</p>
                                                <% } %>
                                      </section>
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

                  document.querySelectorAll('.btn-edit').forEach(button => {
                    button.addEventListener('click', function () {
                      const id = this.getAttribute('data-id');
                      const currentName = this.getAttribute('data-name');
                      document.getElementById('display-' + id).style.display = 'none';
                      const form = document.getElementById('form-' + id);
                      form.classList.add('show');
                      const input = document.getElementById('input-' + id);
                      input.value = currentName;
                      input.focus();
                    });
                  });

                  document.querySelectorAll('.btn-cancel').forEach(button => {
                    button.addEventListener('click', function () {
                      const id = this.getAttribute('data-id');
                      document.getElementById('display-' + id).style.display = 'flex';
                      document.getElementById('form-' + id).classList.remove('show');
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