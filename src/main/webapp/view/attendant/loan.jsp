<%@ page import="br.com.atlas.model.Attendant" %>
<%@ page import="br.com.atlas.model.Book" %>

<%
    Object user = session.getAttribute("userLogged");

    if(user == null || !(user instanceof Attendant)){
        response.sendRedirect(request.getContextPath() + "/view/index.jsp?msg=unauthorized");
        return;
    }

    Attendant attendant = (Attendant) user;

    // Livro enviado pelo LoanController quando o usuário
    // clicou em "Emprestar este livro" na ViewBook
    Book selectedBook = (Book) request.getAttribute("selectedBook");

    // Mensagens de retorno
    String msg = request.getParameter("msg");
    String detail = request.getParameter("detail");
%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Atlas - Empréstimo</title>

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

  <link href="https://fonts.googleapis.com/css2?family=Work+Sans:wght@400;500;600&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">

  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@24,400,0,0"/>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin/navbarAdm.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/attendant/loan.css"/>
</head>

<body>

<header>
  <nav class="navbar navbar-expand-lg atlas-navbar">
    <div class="container-fluid">

      <a class="navbar-brand"
         href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">

        <img src="${pageContext.request.contextPath}/assets/images/logo.png"
             alt="Atlas — Gestão de Biblioteca"/>
      </a>

      <button class="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarAtlas">

        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarAtlas">

        <ul class="navbar-nav me-auto mb-2 mb-lg-0">

          <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">
              Início
            </a>
          </li>

          <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/clients">
              Clientes
            </a>
          </li>

          <li class="nav-item">
             <a class="nav-link" href="${pageContext.request.contextPath}/view/attendant/loan.jsp">Empréstimo</a>
          </li>

          <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/view/attendant/returnBook.jsp">
              Devolução
            </a>
          </li>

          <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/view/attendant/renewal.jsp">
              Renovação
            </a>
          </li>

          <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/searchBooks">
              Buscar Livros
            </a>
          </li>

          <li class="nav-item">
            <a class="nav-link"
               href="${pageContext.request.contextPath}/activeLoans">
              Empréstimos Ativos
            </a>
          </li>

        </ul>

        <a href="${pageContext.request.contextPath}/logout"
           class="btn-sair">

          <i class="bi bi-box-arrow-right"></i> Sair
        </a>

      </div>
    </div>
  </nav>
</header>

<main class="loan-page">

  <div class="container loan-container">

    <div class="breadcrumb-custom">
      <a href="${pageContext.request.contextPath}/view/attendant/attendantPanel.jsp">
        Início
      </a>
      <span>/</span>

      <span class="current-page">
        Empréstimo
      </span>
    </div>

    <div class="title-section">

      <h1>
        Realizar empréstimo
      </h1>

      <p>
        Busque um livro e verifique os dados do cliente para concluir o empréstimo.
      </p>

    </div>

    <%-- Mensagem de sucesso --%>
    <% if("success".equals(msg)){ %>
      <div class="alert alert-success">
        Empréstimo realizado com sucesso!
      </div>
    <% } %>

    <%-- Mensagem de erro --%>
    <% if("error".equals(msg)){ %>
      <div class="alert alert-danger">
        <strong>Erro:</strong>
        <%= detail != null ? detail : "Não foi possível realizar o empréstimo." %>
      </div>
    <% } %>

    <div class="search-book-box">
      <input 
        type="text"
        id="bookSearchInput"
        class="form-control"
        placeholder="Digite o nome do livro ou autor"
        autocomplete="off">
      <button type="button" class="btn-search-book" onclick="searchBooks()">
        Buscar livro
      </button>
    </div>

    <!-- Resultados da busca de livros -->
    <div id="bookResults" style="display: none; margin-top: 20px;">
      <h4>Resultados da busca</h4>
      <div id="bookList" class="list-group"></div>
    </div>

    <div class="loan-card">

      <%-- Se veio da ViewBook, mostramos o livro selecionado --%>
      <% if(selectedBook != null){ %>

      <div class="book-summary">

        <div class="book-icon">
          <i class="bi bi-book-half"></i>
        </div>

        <div class="book-content">

          <div class="book-header">

            <div>
              <h2>Livro selecionado</h2>

              <span>
                <%= selectedBook.getBookName() %>
              </span>
            </div>

            <span class="available-badge">
              Selecionado
            </span>
          </div>
        </div>
      </div>
      <% } %>

