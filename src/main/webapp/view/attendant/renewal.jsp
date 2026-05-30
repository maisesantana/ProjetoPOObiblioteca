<%@ page import="br.com.atlas.model.Attendant" %>

<%
    Object user = session.getAttribute("userLogged");
    if(user == null || !(user instanceof Attendant)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
        return;
    }
    Attendant attendant = (Attendant) user;
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Atlas — Renovação</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/attendant/renewal.css"/>
</head>

<body>

<header>
    <nav class="navbar navbar-expand-lg atlas-navbar">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas — Gestão de Biblioteca"/>
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarAtlas">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarAtlas">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">Início</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/clients">Clientes</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/loan">Empréstimo</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/returnBook.jsp">Devolução</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/view/attendant/renewal.jsp">Renovação</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/searchBooks">Buscar Livros</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/activeLoans">Empréstimos Ativos</a>
                    </li>
                </ul>
                <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
                    <i class="bi bi-box-arrow-right"></i> Sair
                </a>
            </div>
        </div>
    </nav>
</header>

<main>
    <section class="renewal-container">

        <nav class="breadcrumb-custom">
            <a href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">Início</a>
            <span>/</span>
            <span class="current-page">Renovação</span>
        </nav>

        <div class="titulo-section">
            <h1>Realizar Renovação</h1>
            <p>Confirme os dados abaixo para renovar o empréstimo.</p>
        </div>

        <div class="book-cover">
            <div class="book-icon">
                <i class="bi bi-book"></i>
            </div>
        </div>

        <div class="book-title-section">
            <h2>Resumo do livro</h2>
        </div>

        <div class="details-grid">
            <div class="detail-card">
                <div class="card-label">Nome</div>
                <div class="card-value">A vingança do...</div>
            </div>
            <div class="detail-card">
                <div class="card-label">Número de páginas</div>
                <div class="card-value">147</div>
            </div>
            <div class="detail-card">
                <div class="card-label">Autor</div>
                <div class="card-value">Carlos</div>
            </div>
            <div class="detail-card">
                <div class="card-label">Exemplares</div>
                <div class="card-value">147</div>
            </div>
            <div class="detail-card">
                <div class="card-label">Editora</div>
                <div class="card-value">Assus</div>
            </div>
            <div class="detail-card">
                <div class="card-label">Categoria</div>
                <div class="card-value">Romance</div>
            </div>
        </div>

        <div class="status-book">
            <i class="bi bi-check-circle-fill"></i>
            <div>
                <strong>Status do livro</strong>
                <span>Disponível</span>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/attendant/action" method="post">
            <input type="hidden" name="action" value="renewal">
            <input type="hidden" name="loanId" value="1">
            <button type="submit" class="btn-renew">Confirmar Renovação</button>
        </form>

    </section>
</main>

<jsp:include page="/view/footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
