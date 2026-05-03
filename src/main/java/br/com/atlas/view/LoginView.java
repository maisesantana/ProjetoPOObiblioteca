package br.com.atlas.view;

import java.util.Scanner;

public class LoginView {

    private Scanner sc;

    public LoginView() {
        this.sc = new Scanner(System.in);
    }

    public Scanner getSc() {
        return sc;
    }


    // Exibe a tela de login e retorna o CPF digitado
    public String askCpf() {
        System.out.println("========================================");
        System.out.println("        SISTEMA DE BIBLIOTECA ATLAS     ");
        System.out.println("========================================");
        System.out.print("CPF: ");
        return sc.nextLine().trim();
    }

    // Solicita a senha e retorna como int
    // Retorna -1 se o usuário digitar algo que não seja número
    public int askPassword() {
        System.out.print("Senha: ");
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // sinaliza senha inválida para o controller tratar
        }
    }

    // Exibe mensagem de boas vindas com o nome e cargo do funcionário
    public void showWelcome(String name, String role) {
        EmployeeView.clearScreen();
        System.out.println("========================================");
        System.out.println("  Bem-vindo(a), " + name + "!");
        System.out.println("  Cargo: " + role);
        System.out.println("========================================");
    }

    //Exibe mensagem de erro de login
    public void showLoginError() {
        System.out.println("\n CPF ou senha inválidos. Tente novamente.\n");
    }

    // Exibe mensagem de senha inválida (não numérica)
    public void showInvalidPassword() {
        System.out.println("\n A senha deve conter apenas números. Tente novamente.\n");
    }

    // Exibe mensagem de campos vazios
    public void showEmptyFields() {
        System.out.println("\n CPF e senha não podem estar vazios. Tente novamente.\n");
    }

    // Exibe mensagem de erro de conexão com o banco
    public void showConnectionError() {
        System.out.println("\n Erro ao conectar com o banco de dados. Contate o administrador.\n");
    }
}