<div id="selectedBookInfo" class="selected-book-info<%= selectedBook != null ? " visible" : "" %>">
        <h5>Livro Selecionado</h5>
        <p><strong>Nome:</strong> <span id="selectedBookName">-</span></p>
        <p><strong>Autores:</strong> <span id="selectedBookAuthors">-</span></p>
        <p><strong>Exemplares Disponíveis:</strong> <span id="selectedBookAvailable">-</span></p>
        <input type="hidden" id="selectedBookIdField" value="<%= selectedBook != null ? selectedBook.getBookId() : "" %>">
        <input type="hidden" id="selectedCopyId" value="">
      </div>

      <div class="client-section">
        <%-- Formulário que envia para o LoanController --%>
        <%-- O mesmo servlet /loan trata busca de clientes, registro de empréstimo e busca de livros via action. --%>
        <form id="loanForm" action="${pageContext.request.contextPath}/loan" method="post">
          <h3>
            Buscar cliente
          </h3>
          <div class="client-search">
            <input type="text"
                   id="cpfInput"
                   name="cpf"
                   class="form-control"
                   placeholder="Digite o CPF do cliente"
                   autocomplete="off">
            <button type="button"
                    class="btn-check"
                    onclick="searchClient()">
              Verificar
            </button>
          </div>

          <%-- Exemplar que será emprestado (oculto) --%>
          <input type="hidden"
                 id="copyIdInput"
                 name="copyId"
                 value="">

          <%-- Card de cliente --%>
          <div id="clientCardContainer" style="display: none; margin-top: 20px;">
            <div class="client-card">
              <div class="client-avatar">
                <i class="bi bi-person"></i>
              </div>
              <div class="client-info">
                <div class="client-name" id="clientName">
                  -
                </div>
                <div class="client-status" id="clientStatus">
                  <i class="bi bi-question-circle"></i>
                  Aguardando verificação
                </div>
              </div>
            </div>
          </div>

          <%-- Mensagem de erro do cliente --%>
          <div id="clientErrorMessage" style="display: none; color: #dc3545; margin-top: 10px;"></div>

          <%-- Botão de confirmação --%>
          <button type="submit"
                  id="confirmBtn"
                  class="btn-confirm"
                  style="display: none; margin-top: 20px;"
                  onclick="submitLoan()">
            Confirmar empréstimo
          </button>
        </form>
      </div>
    </div>
  </div>
