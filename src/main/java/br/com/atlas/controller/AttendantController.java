package br.com.atlas.controller;

import br.com.atlas.dto.ClientDTO;
import br.com.atlas.model.Client;
import br.com.atlas.model.Book;
import br.com.atlas.service.ClientService;
import br.com.atlas.service.LoanService;
import br.com.atlas.service.RenewalService;
import br.com.atlas.service.ReturnBookService;
import br.com.atlas.service.BookService;
import br.com.atlas.view.AttendantView;

import java.util.List;
import java.util.Scanner;

public class AttendantController {

    private final ClientService clientService;
    private final LoanService loanService;
    private final RenewalService renewalService;
    private final ReturnBookService returnBookService;
    private final BookService bookService;
    private final AttendantView view;

    public AttendantController(ClientService clientService, LoanService loanService,
                               RenewalService renewalService, ReturnBookService returnBookService,
                               BookService bookService, AttendantView view) {
        this.clientService = clientService;
        this.loanService = loanService;
        this.renewalService = renewalService;
        this.returnBookService = returnBookService;
        this.bookService = bookService;
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
                default -> { System.out.println("Opção inválida!"); pressEnterToContinue(); }
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
            view.showClients(clientService.findAll());
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
            clientService.insert(c);
            System.out.println("✅ Cliente cadastrado!");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void editClient() {
        try {
            String cpf = view.askCpf();
            Client c = clientService.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

            System.out.println("Editando: " + c.getName());
            ClientDTO dto = view.readClientData();

            c.setName(dto.getName());
            c.setEmail(dto.getEmail());
            c.setGender(dto.getGender());
            c.setBirthDate(dto.getBirthDate());
            c.setAddress(dto.getAddress());

            clientService.update(c);
            System.out.println("✅ Cliente atualizado!");
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void registerLoan() {
        try {
            loanService.registerLoan(view.askCpf(), view.askCopyId());
            System.out.println("✅ Empréstimo realizado!");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void registerRenewal() {
        if (!loanService.hasActiveLoans()) {
            System.out.println("\n⚠️ Não existem empréstimos ativos no sistema para renovação.");
            pressEnterToContinue();
            return;
        }
        try {
            renewalService.registerRenewal(view.askLoanId());
            System.out.println("✅ Renovação concluída!");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void registerReturn() {
        if (!loanService.hasActiveLoans()) {
            System.out.println("\n⚠️ Não existem empréstimos ativos para devolução.");
            pressEnterToContinue();
            return;
        }
        try {
            returnBookService.registerReturn(view.askLoanId());
            System.out.println("✅ Devolução concluída!");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void searchBooks() {
        List<Book> books = bookService.findByName(view.askBookName());
        view.showBooks(books);
        pressEnterToContinue();
    }

    private void listActiveLoans() {
        br.com.atlas.view.EmployeeView.clearScreen();
        System.out.println("=== LISTAGEM DE EMPRÉSTIMOS ATIVOS ===");

        List<String> loans = loanService.listActiveLoansInfo();

        if (loans.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo no momento.");
        } else {
            loans.forEach(System.out::println);
        }
        pressEnterToContinue();
    }

    private void pressEnterToContinue() {
        System.out.println("\nPressione Enter para continuar...");
        new Scanner(System.in).nextLine();
    }
}