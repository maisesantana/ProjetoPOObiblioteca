<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas — Painel do Administrador</title>

  <!-- 1) Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <!-- 2) Bootstrap Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
  <!-- 3) Navbar Atlas — APÓS o Bootstrap para sobrescrever corretamente -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/navbar.css"/>
</head>
<body>

  <!-- NAVBAR — usa Bootstrap + classe atlas-navbar
       O CSS em navbar.css sobrescreve apenas
       o necessário via .navbar.atlas-navbar -->
  <header>
    <nav class="navbar navbar-expand-lg atlas-navbar">
      <div class="container-fluid">
        <!-- Logo -->
        <a class="navbar-brand" href="#">
          <img
            src="${pageContext.request.contextPath}/assets/images/logo.png"
            alt="Atlas — Gestão de Biblioteca"
          />
        </a>
        <!-- Toggler mobile -->
        <button class="navbar-toggler" type="button"
                data-bs-toggle="collapse" data-bs-target="#navbarAtlas"
                aria-controls="navbarAtlas" aria-expanded="false"
                aria-label="Alternar navegação">
          <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Links + botão -->
        <div class="collapse navbar-collapse" id="navbarAtlas">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a class="nav-link active" aria-current="page" href="#">Início</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Clientes</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Empréstimos</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Acervo</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Localização</a>
            </li>
          </ul>

          <!-- Botão Sair (terciária #80A4ED) -->
          <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
            <i class="bi bi-box-arrow-right"></i>
            Sair
          </a>
        </div>

      </div>
    </nav>
  </header>
  <!-- ═══════════════════════════════════════ -->

  <div class="container-fluid">
    <div class="row">
      <main class="col-md-10 pt-4 px-4">

        <h4 class="mb-1">Bem-vindo, Administrador!</h4>
        <p class="text-muted mb-4">Selecione uma ação abaixo para gerenciar o sistema.</p>

        <!-- GESTÃO DE FUNCIONÁRIOS -->
        <h5 class="mb-3">Gestão de Funcionários</h5>
        <div class="row g-3 mb-4">

          <div class="col-md-3">
            <div class="card h-100">
              <div class="card-body">
                <h6 class="card-title"><i class="bi bi-person-plus-fill"></i> Cadastrar Funcionário</h6>
                <p class="card-text text-muted small">Adicionar novo bibliotecário ou atendente.</p>
                <a href="registerEmployee.jsp" class="btn btn-primary btn-sm">Acessar</a>
              </div>
            </div>
          </div>

          <div class="col-md-3">
            <div class="card h-100">
              <div class="card-body">
                <h6 class="card-title"><i class="bi bi-people-fill"></i> Listar Funcionários</h6>
                <p class="card-text text-muted small">Ver todos os funcionários cadastrados.</p>
                <a href="listEmployees.jsp" class="btn btn-primary btn-sm">Acessar</a>
              </div>
            </div>
          </div>

          <div class="col-md-3">
            <div class="card h-100">
              <div class="card-body">
                <h6 class="card-title"><i class="bi bi-pencil-square"></i> Editar Funcionário</h6>
                <p class="card-text text-muted small">Atualizar dados de um funcionário existente.</p>
                <a href="editEmployee.jsp" class="btn btn-primary btn-sm">Acessar</a>
              </div>
            </div>
          </div>

          <div class="col-md-3">
            <div class="card h-100">
              <div class="card-body">
                <h6 class="card-title"><i class="bi bi-person-dash-fill"></i> Remover Funcionário</h6>
                <p class="card-text text-muted small">Excluir um funcionário do sistema.</p>
                <a href="removeEmployee.jsp" class="btn btn-danger btn-sm">Acessar</a>
              </div>
            </div>
          </div>

        </div>

        <!-- BUSCA -->
        <h5 class="mb-3">Buscar Funcionário</h5>
        <div class="row g-3 mb-4">
          <div class="col-md-6">
            <div class="card h-100">
              <div class="card-body">
                <h6 class="card-title"><i class="bi bi-search"></i> Buscar por CPF</h6>
                <p class="card-text text-muted small">Encontrar um funcionário pelo CPF.</p>
                <a href="findEmployee.jsp" class="btn btn-secondary btn-sm">Acessar</a>
              </div>
            </div>
          </div>
        </div>

        <!-- LISTAGENS POR CARGO -->
        <h5 class="mb-3">Listagens por Cargo</h5>
        <div class="row g-3 mb-4">

          <div class="col-md-4">
            <div class="card h-100">
              <div class="card-body">
                <h6 class="card-title"><i class="bi bi-person-badge-fill"></i> Listar Atendentes</h6>
                <p class="card-text text-muted small">Ver todos os atendentes cadastrados.</p>
                <a href="listAttendants.jsp" class="btn btn-secondary btn-sm">Acessar</a>
              </div>
            </div>
          </div>

          <div class="col-md-4">
            <div class="card h-100">
              <div class="card-body">
                <h6 class="card-title"><i class="bi bi-book-fill"></i> Listar Bibliotecários</h6>
                <p class="card-text text-muted small">Ver todos os bibliotecários cadastrados.</p>
                <a href="listLibrarians.jsp" class="btn btn-secondary btn-sm">Acessar</a>
              </div>
            </div>
          </div>

          <div class="col-md-4">
            <div class="card h-100">
              <div class="card-body">
                <h6 class="card-title"><i class="bi bi-shield-fill"></i> Listar Administradores</h6>
                <p class="card-text text-muted small">Ver todos os administradores do sistema.</p>
                <a href="listAdmins.jsp" class="btn btn-secondary btn-sm">Acessar</a>
              </div>
            </div>
          </div>

        </div>

      </main>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
