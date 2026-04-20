<%-- Página usada apenas para testar se a conexão com o banco está funcionando --%>
<%-- Após os testes, essa página não será mais necessária no sistema final --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<%-- Importa a classe de conexão que criamos em util/ConexaoBD.java --%>
<%@ page import="br.com.atlas.util.ConnectionDb" %>

<%-- Importa a classe Connection do Java para trabalhar com o banco --%>
<%@ page import="java.sql.Connection" %>

<%
    // Tenta abrir a conexão chamando o método getConexao() da nossa classe
    Connection conn = ConnectionDb.getConexao();

    // Verifica se a conexão foi bem sucedida
    // Se conn for diferente de null, significa que conectou
    boolean conectado = (conn != null);

    // Fecha a conexão logo após o teste
    // Importante sempre fechar para não deixar conexões abertas no banco
    if (conn != null) conn.close();
%>

<!DOCTYPE html>
<html>
<body>

    <%-- Verifica o resultado e exibe a mensagem correspondente na tela --%>
    <% if (conectado) { %>
        <h2 style="color:green">✅ Conectado com sucesso!</h2>

    <% } else { %>
        <h2 style="color:red">❌ Falha na conexão</h2>

        <%-- Exibe a mensagem de erro retornada pela classe ConexaoBD --%>
        <%-- Útil para saber exatamente o que deu errado --%>
        <p><%= ConnectionDb.getUltimoErro() %></p>

    <% } %>

</body>
</html>