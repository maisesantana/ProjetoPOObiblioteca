package br.com.atlas.controller;

import br.com.atlas.dto.ClientDTO;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Client;
import br.com.atlas.model.Book;
import br.com.atlas.view.AttendantView;
import java.util.List;

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
            op = view.showMenu();
            switch (op) {
                case 1 -> manageClients();
                case 2 -> registerLoan();
                case 3 -> registerReturn();
                case 4 -> registerRenewal();
                case 5 -> searchBooks();
                case 0 -> System.out.println("Deslogando Atendente...");
                default -> System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private void manageClients() {
        int choice;
        do {
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
    }

    private void registerLoan() {
        try {
            attendant.registerLoan(view.askCpf(), view.askCopyId());
            System.out.println("✅ Empréstimo realizado!");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void registerReturn() {
        try {
            attendant.registerReturn(view.askLoanId());
            System.out.println("✅ Devolução concluída!");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void registerRenewal() {
        try {
            attendant.registerRenewal(view.askLoanId());
            System.out.println("✅ Renovação concluída!");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void searchBooks() {
        List<Book> books = attendant.searchBooks(view.askBookName());
        view.showBooks(books);
    }
}