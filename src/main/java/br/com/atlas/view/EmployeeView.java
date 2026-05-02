package br.com.atlas.view;

import java.io.IOException; // pacote para detectar se é linux ou windows
import java.util.Scanner;

import br.com.atlas.model.Person;

public class EmployeeView {
    
    public int showMenu() {

        Scanner sc = new Scanner(System.in);
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

    public Person registerP() {
        return new Person();
    }
}
