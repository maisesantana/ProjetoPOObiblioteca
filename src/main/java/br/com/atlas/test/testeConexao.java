package br.com.atlas.test;

import br.com.atlas.dao.ClientDAO;
import br.com.atlas.dao.PersonDAO;
import br.com.atlas.model.Client;
import br.com.atlas.model.Person;
import br.com.atlas.util.ConnectionDb;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class testeConexao {

    public static void main(String[] args) throws Exception {

        Connection connection = ConnectionDb.getConexao();

        if (connection == null) {
            System.out.println("❌ Falha na conexão: " + ConnectionDb.getUltimoErro());
            return;
        }

        System.out.println("✅ Conexão estabelecida!\n");

        PersonDAO personDao = new PersonDAO(connection);
        ClientDAO clientDao = new ClientDAO(connection);
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("=== MENU ===");
            System.out.println("1 - Inserir Person");
            System.out.println("2 - Inserir Client");
            System.out.println("3 - Listar Persons");
            System.out.println("4 - Listar Clients");
            System.out.println("5 - Buscar por CPF");
            System.out.println("6 - Deletar por CPF");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {

                case 1:
                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Gênero (M/F): ");
                    String genero = scanner.nextLine();
                    System.out.print("Data de nascimento (AAAA-MM-DD): ");
                    LocalDate nascimento = LocalDate.parse(scanner.nextLine());
                    personDao.insert(new Person(cpf, nome, email, genero, nascimento));
                    System.out.println("✅ Person inserido!\n");
                    break;

                case 2:
                    System.out.print("CPF: ");
                    String cpfC = scanner.nextLine();
                    System.out.print("Nome: ");
                    String nomeC = scanner.nextLine();
                    System.out.print("Email: ");
                    String emailC = scanner.nextLine();
                    System.out.print("Gênero (M/F): ");
                    String generoC = scanner.nextLine();
                    System.out.print("Data de nascimento (AAAA-MM-DD): ");
                    LocalDate nascimentoC = LocalDate.parse(scanner.nextLine());
                    System.out.print("Endereço: ");
                    String endereco = scanner.nextLine();
                    clientDao.insert(new Client(cpfC, nomeC, emailC, generoC, nascimentoC, endereco));
                    System.out.println("✅ Client inserido!\n");
                    break;

                case 3:
                    System.out.println("\n--- PERSONS ---");
                    List<Person> persons = personDao.findAll();
                    for (Person p : persons) {
                        System.out.println(p.getCpf() + " | " + p.getName() + " | " + p.getEmail());
                    }
                    System.out.println();
                    break;

                case 4:
                    System.out.println("\n--- CLIENTS ---");
                    List<Client> clients = clientDao.findAll();
                    for (Client c : clients) {
                        System.out.println(c.getCpf() + " | " + c.getName() + " | " + c.getAddress());
                    }
                    System.out.println();
                    break;

                case 5:
                    System.out.print("CPF para buscar: ");
                    String cpfBusca = scanner.nextLine();
                    Client found = clientDao.findByCpf(cpfBusca);
                    if (found != null) {
                        System.out.println("✅ " + found.getCpf() + " | " + found.getName() + " | " + found.getAddress());
                    } else {
                        System.out.println("❌ Não encontrado.");
                    }
                    System.out.println();
                    break;

                case 6:
                    System.out.print("CPF para deletar: ");
                    String cpfDelete = scanner.nextLine();
                    clientDao.delete(cpfDelete);
                    System.out.println("✅ Deletado!\n");
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("❌ Opção inválida!\n");
            }
        }

        connection.close();
        scanner.close();
    }
}