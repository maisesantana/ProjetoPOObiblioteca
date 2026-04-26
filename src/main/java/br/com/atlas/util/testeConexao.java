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

            System.out.println("\n-----------------------------------------");
            System.out.println("🏆 TESTE FINALIZADO COM SUCESSO!");
            System.out.println("-----------------------------------------");

        } catch (Exception e) {
            System.out.println("\n❌ ERRO DURANTE O TESTE:");
            e.printStackTrace();
        }
    }
}