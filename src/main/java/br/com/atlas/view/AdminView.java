package br.com.atlas.view;

import java.util.List;

import br.com.atlas.dto.EmployeeDTO;
import br.com.atlas.dto.PersonDTO;
import br.com.atlas.model.Administrator;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Librarian;
import br.com.atlas.model.Person;

public class AdminView extends EmployeeView {

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
        
        op = super.getSc().nextInt();
        super.getSc().nextLine(); // limpa buffer
        return op; 
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

            op = super.getSc().nextInt();
            super.getSc().nextLine(); // limpa buffer
        } while ((op != 0) && (op != 1) && (op != 2) && (op != 3));

        return op;
    }

    @Override
    public PersonDTO doRegister() {
        clearScreen();
        System.out.println("====== REGISTRAR FUNCIONÁRIO ======");
        
        EmployeeDTO e = new EmployeeDTO();

        e.setCpf(readCpf());
        e.setName(readName());
        e.setEmail(readEmail());
        e.setGender(readGender());
        e.setBirthDate(readDate());
        e.setPassword(readPassword());

        return e;
    }

    @Override
    public String passCpf() {
        String cpf;

        System.out.println("====== PESQUISAR FUNCIONÁRIO POR CPF =======");
        System.out.print("Digite o CPF a ser buscado: ");
        cpf = super.getSc().nextLine();

        return cpf;
    }

    // =========================
    // 🔥 MÉTODO QUE FALTAVA
    // =========================
    @Override
    public PersonDTO doEdit(Person p) {
        clearScreen();
        System.out.println("====== EDITAR FUNCIONÁRIO ======");

        EmployeeDTO dto = new EmployeeDTO();

        // mantém CPF original (não pode alterar)
        dto.setCpf(p.getCpf());

        System.out.println("Pressione ENTER para manter o valor atual.\n");

        // NOME
        System.out.print("Nome atual: " + p.getName() + "\nNovo nome: ");
        String name = getSc().nextLine();
        dto.setName(name.isEmpty() ? p.getName() : name);

        // EMAIL
        System.out.print("\nEmail atual: " + p.getEmail() + "\nNovo email: ");
        String email = getSc().nextLine();
        dto.setEmail(email.isEmpty() ? p.getEmail() : email);

        // SEXO
        System.out.print("\nSexo atual: " + p.getGender() + "\nNovo sexo: ");
        String genderInput = getSc().nextLine();
        if (genderInput.isEmpty()) {
            dto.setGender(p.getGender());
        } else {
            dto.setGender(genderInput.charAt(0));
        }

        // DATA
        System.out.print("\nData atual: " + p.getBirthDate() + "\nNova data (dd/mm/aaaa): ");
        String date = getSc().nextLine();
        if (date.isEmpty()) {
            dto.setBirthDate(p.getBirthDate());
        } else {
            dto.setBirthDate(java.time.LocalDate.parse(date, getDateFormatter()));
        }

        // SENHA (parte específica de funcionário)
        System.out.print("\nDeseja alterar a senha? (s/n): ");
        String op = getSc().nextLine();

        if (op.equalsIgnoreCase("s")) {
            dto.setPassword(readPassword());
        } else {
            // mantém senha antiga (cast seguro pois é funcionário)
            dto.setPassword(((br.com.atlas.model.Employee) p).getPassword());
        }

        return dto;
    }

    // confirmação antes de editar
    public boolean confirmEmployee(Person p) {
        clearScreen();

        System.out.println("====== CONFIRMAR FUNCIONÁRIO ======");

        System.out.printf("%-15s %-15s %-25s %-5s %-15s\n",
                "CPF", "NOME", "EMAIL", "SEXO", "NASCIMENTO");

        System.out.printf("%-15s %-15s %-25s %-5c %-15s\n",
                p.getCpf(),
                p.getName(),
                p.getEmail(),
                p.getGender(),
                p.getBirthDate());

        System.out.print("\nÉ este funcionário? (s/n): ");
        String op = getSc().nextLine();

        return op.equalsIgnoreCase("s");
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
            System.out.printf("%-5s %-15s %-15s %-25s %-5s %-15s %-10s\n","IDX", "CPF", "NOME", "EMAIL", "SEXO", "NASCIMENTO", "SENHA");
            for (Attendant a : attendants) {
                i++;
                System.out.printf("%-5d %-15s %-15s %-25s %-5c %-15s %-10d\n", i, a.getCpf(), a.getName(), a.getEmail(), a.getGender(), a.getBirthDate(), a.getPassword());
            }
        }

        // BIBLIOTECÁRIOS
        if (librarians == null || librarians.isEmpty()) {
            System.out.println("\nNão há bibliotecários cadastrados no sistema.");
        } else {
            int i = 0;
            System.out.println("\n====== BIBLIOTECÁRIOS ======");
            System.out.printf("%-5s %-15s %-15s %-25s %-5s %-15s %-10s\n","IDX", "CPF", "NOME", "EMAIL", "SEXO", "NASCIMENTO", "SENHA");
            for (Librarian l : librarians) {
                i++;
                System.out.printf("%-5d %-15s %-15s %-25s %-5c %-15s %-10d\n", i, l.getCpf(), l.getName(), l.getEmail(), l.getGender(), l.getBirthDate(), l.getPassword());
            }
        }

        // ADMIN
        if (admins == null || admins.isEmpty()) {
            System.out.println("\nNão há administradores cadastrados no sistema.");
        } else {
            int i = 0;
            System.out.println("\n====== ADMINISTRADORES ======");
            System.out.printf("%-5s %-15s %-15s %-25s %-5s %-15s %-10s\n","IDX", "CPF", "NOME", "EMAIL", "SEXO", "NASCIMENTO", "SENHA");
            for (Administrator ad : admins) {
                i++;
                System.out.printf("%-5d %-15s %-15s %-25s %-5c %-15s %-10d\n", i, ad.getCpf(), ad.getName(), ad.getEmail(), ad.getGender(), ad.getBirthDate(), ad.getPassword());
            }
        }
    }
}