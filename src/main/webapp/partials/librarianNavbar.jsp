<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
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
            <a class="nav-link" href="${pageContext.request.contextPath}/librarian/action?action=removeBook">Remover</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/librarian/action?action=searchBooks">Buscar</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/librarian/action?action=manageAuthors">Autores</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/librarian/action?action=manageCategory">Categorias</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/librarian/action?action=addCopies">Exemplares</a>
          </li>
        </ul>
        <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
          <i class="bi bi-box-arrow-right"></i> Sair
        </a>
      </div>
    </div>
  </nav>
</header>