package br.com.atlas.view;

import java.io.IOException; // pacote para detectar se é linux ou windows
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

        int op;

        System.out.println("====== MENU ======");
        System.out.println("1 - Visualizar pessoas");
        System.out.println("2 - Cadastrar pessoas");
        System.out.println("3 - Editar pessoas");
        System.out.println("4 - Remover pessoas");
        System.out.println("5 - Buscar pessoas");
        System.out.println("6 - Deslogar");
        System.out.println("0 - Fechar o sistema");

        return op = sc.nextInt();
    }

    public static void clearScreen() {
        try {
            String sistema = System.getProperty("os.name");

            if (sistema.toLowerCase().contains("windows")) { 
                //lowercase deixa em minusculo, contains verifica se a string esta la
            
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                // process builder executa comandos externos,  inherit usa o mesmo terminal do programa,
                // start inicia e wait for espera o processo terminar
            } else {
                Runtime.getRuntime().exec("clear");
                //executa no macIOS ou Linux
            }
        } catch (Exception e) {
            throw new RuntimeException ("Erro ao limpar tela" + e);
        }
    }

    public String passCpf() {
        String cpf;
        System.out.println("====== PESQUISAR PESSOA POR CPF =======");
        System.out.print("Digite o CPF a ser buscado: ");
        cpf = sc.nextLine();
        return cpf;
    }

    public String readCpf() {
        String cpf;
        System.out.print("CPF: ");
        cpf = sc.nextLine();
        return cpf;
    }
    
    public String readName() {
        String name;
        System.out.print("Nome: ");
        name = sc.nextLine();
        return name;
    }

    public String readEmail() {
        String email;
        System.out.print("Email: ");
        email = sc.nextLine();
        return email;
    }

    public char readGender() {
        char gender;
        System.out.print("Sexo: ");
        gender = sc.next().charAt(0);
        sc.nextLine();
        return gender;
    }

    public LocalDate readDate() {
        String birthDate;
        System.out.print("Data de nascimento: (dd/mm/aaaa): ");
        birthDate = sc.nextLine();
        //conversao de string para tipo data no formato EUA:
        LocalDate bDate = LocalDate.parse(birthDate, dateFormatter);
        return bDate;
    }

    public int readPassword() {
        int password, validate;

        do {
            System.out.print("\nSenha numérica: ");
            while (!sc.hasNextInt()) { // valida se é número
                System.out.println("Digite apenas números!");
                sc.next(); // descarta entrada inválida
            }
            password = sc.nextInt();
            sc.nextLine(); // limpar buffer

            System.out.print("\nConfirme a senha numérica: ");
            while (!sc.hasNextInt()) { 
                System.out.println("Digite apenas números!");
                sc.next();
            }
            validate = sc.nextInt();
            sc.nextLine(); // limpar buffer

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

        // tabela de 1 único funcionário
        System.out.printf("%-15s %-15s %-25s %-5s %-15s\n", "CPF", "NOME", "EMAIL", "SEXO", "NASCIMENTO");

        System.out.printf("%-15s %-15s %-25s %-5c %-15s\n", p.getCpf(), p.getName(), p.getEmail(), p.getGender(), p.getBirthDate());

        System.out.print("\nÉ este funcionário? (s/n): ");
        String op = getSc().nextLine();

        return op.equalsIgnoreCase("s");
    }

    public PersonDTO doEdit(Person p) {
        clearScreen();
        System.out.println("====== EDITAR PESSOA ======");

        PersonDTO dto = new PersonDTO();

        // CPF não muda
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

        if (d.isEmpty()) {
            dto.setBirthDate(p.getBirthDate());
        } else {
            dto.setBirthDate(LocalDate.parse(d, dateFormatter));
        }
        return dto;
    }
}
