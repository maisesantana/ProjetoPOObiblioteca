package br.com.atlas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe respons�vel por gerenciar a conex�o com o banco de dados
// Ela ser� chamada pelos DAOs sempre que precisarem acessar o banco
public class ConnectionDb {

    // Endere�o do banco - "atlas" � o nome do seu banco de dados no MySQL
    private static final String URL     = "jdbc:mysql://localhost:3306/atlas";

    // Usu�rio do MySQL (normalmente "root" em ambiente local)
    private static final String USUARIO = "root";

    // Senha do MySQL
    private static final String SENHA   = "";

    // Guarda a mensagem do �ltimo erro ocorrido
    // �til para exibir o erro na tela durante os testes
    private static String ultimoErro = "";

    // M�todo que abre e retorna a conex�o com o banco
    // Retorna null se algo der errado
    public static Connection getConexao() {
        try {
            // Carrega o driver do MySQL manualmente
            // Necess�rio para o Tomcat reconhecer o driver corretamente
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tenta abrir a conex�o com as configura��es acima
            // Se funcionar, retorna a conex�o pronta para uso
            return DriverManager.getConnection(URL, USUARIO, SENHA);

        } catch (ClassNotFoundException e) {
            // Esse erro acontece se o .jar do MySQL n�o estiver no projeto
            ultimoErro = "Driver n�o encontrado: " + e.getMessage();
            return null;

        } catch (SQLException e) {
            // Esse erro acontece se a URL, usu�rio ou senha estiverem errados
            // Ou se o MySQL n�o estiver rodando
            ultimoErro = e.getMessage();
            return null;
        }
    }

    // Retorna a mensagem do �ltimo erro ocorrido
    // Usado no testeConexao.jsp para exibir o erro na tela
    public static String getUltimoErro() {
        return ultimoErro;
    }
}