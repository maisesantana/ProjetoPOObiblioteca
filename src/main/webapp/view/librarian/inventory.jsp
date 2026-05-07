<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="br.com.atlas.model.Librarian" %>

<%
    Object user = session.getAttribute("userLogged");
    
    if(user == null || !(user instanceof Librarian)){
        response.sendRedirect(request.getContextPath() + "/view/login.jsp");
        return;
    }
    
    Librarian librarian = (Librarian) user;
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventário - Atlas</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            max-width: 800px;
            margin: 0 auto;
        }
        h1 {
            color: #333;
        }
        .success-message {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .user-info {
            background-color: #e7f3ff;
            border-left: 4px solid #2196F3;
            padding: 10px;
            margin-bottom: 20px;
        }
        a {
            color: #2196F3;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>📚 Painel de Inventário - Bibliotecário</h1>
        
        <div class="success-message">
            ✅ Login bem-sucedido para Bibliotecário!
        </div>
        
        <div class="user-info">
            <strong>Usuário:</strong> <%= librarian.getName() %><br>
            <strong>CPF:</strong> <%= librarian.getCpf() %><br>
            <strong>Email:</strong> <%= librarian.getEmail() %>
        </div>
        
        <h2>Opções do Bibliotecário:</h2>
        <ul>
            <li><a href="#">Gerenciar Acervo</a></li>
            <li><a href="#">Consultar Livros</a></li>
            <li><a href="#">Relatórios de Empréstimo</a></li>
        </ul>
        
        <p><a href="<%= request.getContextPath() %>/logout">🚪 Sair</a></p>
    </div>
</body>
</html>
