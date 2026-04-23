package br.com.atlas.test;

import br.com.atlas.dao.*;
import br.com.atlas.model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class testeConexao {

    public static void main(String[] args) {
        
        System.out.println("🚀 INICIANDO SIMULAÇÃO DO SISTEMA ATLAS\n");

        // --- SIMULAÇÃO 1: ClientController (Cadastro de Cliente) ---
        System.out.println("1. Simulando 'RegisterClient'...");
        
        String cpf = "11122233344";
        Client newClient = new Client(
            cpf, "Miqueias Dev", "miqueias@email.com", "M",
            LocalDate.of(1995, 5, 10), "Rua do Código, 123");

        PersonDAO personDao = new PersonDAO();
        ClientDAO clientDao = new ClientDAO();

        // Tenta salvar a Pessoa e depois o Cliente
        if (personDao.insert(newClient)) {
            clientDao.insert(newClient);
            System.out.println("✅ Cliente cadastrado com sucesso!");
        }

        System.out.println("\n--------------------------------------------\n");

        // --- SIMULAÇÃO 2: BookController (Cadastro de Livro) ---
        System.out.println("2. Simulando 'RegisterBook'...");
        
        // Simula que a Editora ID 1 já existe no banco
        Publisher pub = new Publisher();
        pub.setPublisherId(1); 

        Book newBook = new Book(0, "Java para Iniciantes", "Estante A1", 300, "Programação", "Nacional", pub);
        BookDAO bookDao = new BookDAO();
        bookDao.insert(newBook);
        System.out.println("✅ Livro cadastrado!");
        
        // --- SIMULAÇÃO 2.5: Cadastrando o Exemplar (Físico) ---
        System.out.println("2.5 Simulando criação de Exemplar...");

        Book bookRef = new Book();
        bookRef.setBookId(1); // Referenciando o livro criado acima

        BookCopy newCopy = new BookCopy();
        newCopy.setBook(bookRef);
        newCopy.setStatusAvailable(true); 

        // Criando o DAO do exemplar (Primeira vez)
        BookCopyDAO copyDao = new BookCopyDAO();
        copyDao.insert(newCopy);
        System.out.println("✅ Exemplar ID 1 colocado na estante!");

        System.out.println("\n--------------------------------------------\n");

        // --- SIMULAÇÃO 3: LoanController (Empréstimo com Verificações) ---
        System.out.println("3. Simulando 'RegisterLoan' (Com verificações)...");

        // Busca o cliente real no banco para checar suspensão
        Client clientFromDb = clientDao.findById(cpf);
        
        // REUTILIZANDO a variável copyDao (Sem declarar de novo)
        // Buscamos o exemplar ID 1 que acabamos de criar no passo 2.5
        BookCopy copyFromDb = copyDao.findById(1);

        // A lógica de segurança do seu Controller
        if (clientFromDb == null) {
            System.out.println("❌ Erro: Cliente não encontrado.");
        } else if (!clientFromDb.canBorrow()) {
            System.out.println("❌ Erro: Cliente está SUSPENSO!");
        } else if (copyFromDb == null || !copyFromDb.isStatusAvailable()) {
            System.out.println("❌ Erro: Livro não disponível.");
        } else {
            // Se passar em tudo, registra o empréstimo
            Loan loan = new Loan();
            loan.setClient(clientFromDb);
            loan.setBookCopy(copyFromDb);
            loan.setLoanDate(LocalDateTime.now());
            loan.setExpectedReturnDate(LocalDateTime.now().plusDays(8));
            loan.setRenewals(0);
            loan.setActive(true);

            LoanDAO loanDao = new LoanDAO();
            loanDao.insert(loan);
            
            System.out.println("✅ Empréstimo realizado para: " + clientFromDb.getName());
            System.out.println("📅 Devolução prevista: " + loan.getExpectedReturnDate());
        }
    }
}