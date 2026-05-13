package br.com.atlas.view;

// import java.io.IOException removido — não estava sendo usado
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import br.com.atlas.dto.PersonDTO;
import br.com.atlas.model.Person;

public class EmployeeView {
    
    private Scanner sc;
    private DateTimeFormatter dateFormatter;

    public EmployeeView() {
        this.sc = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public Scanner getSc() {
        return sc;
    }

    public DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }

    public int showMenu() {
        System.out.println("====== MENU ======");
        System.out.println("1 - Visualizar pessoas");
        System.out.println("2 - Cadastrar pessoas");
        System.out.println("3 - Editar pessoas");
        System.out.println("4 - Remover pessoas");
        System.out.println("5 - Buscar pessoas");
        System.out.println("6 - Deslogar");
        System.out.println("0 - Fechar o sistema");

        return sc.nextInt(); // variável op desnecessária — retorna direto
    }

    public static void clearScreen() {
        try {
            String sistema = System.getProperty("os.name");

            if (sistema.toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // ProcessBuilder no lugar de Runtime.exec() — não é deprecated
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao limpar tela" + e);
        }
    }

    public String passCpf() {
        System.out.println("====== PESQUISAR PESSOA POR CPF =======");
        System.out.print("Digite o CPF a ser buscado: ");
        return sc.nextLine();
    }

    public String readCpf() {
        System.out.print("CPF: ");
        return sc.nextLine();
    }

    public String readName() {
        System.out.print("Nome: ");
        return sc.nextLine();
    }

    public String readEmail() {
        System.out.print("Email: ");
        return sc.nextLine();
    }

    public char readGender() {
        System.out.print("Sexo: ");
        char gender = sc.next().charAt(0);
        sc.nextLine();
        return gender;
    }

    public LocalDate readDate() {
        System.out.print("Data de nascimento: (dd/mm/aaaa): ");
        String birthDate = sc.nextLine();
        return LocalDate.parse(birthDate, dateFormatter);
    }

    public int readPassword() {
        int password, validate;

        do {
            System.out.print("\nSenha numérica: ");
            while (!sc.hasNextInt()) {
                System.out.println("Digite apenas números!");
                sc.next();
            }
            password = sc.nextInt();
            sc.nextLine();

            System.out.print("\nConfirme a senha numérica: ");
            while (!sc.hasNextInt()) {
                System.out.println("Digite apenas números!");
                sc.next();
            }
            validate = sc.nextInt();
            sc.nextLine();

            if (password != validate) {
                System.out.println("A senha numérica precisa ser igual ao confirmar! Digite novamente.");
            }
        } while (password != validate);

        return password;
    }

    public PersonDTO doRegister() {
        clearScreen();
        System.out.println("====== REGISTRAR PESSOA ======");

        PersonDTO p = new PersonDTO();
        p.setCpf(readCpf());
        p.setName(readName());
        p.setEmail(readEmail());
        p.setGender(readGender());
        p.setBirthDate(readDate());

        return p;
    }

    public boolean confirmEmployee(Person p) {
        clearScreen();
        System.out.println("====== CONFIRMAR FUNCIONÁRIO ======");
        System.out.printf("%-15s %-15s %-25s %-5s %-15s\n", "CPF", "NOME", "EMAIL", "SEXO", "NASCIMENTO");
        System.out.printf("%-15s %-15s %-25s %-5c %-15s\n", p.getCpf(), p.getName(), p.getEmail(), p.getGender(), p.getBirthDate());
        System.out.print("\nÉ este funcionário? (s/n): ");
        return getSc().nextLine().equalsIgnoreCase("s");
    }

    public PersonDTO doEdit(Person p) {
        clearScreen();
        System.out.println("====== EDITAR PESSOA ======");

        PersonDTO dto = new PersonDTO();
        dto.setCpf(p.getCpf());

        System.out.println("Deixe vazio para não alterar.");

        System.out.print("Nome atual: " + p.getName() + " | Novo: ");
        String name = sc.nextLine();
        dto.setName(name.isEmpty() ? p.getName() : name);

        System.out.print("Email atual: " + p.getEmail() + " | Novo: ");
        String email = sc.nextLine();
        dto.setEmail(email.isEmpty() ? p.getEmail() : email);

        System.out.print("Sexo atual: " + p.getGender() + " | Novo: ");
        String g = sc.nextLine();
        dto.setGender(g.isEmpty() ? p.getGender() : g.charAt(0));

        System.out.print("Data atual: " + p.getBirthDate() + " | Nova (dd/mm/aaaa): ");
        String d = sc.nextLine();
        dto.setBirthDate(d.isEmpty() ? p.getBirthDate() : LocalDate.parse(d, dateFormatter));

        return dto;
    }
}