<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Atlas - Biblioteca</title>

    <!-- GOOGLE FONTS -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&display=swap"
        rel="stylesheet">

    <!-- BOOTSTRAP -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- BOOTSTRAP ICONS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <!-- FONT AWESOME -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- FOOTER CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css" />

    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">

    <!-- ICONES DAS TECNOLOGIAS USADAS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/devicon.min.css">
</head>

<body>

    <!-- NAVBAR -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm fixed-top py-3">
        <div class="container">

            <a class="navbar-brand" href="#">
                <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas Logo" class="logo">
            </a>

            <button class="navbar-toggler border-0 shadow-none" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarAtlas">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarAtlas">

                <ul class="navbar-nav mx-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link active" href="#">Início</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="#sobre">Sobre</a>
                    </li>
                </ul>

                <a href="${pageContext.request.contextPath}/view/login.jsp" class="btn btn-atlas">
                    Login
                </a>

            </div>
        </div>
    </nav>

    <!-- MAIN -->
    <main>

        <!-- HERO -->
        <section class="hero-section">

            <div class="container">

                <div class="row align-items-center g-5">

                    <!-- TEXTO -->
                    <div class="col-lg-6">

                        <h1 class="hero-title">
                            <span>Busque.</span>
                            <span>Organize.</span>
                            <span>Transforme.</span>
                            Conhecimento ao alcance de todos.
                        </h1>

                        <p class="hero-description">
                            Sistema moderno para gerenciamento de biblioteca,
                            empréstimos, devoluções e organização de acervo.
                            Tudo de forma prática, rápida e eficiente.
                        </p>

                        <div class="d-flex gap-3 flex-wrap">

                            <a href="${pageContext.request.contextPath}/view/login.jsp"
                                class="btn btn-atlas btn-lg">
                                Entrar no sistema
                            </a>

                        </div>
                    </div>

                    <!-- IMAGENS -->
                    <div class="col-lg-6">

                        <div class="hero-images">

                            <div class="book-card card-1">
                                <img
                                    src="https://images.unsplash.com/photo-1541963463532-d68292c34b19?q=80&w=800&auto=format&fit=crop">
                            </div>

                            <div class="book-card card-2">
                                <img
                                    src="https://images.unsplash.com/photo-1512820790803-83ca734da794?q=80&w=800&auto=format&fit=crop">
                            </div>

                            <div class="book-card card-3">
                                <img
                                    src="https://images.unsplash.com/photo-1495446815901-a7297e633e8d?q=80&w=800&auto=format&fit=crop">
                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </section>

        </section>

        <!-- ========== SOBRE ========== -->
        <section class="about-section" id="sobre">
            <div class="container">

                <div class="section-header">
                    <span class="section-tag">SOBRE</span>
                    <h2>Nosso sistema</h2>
                </div>

                 <!-- POR QUE ATLAS -->
                <div class="row mb-4">
                    <div class="col-12">
                        <div class="atlas-name-card">
                            <div class="atlas-name-icon">
                            </div>
                            <div>
                                <h4>Por que Atlas?</h4>
                                <p>
                                    O nome Atlas foi inspirado tanto na figura mitológica que sustenta o mundo
                                    quanto nos grandes compêndios cartográficos que levam seu nome. Assim como
                                    um atlas reúne e organiza o conhecimento do mundo em um só lugar, nosso
                                    sistema busca centralizar e facilitar o acesso ao acervo da biblioteca de
                                    forma simples e eficiente.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row g-4">

                    <div class="col-lg-6">
                        <div class="info-card">
                            <div class="icon-box">
                                <i class="bi bi-book"></i>
                            </div>
                            <h4>O que o Atlas faz?</h4>
                            <p>
                                O Atlas é um sistema web de gerenciamento de bibliotecas,
                                desenvolvido para digitalizar e agilizar os processos de
                                empréstimo, devolução e organização do acervo de forma
                                prática, rápida e eficiente.
                            </p>
                        </div>
                    </div>

                    <div class="col-lg-6">
                        <div class="info-card">
                            <div class="icon-box">
                                <i class="bi bi-bullseye"></i>
                            </div>
                            <h4>Qual problema busca resolver?</h4>
                            <p>
                                Elimina a necessidade de controles manuais e processos
                                lentos em bibliotecas, centralizando o gerenciamento de
                                livros, usuários e empréstimos em uma plataforma
                                intuitiva e acessível.
                            </p>
                        </div>
                    </div>

                </div>
            </div>
        </section>

        <!-- FUNCIONALIDADES -->
        <section class="about-section about-section-white">
            <div class="container">

                <div class="section-header">
                    <span class="section-tag">FUNCIONALIDADES</span>
                    <h2>Principais funcionalidades</h2>
                </div>

                <div class="row g-3">
                    <div class="col-lg-6">
                        <div class="feature-item">
                            <span class="feature-dot"></span>
                            Cadastro e gerenciamento de livros
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="feature-item">
                            <span class="feature-dot"></span>
                            Cadastro de usuários e membros
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="feature-item">
                            <span class="feature-dot"></span>
                            Registro e gerenciamento de empréstimos
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="feature-item">
                            <span class="feature-dot"></span>
                            Controle de prazos e disponibilidade
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="feature-item">
                            <span class="feature-dot"></span>
                            Busca e filtragem do acervo
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="feature-item">
                            <span class="feature-dot"></span>
                            Autenticação de usuários
                        </div>
                    </div>
                </div>

            </div>
        </section>

        <!-- TECNOLOGIAS -->
        <section class="about-section">
            <div class="container">

                <div class="section-header">
                    <span class="section-tag">TECNOLOGIAS</span>
                    <h2>Tecnologias utilizadas</h2>
                </div>

                <div class="row g-3 justify-content-center">
                    <div class="col-auto"><span class="tech-tag"><i class="devicon-java-plain"></i> Java</span></div>
                    <div class="col-auto"><span class="tech-tag"><i class="devicon-java-plain"></i> JSP / Servlets</span></div>
                    <div class="col-auto"><span class="tech-tag"><i class="devicon-mysql-plain"></i> MySQL</span></div>
                    <div class="col-auto"><span class="tech-tag"><i class="devicon-mysql-plain"></i> JDBC</span></div>
                    <div class="col-auto"><span class="tech-tag"><i class="devicon-vscode-plain"></i> VS Code</span></div>
                    <div class="w-100"></div>
                    <div class="col-auto"><span class="tech-tag"><i class="devicon-bootstrap-plain"></i> Bootstrap 5</span></div>
                    <div class="col-auto"><span class="tech-tag"><i class="devicon-html5-plain"></i> HTML / CSS</span></div>
                    <div class="col-auto"><span class="tech-tag"><i class="devicon-tomcat-plain"></i> Apache Tomcat</span></div>
                    <div class="col-auto"><span class="tech-tag"><i class="bi bi-layers"></i> Arquitetura MVC</span></div>
                </div>

            </div>
        </section>

        <!-- EQUIPE -->
        <section class="about-section about-section-white">
            <div class="container">

                <div class="section-header">
                    <span class="section-tag">EQUIPE</span>
                    <h2>Equipe de desenvolvimento</h2>
                </div>

                <div class="row g-4 justify-content-center">

                    <div class="col-lg-4 col-md-6">
                        <div class="team-card">
                            <img src="${pageContext.request.contextPath}/assets/images/home/erica.jpg" alt="Erica Ellen">
                            <h4>Erica Ellen</h4>
                            <span>Desenvolvedor</span>
                        </div>
                    </div>

                    <div class="col-lg-4 col-md-6">
                        <div class="team-card">
                            <img src="${pageContext.request.contextPath}/assets/images/home/maise.jpg" alt="Maise Santana">
                            <h4>Maise Santana</h4>
                            <span>Desenvolvedor</span>
                        </div>
                    </div>

                    <div class="col-lg-4 col-md-6">
                        <div class="team-card">
                            <img src="${pageContext.request.contextPath}/assets/images/home/miqueias.jpg" alt="Miqueias Nogueira">
                            <h4>Miqueias Nogueira</h4>
                            <span>Desenvolvedor</span>
                        </div>
                    </div>

                    <div class="col-lg-4 col-md-6">
                        <div class="team-card">
                            <img src="${pageContext.request.contextPath}/assets/images/home/anaRode.jpg" alt="Ana Rode">
                            <h4>Ana Rode</h4>
                            <span>Desenvolvedor</span>
                        </div>
                    </div>

                    <div class="col-lg-4 col-md-6">
                        <div class="team-card">
                            <img src="${pageContext.request.contextPath}/assets/images/home/rayane.jpg" alt="Rayane Gabrielle">
                            <h4>Rayane Gabrielle</h4>
                            <span>Analista</span>
                        </div>
                    </div>

                    <div class="col-lg-4 col-md-6">
                        <div class="team-card">
                            <img src="${pageContext.request.contextPath}/assets/images/home/woquiton.jpg" alt="Woquiton Fernandes">
                            <h4>Woquiton Fernandes</h4>
                            <span>Professor Orientador</span>
                        </div>
                    </div>

                </div>
            </div>
        </section>

        <!-- INFORMAÇÃO ACADÊMICA -->
        <section class="about-section">
            <div class="container">
                <div class="academic-box">
                    <h2>Informação Acadêmica</h2>

                    <p style="text-align: justify;">
                        Este sistema foi desenvolvido como atividade prática da disciplina de
                        Linguagem de Programação Orientada a Objetos (LPOO),
                        no curso de Análise e Desenvolvimento de Sistemas, sob
                        orientação do professor <strong>Woquiton Fernandes</strong>.
                    </p>

                    <p style="text-align: justify;">
                        O objetivo do projeto é aplicar conceitos de programação orientada a objetos,
                        organização em camadas, banco de dados e desenvolvimento de sistemas web,
                        a partir de uma proposta criada pela equipe.
                    </p>

                    <p class="closing-text" style="text-align: justify;">
                        Projeto acadêmico desenvolvido para fins de aprendizagem,
                        integração de conhecimentos e prática de desenvolvimento de software.
                    </p>
                </div>
            </div>
        </section>

    </main>

    <!-- FOOTER -->
    <jsp:include page="/view/footer.jsp" />

    <!-- BOOTSTRAP JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>

</html>