</main>
<jsp:include page="/view/footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
  const contextPath = '${pageContext.request.contextPath}';

  // Variáveis de estado
  let selectedBook = null;
  let selectedClient = null;
  let selectedCopyId = null;

  // Inicializar se livro veio da ViewBook
  document.addEventListener('DOMContentLoaded', function() {
    const bookIdField = document.getElementById('selectedBookIdField');
    if (bookIdField.value) {
      initializeWithBook(parseInt(bookIdField.value));
    }
  });

  /**
   * Inicializa a página com um livro já selecionado (vindo da ViewBook)
   */
  function initializeWithBook(bookId) {
    const formData = new FormData();
    formData.append('action', 'searchBook');
    formData.append('q', ''); // Será filtrado por ID no servidor

    fetch(`${contextPath}/loan`, {
      method: 'POST',
      body: formData
    })
    .then(response => response.json())
    .then(books => {
      const book = books.find(b => b.bookId === bookId);
      if (book) {
        selectBook(book);
      }
    })
    .catch(error => console.error('Erro ao carregar livro:', error));
  }

  /**
   * Busca livros por nome/autor
   */
  function searchBooks() {
    const query = document.getElementById('bookSearchInput').value.trim();

    if (!query) {
      alert('Digite o nome do livro ou autor para buscar');
      return;
    }

    // Busca de livros é feita via POST para /loan com action=searchBook.
    // O LoanController redireciona essa ação para searchBook().
    const formData = new FormData();
    formData.append('action', 'searchBook');
    formData.append('q', query);

    fetch(`${contextPath}/loan`, {
      method: 'POST',
      body: formData
    })
    .then(response => response.json())
    .then(books => {
      displayBookResults(books);
    })
    .catch(error => {
      console.error('Erro ao buscar livros:', error);
      alert('Erro ao buscar livros');
    });
  }

  /**
   * Exibe resultados da busca de livros
   */
  function displayBookResults(books) {
    const resultsContainer = document.getElementById('bookResults');
    const bookList = document.getElementById('bookList');

    if (books.length === 0) {
      bookList.innerHTML = '<p class="text-muted">Nenhum livro com exemplares disponíveis encontrado.</p>';
      resultsContainer.style.display = 'block';
      return;
    }

    bookList.innerHTML = '';
    books.forEach(book => {
      const bookElement = document.createElement('div');
      bookElement.className = 'list-group-item list-group-item-action';
      bookElement.style.cursor = 'pointer';
      bookElement.innerHTML = `
        <div class="d-flex w-100 justify-content-between align-items-start">
          <div class="flex-grow-1">
            <h6 class="mb-1"><strong>${book.bookName}</strong></h6>
            <p class="mb-1"><small>${book.authors}</small></p>
            <small class="text-muted">
              ${book.availableCopies} de ${book.totalCopies} exemplares disponíveis
            </small>
          </div>
          <span class="badge bg-success">${book.availableCopies}</span>
        </div>
      `;
      bookElement.onclick = () => selectBook(book);
      bookList.appendChild(bookElement);
    });

    resultsContainer.style.display = 'block';
  }

  /**
   * Seleciona um livro
   */
  function selectBook(book) {
    selectedBook = book;
    selectedCopyId = book.copies[0].copyId; // Pega primeiro exemplar disponível

    // Atualiza interface
    document.getElementById('selectedBookName').textContent = book.bookName;
    document.getElementById('selectedBookAuthors').textContent = book.authors;
    document.getElementById('selectedBookAvailable').textContent = book.availableCopies;
    document.getElementById('selectedBookIdField').value = book.bookId;
    document.getElementById('selectedCopyId').value = selectedCopyId;
    document.getElementById('copyIdInput').value = selectedCopyId;

    // Mostra informações do livro selecionado usando a classe CSS
    document.getElementById('selectedBookInfo').classList.add('visible');

    // Esconde resultados
    document.getElementById('bookResults').style.display = 'none';

    // Limpa campo de busca
    document.getElementById('bookSearchInput').value = '';

    console.log('Livro selecionado:', book);
  }

  /**
   * Busca cliente por CPF
   */
  function searchClient() {
    const cpf = document.getElementById('cpfInput').value.trim();

    if (!cpf) {
      alert('Digite o CPF do cliente');
      return;
    }

    if (!selectedBook) {
      alert('Selecione um livro antes de buscar o cliente');
      return;
    }

    const formData = new FormData();
    formData.append('action', 'searchClient');
    formData.append('cpf', cpf);

    fetch(`${contextPath}/loan`, {
      method: 'POST',
      body: formData
    })
    .then(response => response.json())
    .then(data => {
      handleClientSearchResult(data);
    })
    .catch(error => {
      console.error('Erro ao buscar cliente:', error);
      alert('Erro ao buscar cliente');
    });
  }

  /**
   * Processa resultado da busca de cliente
   */
  function handleClientSearchResult(data) {
    const clientCardContainer = document.getElementById('clientCardContainer');
    const clientErrorMessage = document.getElementById('clientErrorMessage');
    const confirmBtn = document.getElementById('confirmBtn');
    const clientNameEl = document.getElementById('clientName');
    const clientStatusEl = document.getElementById('clientStatus');

    clientCardContainer.style.display = 'block';

    if (!data.found) {
      clientNameEl.textContent = data.error;
      clientStatusEl.innerHTML = '<i class="bi bi-x-circle-fill" style="color: #dc3545;"></i> Não encontrado';
      clientStatusEl.style.color = '#dc3545';
      clientErrorMessage.textContent = data.error;
      clientErrorMessage.style.display = 'block';
      confirmBtn.style.display = 'none';
      return;
    }

    selectedClient = data;
    clientNameEl.textContent = data.name;
    clientErrorMessage.style.display = 'none';

    if (data.canBorrow) {
      clientStatusEl.innerHTML = '<i class="bi bi-check-circle-fill"></i> Cliente liberado para empréstimo';
      clientStatusEl.style.color = '#28a745';
      confirmBtn.style.display = 'block';
    } else {
      clientStatusEl.innerHTML = '<i class="bi bi-x-circle-fill"></i> Cliente suspenso';
      clientStatusEl.style.color = '#dc3545';
      clientErrorMessage.textContent = data.suspensionReason || 'Cliente não pode fazer empréstimo';
      clientErrorMessage.style.display = 'block';
      confirmBtn.style.display = 'none';
    }
  }

  /**
   * Submete o formulário de empréstimo
   */
  function submitLoan() {
    if (!selectedClient || !selectedClient.canBorrow) {
      alert('Cliente não pode fazer empréstimo');
      return;
    }

    if (!selectedBook || !selectedCopyId) {
      alert('Selecione um livro e exemplar');
      return;
    }

    document.getElementById('loanForm').submit();
  }

  // Permite buscar pressionando Enter
  document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('bookSearchInput').addEventListener('keypress', function(event) {
      if (event.key === 'Enter') {
        event.preventDefault();
        searchBooks();
      }
    });

    document.getElementById('cpfInput').addEventListener('keypress', function(event) {
      if (event.key === 'Enter') {
        event.preventDefault();
        searchClient();
      }
    });

    // Se já temos um livro selecionado (viewBook → loan)
    const bookIdValue = document.getElementById('selectedBookIdField').value;
    if (bookIdValue) {
      document.getElementById('selectedBookInfo').classList.add('visible');
    }
  });
</script>

</body>
</html>
