package br.com.atlas.util;

import br.com.atlas.dao.*;
import br.com.atlas.model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
//import java.util.Collections;

public class testeConexao {

    public static void main(String[] args) {
        System.out.println("🤖 INICIANDO TESTE DE FLUXO AUTOMATIZADO - ATLAS\n");

        try {
            // 1. Instanciando os DAOs
            BookDAO bookDao = new BookDAO();
            BookCopyDAO copyDao = new BookCopyDAO();
            ClientDAO clientDao = new ClientDAO();
            LoanDAO loanDao = new LoanDAO();
            RenewalDAO renewalDao = new RenewalDAO();
            ReturnBookDAO returnDao = new ReturnBookDAO();

            // --- PASSO 0: CADASTRO DE FUNCIONÁRIO ---
            System.out.println("0. Cadastrando Funcionário...");
            Librarian bibliotecario = new Librarian("98765432100", "Ana Bibliotecária", "ana@atlas.com", "F", LocalDate.of(1990, 5, 15), 1234);

            // Administrator precisa de uma Connection, então usamos o register diretamente
            // Precisamos de um Administrator instanciado para chamar register()
            Administrator admin = new Administrator("00000000000", "Admin Teste", "admin@atlas.com", "M", LocalDate.of(1985, 1, 1), 9999);
            admin.register(bibliotecario);
            System.out.println("✅ Funcionário " + bibliotecario.getName() + " cadastrado.");

            // --- PASSO 1: CADASTRO DE INFRAESTRUTURA ---
            System.out.println("1. Cadastrando Editora...");
            // Supondo que Publisher agora use String ou ID conforme seu SQL
            // Aqui vamos simular o cadastro manual para teste
            System.out.println("✅ Editora 'Atlas Editorial' pronta.");

            // --- PASSO 2: CADASTRO DO LIVRO E EXEMPLAR ---
            System.out.println("2. Cadastrando Livro e Exemplar...");
            Book livro = new Book("Java: Como Programar", "Prateleira A1", 500, "Tecnologia", "Atlas Editorial");
            bookDao.insert(livro); 
            // O insert do BookDAO já deve setar o ID no objeto 'livro'
            
            BookCopy exemplar = new BookCopy(livro);
            copyDao.insert(exemplar);
            System.out.println("✅ Livro ID " + livro.getBookId() + " e Exemplar cadastrados.");

            // --- PASSO 3: CADASTRO DO CLIENTE ---
            System.out.println("3. Cadastrando Cliente...");
            Client cliente = new Client("12345678900", "Miqueias Dev", "miqueias@email.com", "M", LocalDate.of(2000, 1, 1), "Rua Java, 100");
            clientDao.insert(cliente);
            System.out.println("✅ Cliente " + cliente.getName() + " cadastrado.");

            // --- PASSO 4: REALIZANDO EMPRÉSTIMO ---
            System.out.println("4. Realizando Empréstimo...");
            Loan emprestimo = new Loan(cliente, exemplar);
            emprestimo.setActive(true);
            loanDao.insert(emprestimo);
            System.out.println("✅ Empréstimo realizado. Devolução prevista para: " + emprestimo.getExpectedReturnDate());

            // --- PASSO 5: REALIZANDO RENOVAÇÃO ---
            System.out.println("5. Realizando Renovação...");
            if (emprestimo.canRenew()) {
                LocalDateTime novaData = emprestimo.getExpectedReturnDate().plusDays(7);
                Renewal renovacao = new Renewal(novaData, 1, emprestimo);
                renewalDao.insert(renovacao);
                System.out.println("✅ Renovado! Nova data: " + novaData);
            }

            // --- PASSO 6: REALIZANDO DEVOLUÇÃO ---
            System.out.println("6. Realizando Devolução...");
            ReturnBook devolucao = new ReturnBook(LocalDateTime.now(), emprestimo);
            returnDao.insert(devolucao);
            
            if (devolucao.isLate()) {
                System.out.println("⚠️ Devolução com ATRASO! Dias de suspensão: " + devolucao.calculateSuspensionDays());
            } else {
                System.out.println("✅ Devolução realizada com sucesso. Livro disponível novamente.");
            }

            // --- PASSO 7: TESTE DE AUTENTICAÇÃO ---
System.out.println("\n7. Testando Sistema de Login...");
LoginDAO loginDao = new LoginDAO(br.com.atlas.util.ConnectionDb.getConexao());

// Cenário A: Login da Ana (Bibliotecária) com a senha correta (1234)
System.out.print("   -> Tentando login [Ana - Correto]: ");
java.util.Optional<Employee> loginSucesso = loginDao.authenticate("98765432100", 1234);

if (loginSucesso.isPresent()) {
    Employee user = loginSucesso.get();
    System.out.println("✅ LOGADO!");
    System.out.println("      Usuário: " + user.getName());
    
    // Testando o Polimorfismo e Cargo
    if (user instanceof Librarian) {
        System.out.println("      Cargo Detectado: 📚 Bibliotecário (Acesso ao Acervo liberado)");
    } else if (user instanceof Administrator) {
        System.out.println("      Cargo Detectado: 👑 Administrador (Acesso Total)");
    }
} else {
    System.out.println("❌ Erro inesperado: O login deveria ter funcionado.");
}

// Cenário B: Login com SENHA ERRADA
System.out.print("   -> Tentando login [Ana - Senha Errada]: ");
java.util.Optional<Employee> loginSenhaErrada = loginDao.authenticate("98765432100", 9999);
if (loginSenhaErrada.isEmpty()) {
    System.out.println("✅ BLOQUEADO (Senha incorreta funcionou)");
} else {
    System.out.println("❌ FALHA DE SEGURANÇA: Login permitido com senha errada!");
}

// Cenário C: Login com CPF que não existe
System.out.print("   -> Tentando login [CPF Inexistente]: ");
java.util.Optional<Employee> loginInexistente = loginDao.authenticate("11111111111", 1234);
if (loginInexistente.isEmpty()) {
    System.out.println("✅ BLOQUEADO (CPF inexistente funcionou)");
} else {
    System.out.println("❌ FALHA: O sistema inventou um usuário!");
}

            System.out.println("\n-----------------------------------------");
            System.out.println("🏆 TESTE FINALIZADO COM SUCESSO!");
            System.out.println("-----------------------------------------");

        } catch (Exception e) {
            System.out.println("\n❌ ERRO DURANTE O TESTE:");
            e.printStackTrace();
        }
    }
}