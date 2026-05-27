<%@ page import="br.com.atlas.model.Attendant" %>
<%@ page import="java.util.List" %>
<%@ page import="br.com.atlas.model.Loan" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    Object user = session.getAttribute("userLogged");
    if (user == null || !(user instanceof Attendant)) {
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
        return;
    }
    List<Loan> loans = (List<Loan>) request.getAttribute("loans");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate today = LocalDate.now();
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Atlas - Empréstimos Ativos</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
        href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap"
        rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/attendant/activeLoans.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css" />
</head>

<body>
    <header>
        <nav class="navbar navbar-expand-lg atlas-navbar">
            <div class="container-fluid">
                <a class="navbar-brand"
                    href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">
                    <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas" />
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarAtlas">
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
                            <a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/loan.jsp">Empréstimo</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/returnBook.jsp">Devolução</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/attendant/action?action=viewRenewal&loanId=1">Renovação</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/searchBooks">Buscar Livros</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/activeLoans">Empréstimos Ativos</a>
                        </li>
                    </ul>
                    <a href="${pageContext.request.contextPath}/logout" class="btn-sair">
                        <i class="bi bi-box-arrow-right"></i> Sair
                    </a>
                </div>
            </div>
        </nav>
    </header>

    <main class="container-fluid loans-container">
        <nav class="breadcrumb-custom">
            <a href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">Início</a>
            <span>/</span>
            <span class="current-page">Empréstimos Ativos</span>
        </nav>

        <section class="titulo-section">
            <h1>Acompanhe todos os empréstimos ativos do sistema</h1>
            <p>Gestão de Empréstimos</p>
        </section>

        <%
            int totalLoans = (loans != null) ? loans.size() : 0;
        %>
        <div class="loans-summary">
            <span class="summary-badge"><%= totalLoans %></span>
            <span class="summary-label">
                <%= totalLoans == 1 ? "empréstimo ativo encontrado" : "empréstimos ativos encontrados" %>
            </span>
        </div>

        <section class="loans-list">
            <% if (loans != null && !loans.isEmpty()) { %>
                <% for (Loan loan : loans) {
                    LocalDate dueDate = loan.getExpectedReturnDate().toLocalDate();
                    boolean isOverdue = dueDate != null && dueDate.isBefore(today);
                    String dueCssClass = isOverdue ? "tag-overdue" : "tag-due";
                    String dueIcon = isOverdue ? "bi-exclamation-circle" : "bi-calendar-check";
                    String dueDateText = dueDate != null ? dueDate.format(formatter) : "—";
                    String borrowedDateText = loan.getLoanDate().toLocalDate() != null
                            ? loan.getLoanDate().toLocalDate().format(formatter) : "—";
                %>
                <div class="loan-card">
                    <div class="loan-icon">
                        <i class="bi bi-book-half"></i>
                    </div>
                    <div class="loan-info">
                        <div class="loan-main">
                            <h3><%= loan.getBookCopy().getBook().getBookName() %></h3>
                            <span class="loan-client">
                                <i class="bi bi-person-fill"></i><%= loan.getClient().getName() %>
                            </span>
                        </div>
                        <div class="loan-meta">
                            <span class="meta-tag tag-borrowed">
                                <i class="bi bi-calendar2-event"></i>
                                Retirada: <%= borrowedDateText %>
                            </span>
                            <span class="meta-tag <%= dueCssClass %>">
                                <i class="bi <%= dueIcon %>"></i>
                                Devolução: <%= dueDateText %>
                            </span>
                            <span class="meta-tag tag-renewals">
                                <i class="bi bi-arrow-repeat"></i>
                                <%= loan.getRenewalTotal() %> <%= loan.getRenewalTotal() == 1 ? "renovação" : "renovações" %>
                            </span>
                        </div>
                    </div>
                </div>
                <% } %>
            <% } else { %>
                <div class="empty-state">
                    <i class="bi bi-inbox"></i>
                    <p>Nenhum empréstimo ativo no momento.</p>
                </div>
            <% } %>
        </section>
    </main>

    <jsp:include page="/view/footer.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
