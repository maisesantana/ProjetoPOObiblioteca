package br.com.atlas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe responsável por gerenciar a conexão com o banco de dados
// Ela será chamada pelos DAOs sempre que precisarem acessar o banco
public class ConnectionDb {

    // Endereço do banco - "atlas" é o nome do seu banco de dados no MySQL
    private static final String URL     = "jdbc:mysql://localhost:3306/atlas";

    // Usuário do MySQL (normalmente "root" em ambiente local)
    private static final String USUARIO = "root";

    // Senha do MySQL
    private static final String SENHA   = "";

    // Guarda a mensagem do último erro ocorrido
    // Útil para exibir o erro na tela durante os testes
    private static String ultimoErro = "";

    // Método que abre e retorna a conexão com o banco
    // Retorna null se algo der errado
    public static Connection getConexao() {
        try {
            // Carrega o driver do MySQL manualmente
            // Necessário para o Tomcat reconhecer o driver corretamente
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tenta abrir a conexão com as configurações acima
            // Se funcionar, retorna a conexão pronta para uso
            return DriverManager.getConnection(URL, USUARIO, SENHA);

        } catch (ClassNotFoundException e) {
            // Esse erro acontece se o .jar do MySQL não estiver no projeto
            ultimoErro = "Driver não encontrado: " + e.getMessage();
            return null;

        } catch (SQLException e) {
            // Esse erro acontece se a URL, usuário ou senha estiverem errados
            // Ou se o MySQL não estiver rodando
            ultimoErro = e.getMessage();
            return null;
        }
    }

    // Retorna a mensagem do último erro ocorrido
    // Usado no testeConexao.jsp para exibir o erro na tela
    public static String getUltimoErro() {
        return ultimoErro;
    }
}