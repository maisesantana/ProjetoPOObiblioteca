<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Login - Atlas</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
</head>
<body>

<div class="login-page">
    <header class="logo-header">
        <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Atlas Logo">
    </header>

    <main class="login-card">
        <nav class="breadcrumb">
            <a href="#" class="link-inicio">Início</a> <span>/ Login</span>
        </nav>

        <h1 class="login-title">Login</h1>
        <p class="login-subtitle">Digite seu CPF e senha</p>

        <% String msg = request.getParameter("msg"); 
           if(msg != null) { %>
            <div class="alert-error">
                <i class="fa-solid fa-circle-exclamation"></i>
                <% if(msg.equals("cpf_not_found")) { %> CPF não cadastrado no sistema.
                <% } else if(msg.equals("invalid_credentials")) { %> Senha incorreta.
                <% } else if(msg.equals("empty_fields")) { %> Preencha todos os campos.
                <% } else if(msg.equals("password_must_be_number")) { %> A senha deve ser numérica.
                <% } else { %> Erro ao realizar login. <% } %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="input-group">
                <label for="cpf">CPF</label>
                <input type="text" id="cpf" name="cpf" placeholder="Inserir CPF" required>
            </div>

            <div class="input-group">
                <label for="senha">SENHA</label>
                <div class="password-wrapper">
                    <input type="password" id="senha" name="password" placeholder="Senha numérica" required>
                </div>
            </div> <button type="submit" class="btn-login">Login</button>
        </form>
    </main>

    <footer class="login-footer">
        <span class="copyright">© 2026 Atlas. Todos os direitos reservados.</span>
    </footer>
</div>

<script>
    const togglePassword = document.querySelector('#togglePassword');
    const password = document.querySelector('#senha');

    togglePassword.addEventListener('click', function () {
        const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
        password.setAttribute('type', type);
        
        // Alterna entre fa-eye e fa-eye-slash
        this.classList.toggle('fa-eye');
        this.classList.toggle('fa-eye-slash');
    });
</script>

</body>
</html>