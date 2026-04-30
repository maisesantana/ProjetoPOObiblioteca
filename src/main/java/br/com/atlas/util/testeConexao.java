package br.com.atlas.util;

import br.com.atlas.dao.*;
import br.com.atlas.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Optional;

public class testeConexao {

    public static void main(String[] args) {

        System.out.println("🚀 INICIANDO TESTE DO SISTEMA ATLAS (ORDEM MANUAL)");

        try (Connection conn = ConnectionDb.getConexao()) {

            if (conn == null) {
                System.err.println("❌ ERRO: Conexão nula!");
                return;
            }

            System.out.println("✅ Conexão estabelecida!");

            PersonDAO personDAO = new PersonDAO(conn);
            LibrarianDAO librarianDAO = new LibrarianDAO(conn);
            LoginDAO loginDAO = new LoginDAO(conn);

            String cpfTeste = "98765432100";

            // --- PASSO 1: LIMPEZA MANUAL EM ORDEM REVERSA ---
            System.out.println("\n--- PASSO 1: LIMPEZA DE DADOS ---");

            // 1. Apagamos o nível mais baixo (Librarian/Administrator/Attendant)
            // Se o seu DDL tem cascade de Librarian para Employee, este comando limpa ambos:
            String sqlDelEmp = "DELETE FROM Employee WHERE cpf = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDelEmp)) {
                stmt.setString(1, cpfTeste);
                stmt.executeUpdate();
            }

            // 2. Agora que o Employee sumiu, podemos apagar a Person com segurança
            personDAO.delete(cpfTeste);
            System.out.println("✅ Banco limpo manualmente (Filhos -> Pai).");

            // --- PASSO 2: CADASTRO ---
            System.out.println("\n--- PASSO 2: TESTE DE CADASTRO ---");

            Person p = new Person(cpfTeste, "Ana Bibliotecaria", "ana@atlas.com", "F", LocalDate.of(1990, 5, 15));
            personDAO.insert(p);
            System.out.println("✅ 1/3: Person inserida.");

            String sqlInsEmployee = "INSERT INTO Employee (cpf, password) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsEmployee)) {
                stmt.setString(1, cpfTeste);
                stmt.setInt(2, 1234);
                stmt.executeUpdate();
            }
            System.out.println("✅ 2/3: Employee inserido.");

            Librarian lib = new Librarian(cpfTeste, "Ana Bibliotecaria", "ana@atlas.com", "F", LocalDate.of(1990, 5, 15), 1234);
            librarianDAO.insert(lib);
            System.out.println("✅ 3/3: Librarian inserido.");

            // --- PASSO 3: AUTENTICAÇÃO ---
            System.out.println("\n--- PASSO 3: TESTE DE LOGIN ---");
            Optional<Employee> login = loginDAO.authenticate(cpfTeste, 1234);

            if (login.isPresent()) {
                System.out.println("🎊 SUCESSO: Bem-vindo(a), " + login.get().getName());
            } else {
                System.out.println("❌ FALHA NO LOGIN.");
            }

        } catch (Exception e) {
            System.err.println("\n🚨 ERRO NO FLUXO:");
            e.printStackTrace();
        }
    }
}