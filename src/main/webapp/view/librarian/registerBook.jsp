<%@ page import="br.com.atlas.model.Librarian" %>
<%@ page import="br.com.atlas.model.Category" %>
<%@ page import="br.com.atlas.dao.CategoryDAO" %>
<%@ page import="br.com.atlas.util.ConnectionDb" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.Connection" %>

<%
    Object user = session.getAttribute("userLogged");
    if(user == null || !(user instanceof Librarian)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
        return;
    }

    List<Category> categories = new ArrayList<>();
    try (Connection conn = ConnectionDb.getConexao()) {
        categories = new CategoryDAO(conn).findAll();
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Cadastrar Livro</title>

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/admTools.css"/>

  <style>
    body { font-family: 'Plus Jakarta Sans', sans-serif; background-color: #fff; }
    .breadcrumb-atlas { font-size: 0.9rem; color: #6c757d; }
    .breadcrumb-atlas a { text-decoration: none; color: #80A4ED; }
    .form-label { font-weight: 600; color: #333; margin-bottom: 0.5rem; }
    .form-control, .form-select { border-radius: 8px; padding: 0.6rem 1rem; border: 1px solid #dee2e6; }
    .btn-finalizar { background-color: #80A4ED; border: none; color: white; font-weight: 600; padding: 0.8rem; border-radius: 8px; transition: 0.3s; width: 100%; max-width: 500px; }
    .btn-finalizar:hover { background-color: #6690d8; transform: translateY(-2px); box-shadow: 0 4px 12px rgba(128,164,237,0.3); }
    #newCategoryDiv { display: none; margin-top: 0.5rem; }
  </style>
</head>

<body>

  <header>
    <nav class="navbar navbar-expand-lg atlas-navbar">
      <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">
          <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas — Gestão de Biblioteca"/>
        </a>
        <div class="collapse navbar-collapse" id="navbarAtlas">
          <ul class="navbar-nav me-auto">
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a></li>
            <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/view/librarian/registerBook.jsp">Cadastrar</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/removeBook.jsp">Remover</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/addCopies.jsp">Exemplares</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/searchBooks.jsp">Buscar</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/manageAuthors.jsp">Autores</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/manageCategories.jsp">Categorias</a></li>
          </ul>
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair"><i class="bi bi-box-arrow-right"></i> Sair</a>
        </div>
      </div>
    </nav>
  </header>

  <main class="container py-4">

    <nav class="breadcrumb-atlas mb-4">
      <a href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a>
      <span> / </span>
      <span style="color: #6B1A2B;">Cadastrar livro</span>
    </nav>

    <div class="mb-5">
      <h1 style="font-weight: 700; color: #212529;">Cadastrar livro</h1>
      <p class="text-muted">Cadastro para registrar livros no sistema.</p>
    </div>

    <% String msg = request.getParameter("msg"); %>
    <% if ("book_added".equals(msg)) { %>
      <div class="alert alert-success alert-dismissible fade show" role="alert">
        Livro cadastrado com sucesso!
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    <% } else if (msg != null && msg.contains("error")) { %>
      <div class="alert alert-danger alert-dismissible fade show" role="alert">
        Erro: <%= request.getParameter("detail") != null ? request.getParameter("detail") : "Falha ao cadastrar." %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    <% } %>

    <div class="row justify-content-center">
      <div class="col-lg-7">
        <form action="${pageContext.request.contextPath}/librarian/action" method="post" class="row g-4">

          <input type="hidden" name="action" value="registerBook">

          <div class="col-12">
            <label for="name" class="form-label">Título</label>
            <input type="text" id="name" name="name" class="form-control" placeholder="Insira o título" required/>
          </div>

          <div class="col-12">
            <label for="pages" class="form-label">Número de páginas</label>
            <input type="number" id="pages" name="pages" min="1" class="form-control" placeholder="Insira a quantidade de páginas" required/>
          </div>

          <div class="col-12">
            <label for="authors" class="form-label">Autor</label>
            <input type="text" id="authors" name="authors" class="form-control" placeholder="Insira o nome do autor (separe por vírgula)" required/>
          </div>

          <div class="col-12">
            <label for="publisher" class="form-label">Editora</label>
            <input type="text" id="publisher" name="publisher" class="form-control" placeholder="Insira a editora" required/>
          </div>

          <div class="col-12">
            <label for="categoryId" class="form-label">Categoria</label>
            <select id="categoryId" name="categoryId" class="form-select" required onchange="toggleNewCategory(this)">
              <option value="" selected disabled>Selecione...</option>
              <% for (Category cat : categories) { %>
                <option value="<%= cat.getCategoryId() %>"><%= cat.getCategoryName() %></option>
              <% } %>
              <option value="outra">+ Outra (digitar nova)</option>
            </select>

            <div id="newCategoryDiv">
              <input type="text" id="newCategoryName" name="newCategoryName"
                     class="form-control mt-2"
                     placeholder="Digite o nome da nova categoria"/>
            </div>
          </div>

          <div class="col-12">
            <label for="copies" class="form-label">Exemplares</label>
            <input type="number" id="copies" name="copies" min="1" class="form-control" placeholder="Quantidade de exemplares" required/>
          </div>

          <div class="col-md-6">
            <label class="form-label">Estante</label>
            <select name="shelf" class="form-select" required>
              <option value="" selected disabled>Selecione a estante</option>
              <option value="A">A</option>
              <option value="B">B</option>
              <option value="C">C</option>
              <option value="D">D</option>
            </select>
          </div>

          <div class="col-md-6">
            <label class="form-label">Prateleira</label>
            <select name="rack" class="form-select" required>
              <option value="" selected disabled>Selecione a prateleira</option>
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
            </select>
          </div>

          <div class="col-12 text-center mt-5">
            <button type="submit" class="btn-finalizar">Finalizar cadastro</button>
          </div>

        </form>
      </div>
    </div>

  </main>

  <footer class="text-center py-4 mt-5 text-muted" style="font-size: 0.8rem; border-top: 1px solid #eee;">
    © 2026 Atlas. Todos os direitos reservados.
  </footer>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

  <script>
    function toggleNewCategory(select) {
      const newCategoryDiv = document.getElementById('newCategoryDiv');
      const newCategoryInput = document.getElementById('newCategoryName');

      if (select.value === 'outra') {
        newCategoryDiv.style.display = 'block';
        newCategoryInput.required = true;
      } else {
        newCategoryDiv.style.display = 'none';
        newCategoryInput.required = false;
        newCategoryInput.value = '';
      }
    }
  </script>

</body>
</html>