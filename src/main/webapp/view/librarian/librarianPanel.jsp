<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Atlas — Painel do Bibliotecário</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@24,400,0,0"/>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/admTools.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
</head>

<body>

    <header>
        <nav class="navbar navbar-expand-lg atlas-navbar">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">
                    <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas — Gestão de Biblioteca"/>
                </a>

                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarAtlas">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarAtlas">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/view/librarian/librarianPanel.jsp">Início</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/registerBook.jsp">Cadastrar</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/removeBook">Remover</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/view/librarian/addCopies.jsp">Exemplares</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/searchBooks">Buscar</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/manageAuthors">Autores</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/manageCategory">Categorias</a>
                        </li>
                    </ul>

                    <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
                        <i class="bi bi-box-arrow-right"></i> Sair
                    </a>
                </div>
            </div>
        </nav>
    </header>

    <div class="container-fluid">
        <div class="row">
            <main class="col-12 pt-4 px-5">
                <section class="ferramentas-admin">
                    <div class="ferramentas-titulo">
                        <span>BIBLIOTECÁRIO</span>
                        <h2>Gestão de Acervo</h2>
                    </div>

                    <div class="row justify-content-center text-center g-5">

                        <div class="col-lg-4 col-md-6">
                            <a href="${pageContext.request.contextPath}/view/librarian/registerBook.jsp" class="tool-card">
                                <div class="tool-icon">
                                    <span class="material-symbols-rounded">library_add</span>
                                </div>
                                <h5>Cadastrar Livro</h5>
                                <p>Adicione novas obras literárias ao acervo.</p>
                            </a>
                        </div>

                        <div class="col-lg-4 col-md-6">
                            <a href="${pageContext.request.contextPath}/removeBook" class="tool-card">
                                <div class="tool-icon">
                                    <span class="material-symbols-rounded">delete</span>
                                </div>
                                <h5>Remover Livro</h5>
                                <p>Remova livros do acervo.</p>
                            </a>
                        </div>

                        <div class="col-lg-4 col-md-6">
                            <a href="${pageContext.request.contextPath}/view/librarian/addCopies.jsp" class="tool-card">
                                <div class="tool-icon">
                                    <span class="material-symbols-rounded">library_books</span>
                                </div>
                                <h5>Adicionar Exemplares</h5>
                                <p>Aumente a quantidade de cópias disponíveis.</p>
                            </a>
                        </div>

                        <div class="col-lg-4 col-md-6">
                            <a href="${pageContext.request.contextPath}/searchBooks" class="tool-card">
                                <div class="tool-icon">
                                    <span class="material-symbols-rounded">search</span>
                                </div>
                                <h5>Buscar Livros</h5>
                                <p>Consulte por títulos ou autores.</p>
                            </a>
                        </div>

                        <div class="col-lg-4 col-md-6">
                            <a href="${pageContext.request.contextPath}/manageAuthors" class="tool-card">
                                <div class="tool-icon">
                                    <span class="material-symbols-rounded">person_edit</span>
                                </div>
                                <h5>Gerenciar Autores</h5>
                                <p>Cadastre e edite informações sobre os autores das obras.</p>
                            </a>
                        </div>

                        <div class="col-lg-4 col-md-6">
                            <a href="${pageContext.request.contextPath}/manageCategory" class="tool-card">
                                <div class="tool-icon">
                                    <span class="material-symbols-rounded">category</span>
                                </div>
                                <h5>Gerenciar Categorias</h5>
                                <p>Organize o acervo por Categorias.</p>
                            </a>
                        </div>

                    </div>
                </section>
            </main>
        </div>
    </div>
    <jsp:include page="/view/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>