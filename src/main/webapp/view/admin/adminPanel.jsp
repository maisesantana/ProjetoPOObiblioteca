<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas — Painel do Administrador</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
</head>
<body>

  <!-- NAVBAR -->
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">ATLAS</a>
      <span class="navbar-text text-white-50 ms-2">Sistema de Biblioteca</span>
      <div class="ms-auto d-flex align-items-center gap-3">
        <span class="navbar-text text-white-50">Administrador</span>
        <a href="login.jsp" class="btn btn-outline-light btn-sm">
          <i class="bi bi-box-arrow-right"></i> Sair
        </a>
      </div>
    </div>
  </nav>

  <div class="container-fluid">
    <div class="row">

      <!-- SIDEBAR -->
      <nav class="col-md-2 bg-dark min-vh-100 pt-3">
        <ul class="nav flex-column">

          <li class="nav-item">
            <span class="nav-link text-white-50 text-uppercase small">Principal</span>
          </li>
          <li class="nav-item">
            <a class="nav-link text-white active" href="adminPanel.jsp">
              <i class="bi bi-grid-1x2-fill"></i> Dashboard
            </a>
          </li>

          <li class="nav-item mt-2">
            <span class="nav-link text-white-50 text-uppercase small">Funcionários</span>
          </li>
          <li class="nav-item">
            <a class="nav-link text-white-50" href="registerEmployee.jsp">
              <i class="bi bi-person-plus-fill"></i> Cadastrar
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link text-white-50" href="listEmployees.jsp">
              <i class="bi bi-people-fill"></i> Listar
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link text-white-50" href="editEmployee.jsp">
              <i class="bi bi-pencil-square"></i> Editar
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link text-white-50" href="removeEmployee.jsp">
              <i class="bi bi-person-dash-fill"></i> Remover
            </a>
          </li>

          <li class="nav-item mt-2">
            <span class="nav-link text-white-50 text-uppercase small">Sistema</span>
          </li>
          <li class="nav-item">
            <a class="nav-link text-white-50" href="../logout">
              <i class="bi bi-box-arrow-right"></i> Sair
            </a>
          </li>

        </ul>
      </nav>

      <!-- CONTEÚDO PRINCIPAL -->
      <main class="col-md-10 pt-4 px-4">

        <h4 class="mb-1">Bem-vindo, Administrador!</h4>
        <p class="text-muted mb-4">Selecione uma ação abaixo para gerenciar o sistema.</p>

        <!-- SEÇÃO: GESTÃO DE FUNCIONÁRIOS -->
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

        <!-- SEÇÃO: BUSCA -->
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

        <!-- SEÇÃO: LISTAGENS ESPECÍFICAS -->
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