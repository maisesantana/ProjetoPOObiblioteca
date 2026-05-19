package br.com.atlas.view;

import br.com.atlas.dto.ClientDTO;
import br.com.atlas.model.Book;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AttendantView extends EmployeeView {

    private Scanner scanner = new Scanner(System.in);

    @Override public int showMenu() {
        System.out.println("\n=== PAINEL DO ATENDENTE ===");
        System.out.println("1. Gerenciar Clientes (Listar/Cadastrar/Editar)");
        System.out.println("2. Registrar Empréstimo");
        System.out.println("3. Registrar Devolução");
        System.out.println("4. Registrar Renovação");
        System.out.println("5. Pesquisar Livros");
        System.out.println("6. Listar Empréstimos Ativos");
        System.out.println("7. Listar Todos os Livros");
        System.out.println("0. Sair");
        System.out.print("Opção: ");
        return scanner.nextInt();
    }

    public int showClientMenu() {
        System.out.println("\n--- GERENCIAR CLIENTES ---");
        System.out.println("1. Listar Clientes");
        System.out.println("2. Cadastrar Novo Cliente");
        System.out.println("3. Editar Cliente");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
        int op = scanner.nextInt();
        scanner.nextLine();
        return op;
    }

    public void showClients(List<br.com.atlas.model.Client> clients) {
        br.com.atlas.view.EmployeeView.clearScreen();
        if (clients.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            System.out.println("\n=== LISTA DE CLIENTES ===");
            clients.forEach(c -> System.out.println("CPF: " + c.getCpf() + " | Nome: " + c.getName()));
        }
    }

    public ClientDTO readClientData() {
        br.com.atlas.view.EmployeeView.clearScreen();
        ClientDTO dto = new ClientDTO();
        System.out.print("CPF: "); dto.setCpf(scanner.nextLine());
        System.out.print("Nome: "); dto.setName(scanner.nextLine());
        System.out.print("Email: "); dto.setEmail(scanner.nextLine());
        System.out.print("Gênero (M/F): "); dto.setGender(scanner.next().charAt(0));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            try {
                System.out.print("Data Nasc (DD/MM/AAAA): ");
                String dataTexto = scanner.next();
                dto.setBirthDate(LocalDate.parse(dataTexto, formatter));
                break;
            } catch (DateTimeParseException e) {
                System.out.println("⚠️ Data inválida! Use o formato dia/mês/ano (ex: 30/09/2006).");
            }
        }
        scanner.nextLine();
        System.out.print("Endereço: "); dto.setAddress(scanner.nextLine());
        return dto;
    }

    public String askCpf() {
        System.out.print("Informe o CPF do Cliente: ");
        return scanner.next();
    }

    public int askCopyId() {
        System.out.print("ID do Exemplar: ");
        return scanner.nextInt();
    }

    public int askLoanId() {
        System.out.print("ID do Empréstimo: ");
        return scanner.nextInt();
    }

    public String askBookName() {
        scanner.nextLine();
        System.out.print("Nome do Livro para busca: ");
        return scanner.nextLine();
    }

    public void showBooks(List<Book> books) {
        System.out.println("\n====== LIVROS ======");
        if (books == null || books.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }
        System.out.printf("%-5s %-30s %-15s\n", "ID", "NOME", "LOCALIZAÇÃO");
        System.out.println("-".repeat(55));
        books.forEach(b -> System.out.printf("%-5d %-30s %-15s\n",
            b.getBookId(),
            b.getBookName(),
            b.getBookLocation() != null ? b.getBookLocation() : "-"));
    }
}