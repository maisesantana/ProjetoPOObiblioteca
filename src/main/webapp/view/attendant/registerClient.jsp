<%@ page import="br.com.atlas.model.Attendant" %>

<%
    Object user = session.getAttribute("userLogged");
    if(user == null || !(user instanceof Attendant)){
        response.sendRedirect(request.getContextPath() + "/view/login.jsp?msg=unauthorized");
        return;
    }
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Cadastrar Cliente</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/attendant/clientForm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
</head>
<body>
  <header>
    <nav class="navbar navbar-expand-lg atlas-navbar">
      <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">
          <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas"/>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarAtlas">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarAtlas">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">Início</a></li>
            <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/clients">Clientes</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/loan.jsp">Empréstimo</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/returnBook.jsp">Devolução</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/renewal.jsp">Renovação</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/searchBooks">Buscar Livros</a></li>
            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/activeLoans.jsp">Empréstimos Ativos</a></li>
          </ul>
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair"><i class="bi bi-box-arrow-right"></i> Sair</a>
        </div>
      </div>
    </nav>
  </header>

  <main class="container-fluid form-container">
    <nav class="breadcrumb-custom">
      <a href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">Início</a>
      <span>/</span>
      <a href="${pageContext.request.contextPath}/clients">Clientes</a>
      <span>/</span>
      <span class="current">Cadastrar</span>
    </nav>
    <section class="titulo-section">
      <h1>Cadastrar Cliente</h1>
      <p>Preencha os dados para registrar um novo cliente</p>
    </section>

    <form action="${pageContext.request.contextPath}/clients" method="post">
      <input type="hidden" name="action" value="register">

      <div class="form-group">
        <label for="cpf">CPF</label>
        <input type="text" id="cpf" name="cpf" placeholder="000.000.000-00" maxlength="14" required/>
      </div>
      <div class="form-group">
        <label for="name">Nome completo</label>
        <input type="text" id="name" name="name" placeholder="Insira o nome" required/>
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" placeholder="Insira o e-mail" required/>
      </div>
      <div class="form-group">
        <label for="gender">Gênero</label>
        <select id="gender" name="gender" required>
          <option value="" disabled selected>Selecione...</option>
          <option value="M">Masculino</option>
          <option value="F">Feminino</option>
          <option value="O">Outro</option>
        </select>
      </div>
      <div class="form-group">
        <label for="birthDate">Data de nascimento</label>
        <input type="date" id="birthDate" name="birthDate" required/>
      </div>
      <div class="form-group">
        <label for="address">Endereço</label>
        <input type="text" id="address" name="address" placeholder="Insira o endereço" required/>
      </div>

      <div class="form-actions">
        <button type="submit" class="btn-submit">Cadastrar</button>
        <a href="${pageContext.request.contextPath}/clients" class="btn-cancel-link">Cancelar</a>
      </div>
    </form>
  </main>

  <jsp:include page="/view/footer.jsp"/>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
