<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Atlas</title>
</head>
<body>


    <h1>ATLAS</h1>
    <h5>GESTÃO DE BIBLIOTECA</h5>

    <h2>Login</h2>
    <p>Digite seu CPF e senha</p>

    <form action="/login" method="post">
        
        <label for="cpf">CPF:</label><br>
        <input type="text" id="cpf" name="cpf" placeholder="Digite seu CPF"><br><br>

        <label for="senha">Senha:</label><br>
        <input type="password" id="senha" name="senha" placeholder="Digite sua senha"><br><br>

        <button type="submit">Entrar</button>
    </form>

    <style>
    h1 {
        margin-bottom: 2px; /* diminui espaço abaixo do ATLAS */
    }

    h5 {
        margin-top: 0;      /* tira espaço acima */
        margin-bottom: 20px; /* opcional: controla o resto do layout */
    }
    </style>

    <br>
    <p>Não tem uma conta? <a href="#">Crie uma</a></p>

</body>
</html>