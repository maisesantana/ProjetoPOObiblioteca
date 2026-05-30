package br.com.atlas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDb {

    private static String ultimoErro = "";

    public static Connection getConexao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Lê variáveis de ambiente do Docker; fallback para desenvolvimento local
            String host = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
            String port = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
            String name = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "atlas";
            String user = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
            String pass = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "";

            String url = "jdbc:mysql://" + host + ":" + port + "/" + name
                       + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo";

            return DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException e) {
            ultimoErro = "Driver não encontrado: " + e.getMessage();
            return null;
        } catch (SQLException e) {
            ultimoErro = e.getMessage();
            return null;
        }
    }

    public static String getUltimoErro() {
        return ultimoErro;
    }
}