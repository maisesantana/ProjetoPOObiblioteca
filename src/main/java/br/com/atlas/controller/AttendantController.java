package br.com.atlas.controller;

import br.com.atlas.dto.ClientDTO;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Client;
import br.com.atlas.model.Book;
import br.com.atlas.view.AttendantView;
import java.util.List;
import java.util.Scanner;

public class AttendantController {
    private Attendant attendant;
    private AttendantView view;

    public AttendantController(Attendant attendant, AttendantView view) {
        this.attendant = attendant;
        this.view = view;
    }

    public void start() {
        int op;
        do {
            br.com.atlas.view.EmployeeView.clearScreen();
            op = view.showMenu();
            switch (op) {
                case 1 -> manageClients();
                case 2 -> registerLoan();
                case 3 -> registerReturn();
                case 4 -> registerRenewal();
                case 5 -> searchBooks();
                case 6 -> listActiveLoans();
                case 0 -> System.out.println("Deslogando Atendente...");
                default -> 
                    {System.out.println("Opção inválida!");
                    pressEnterToContinue();
                }
            }
        } while (op != 0);
    }

    private void manageClients() {
        int choice;
        do {
            br.com.atlas.view.EmployeeView.clearScreen();
            choice = view.showClientMenu();
            switch (choice) {
                case 1 -> listClients();
                case 2 -> registerNewClient();
                case 3 -> editClient();
                case 0 -> System.out.println("Voltando...");
            }
        } while (choice != 0);
    }

    private void listClients() {
        try {
            // O Controller pede a lista para o Model
            view.showClients(attendant.listAllClients());
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void registerNewClient() {
        try {
            ClientDTO dto = view.readClientData();
            Client c = new Client(dto.getCpf(), dto.getName(), dto.getEmail(), 
                                 dto.getGender(), dto.getBirthDate(), dto.getAddress());
            attendant.register(c);
            System.out.println("✅ Cliente cadastrado!");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void editClient() {
        try {
            String cpf = view.askCpf();
            // findPersonByCpf já existe no seu Model Attendant
            Client c = (Client) attendant.findPersonByCpf(cpf);
            if (c == null) throw new RuntimeException("Cliente não encontrado.");

            System.out.println("Editando: " + c.getName());
            ClientDTO dto = view.readClientData();
            
            c.setName(dto.getName());
            c.setEmail(dto.getEmail());
            c.setGender(dto.getGender());
            c.setBirthDate(dto.getBirthDate());
            c.setAddress(dto.getAddress());
            
            attendant.update(c);
            System.out.println("✅ Cliente atualizado!");
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void registerLoan() {
        try {
            attendant.registerLoan(view.askCpf(), view.askCopyId());
            System.out.println("✅ Empréstimo realizado!");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
        pressEnterToContinue();
    }

    // Localize o método registerRenewal no seu AttendantController.java
    private void registerRenewal() {
        // BLINDAGEM: Só pede o ID se houver algum empréstimo ativo no banco
        if (!new br.com.atlas.dao.LoanDAO().hasActiveLoans()) {
            System.out.println("\n⚠️ [AVISO] Não existem empréstimos ativos no sistema para renovação.");
            pressEnterToContinue();
            return; 
        }

        try {
            int loanId = view.askLoanId();
            attendant.registerRenewal(loanId);
            System.out.println("✅ Renovação concluída!");
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    // Aproveite e faça o mesmo para o registerReturn!
    private void registerReturn() {
        if (!new br.com.atlas.dao.LoanDAO().hasActiveLoans()) {
            System.out.println("\n⚠️ [AVISO] Não existem empréstimos ativos para devolução.");
            pressEnterToContinue();
            return;
        }

        try {
            attendant.registerReturn(view.askLoanId());
            System.out.println("✅ Devolução concluída!");
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void searchBooks() {
        List<Book> books = attendant.searchBooks(view.askBookName());
        view.showBooks(books);
        pressEnterToContinue();
    }
    
    // O método que executa a listagem
    private void listActiveLoans() {
        br.com.atlas.view.EmployeeView.clearScreen();
        System.out.println("=== LISTAGEM DE EMPRÉSTIMOS ATIVOS ===");
        
        // O Controller pede os dados para o DAO/Model
        List<String> loans = new br.com.atlas.dao.LoanDAO().listActiveLoansInfo();
        
        if (loans.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo no momento.");
        } else {
            loans.forEach(System.out::println);
        }
        pressEnterToContinue();
    }

    // MÉTODO AUXILIAR PARA PAUSA
    private void pressEnterToContinue() {
        System.out.println("\nPressione Enter para continuar...");
        new Scanner(System.in).nextLine();
    }
}