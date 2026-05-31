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

            <!-- SERVIÇOS -->
            <section class="services-section">

                <div class="container">

                    <div class="section-header text-center">
                        <span class="section-tag">SERVIÇOS</span>

                        <h2>
                            Nossos serviços para você
                        </h2>
                    </div>

                    <div class="row align-items-center g-5 mb-5">

                        <div class="col-lg-6">
                            <img src="https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?q=80&w=1200&auto=format&fit=crop"
                                class="service-image">
                        </div>

                        <div class="col-lg-6">

                            <h3 class="service-title">
                                Empréstimos rápidos e organizados
                            </h3>

                            <p class="service-text">
                                Gerencie livros, usuários e empréstimos
                                com uma plataforma intuitiva e eficiente,
                                feita para otimizar o fluxo da biblioteca.
                            </p>

                        </div>

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