<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

  <footer class="atlas-footer">

    <div class="footer-content">

      <div class="footer-social">

        <h3>Nossas redes</h3>

        <div class="social-icons">

          <a href="#">
            <i class="bi bi-twitter"></i>
          </a>

          <a href="#">
            <i class="bi bi-instagram"></i>
          </a>

          <a href="#">
            <i class="bi bi-facebook"></i>
          </a>

        </div>

      </div>

      <div class="footer-logo">

        <!-- Alterado: usa JSTL c:url para gerar o caminho da logo relativo ao contexto da aplicação -->
        <img src="<c:url value='/assets/images/logo.png'/>" alt="Atlas - Gestão de Biblioteca">

      </div>

    </div>

    <div class="footer-copy">
      <p>&copy; 2026 Atlas. Todos os direitos reservados.</p>
    </div>

  </footer>