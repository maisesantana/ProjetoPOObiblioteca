package br.com.atlas.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import br.com.atlas.model.Administrator;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Librarian;
import br.com.atlas.model.Person;


public class AdminView extends EmployeeView {

    private Scanner sc;
    private DateTimeFormatter dateFormatter;

    public AdminView() {
        this.sc = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    @Override
    public int showMenu() {
        
        int op;
        
        System.out.println("====== MENU ======");
        System.out.println("1 - Visualizar funcionários");
        System.out.println("2 - Registrar funcionários");
        System.out.println("3 - Editar funcionários");
        System.out.println("4 - Remover funcionários");
        System.out.println("5 - Buscar funcionários");
        System.out.println("6 - Deslogar");
        System.out.println("0 - Fechar o sistema");
        
        return op = sc.nextInt(); 
    }

    public int selectKindOfEmployee() {
        int op = -1;

        clearScreen();
        System.out.println("====== REGISTRAR FUNCIONÁRIO ======");

        do {
            System.out.println("Qual tipo de funcionário deseja cadastrar?");
            System.out.println("1 - Atendente");
            System.out.println("2 - Bibliotecário");
            System.out.println("3 - Admin");
            System.out.println("0 - Voltar");

            op = sc.nextInt();
            sc.nextLine(); //limpa o buffer
        } while ((op != 0) && (op != 1) && (op != 2) && (op != 3));
        return op;
    }

    public Person registerP() {
        int op = selectKindOfEmployee();
        if (op == 0) {
            System.out.println("Voltando para menu principal.");
            return null;
        }
        clearScreen();
        return viewRegister(op);
    }

    public Person viewRegister(int op) {
        String cpf, name, email, birthDate; 
        char gender;
        int password, validatePassword;
        LocalDate bDate;

        System.out.println("====== REGISTRAR FUNCIONÁRIO ======");

        System.out.print("CPF: ");
        cpf = sc.next();
        sc.nextLine(); //limpa buffer
        
        System.out.print("\nNome: ");
        name = sc.nextLine();
        sc.nextLine(); //limpa buffer
        
        System.out.print("\nEmail: ");
        email = sc.nextLine();
        sc.nextLine(); //limpa buffer
        
        System.out.print("\nSexo (caractere único): ");
        gender = sc.next().charAt(0);
        sc.nextLine(); //limpa buffer
        
        System.out.print("\nData de nascimento: (dd/mm/aaaa): ");
        birthDate = sc.nextLine();
        sc.nextLine(); //limpa buffer
        //conversao de string para tipo data no formato EUA:
        bDate = LocalDate.parse(birthDate, dateFormatter);

        do {
            System.out.print("\nSenha numérica: ");
            password = sc.nextInt();
            sc.nextLine(); //limpa buffer

            System.out.print("\nConfirme a senha numérica: ");
            validatePassword = sc.nextInt();
            sc.nextLine();

            if (password != validatePassword) {
                System.out.println("A senha numérica precisa ser igual ao confirmar! Digite novamente.");
            }
        } while (password != validatePassword);

        if (op == 1) {
            return new Attendant(cpf, name, email, gender, bDate, password);
        } else if (op == 2) {
            return new Librarian (cpf, name, email, gender, bDate, password);
        } else {
            return new Administrator(cpf, name, email, gender, bDate, password);
        }
    }

    public void showAllEmployees(List<Attendant> attendants, List<Librarian> librarians, List<Administrator> admins) {
        clearScreen();
        System.out.println("====== FUNCIONÁRIOS =======");
        // ATENDENTES
        if (attendants == null || attendants.isEmpty()) {
            System.out.println("Não há atendentes cadastrados no sistema.");
        } else {
            int i = 0;
            System.out.println("====== ATENDENTES ======");
            System.out.println("ÍNDICE \tCPF \tNOME \tEMAIL \tSEXO \tDATA DE NASC. \tSENHA");
            for (Attendant a : attendants) {
                i++;
                System.out.println(i + "\t" + a.getCpf() + "\t" + a.getName() + "\t" + a.getEmail() + "\t" + a.getGender() + "\t" + a.getBirthDate() + "\t" + a.getPassword());
            }
        }

        // BIBLIOTECÁRIOS
        if (librarians == null || librarians.isEmpty()) {
            System.out.println("Não há bibliotecários cadastrados no sistema.");
        } else {
            int i = 0;
                System.out.println("====== BIBLIOTECÁRIOS ======");
            System.out.println("ÍNDICE \tCPF \tNOME \tEMAIL \tSEXO \tDATA DE NASC. \tSENHA");
            for (Librarian l : librarians) {
                i++;
                System.out.println(i + "\t" + l.getCpf() + "\t" + l.getName() + "\t" + l.getEmail() + "\t" + l.getGender() + "\t" + l.getBirthDate() + "\t" + l.getPassword());
            }
        }

        // ADMIN
        if (admins == null || admins.isEmpty()) {
            System.out.println("Não há administradores cadastrados no sistema.");
        } else {
            int i = 0;
            System.out.println("====== ADMINISTRADORES ======");
            System.out.println("ÍNDICE \tCPF \tNOME \tEMAIL \tSEXO \tDATA DE NASC. \tSENHA");
            for (Administrator ad : admins) {
                i++;
                System.out.println(i + "\t" + ad.getCpf() + "\t" + ad.getName() + "\t" + ad.getEmail() + "\t" + ad.getGender() + "\t" + ad.getBirthDate() + "\t" + ad.getPassword());
            }
        }
    }
}
