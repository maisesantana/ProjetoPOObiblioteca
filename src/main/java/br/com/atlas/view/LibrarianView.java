package br.com.atlas.view;

import br.com.atlas.model.Book;

import java.util.List;
import java.util.Scanner;

public class LibrarianView {

    private Scanner sc = new Scanner(System.in);

    public int showMenu() {
        System.out.println("\n====== MENU BIBLIOTECÁRIO ======");
        System.out.println("1 - Cadastrar livro");
        System.out.println("2 - Remover livro");
        System.out.println("3 - Adicionar exemplares");
        System.out.println("4 - Buscar livros");
        System.out.println("0 - Sair");

        return sc.nextInt();
    }

    public Book readBookData() {
        sc.nextLine(); // limpar buffer

        System.out.println("\n====== CADASTRAR LIVRO ======");
        System.out.print("Nome: ");
        String name = sc.nextLine();

        System.out.print("Localização: ");
        String location = sc.nextLine();

        System.out.print("Número de páginas: ");
        int pages = sc.nextInt();

        sc.nextLine();
        System.out.print("Editora: ");
        String publisher = sc.nextLine();

        return new Book(name, location, pages, publisher);
    }

    public int askBookId() {
        System.out.print("Digite o ID do livro: ");
        return sc.nextInt();
    }

    public int askQuantity() {
        System.out.print("Quantidade de exemplares: ");
        return sc.nextInt();
    }

    public String askBookName() {
        sc.nextLine();
        System.out.print("Nome do livro: ");
        return sc.nextLine();
    }

    public void showBooks(List<Book> books) {
        System.out.println("\n====== RESULTADO ======");

        if (books == null || books.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        for (Book b : books) {
            System.out.println("ID: " + b.getBookId());
            System.out.println("Nome: " + b.getBookName());
            System.out.println("Local: " + b.getBookLocation());
            System.out.println("--------------------------");
        }
    }
}