package br.com.atlas.view;

import java.util.List;
import java.util.Scanner;

import br.com.atlas.model.Author;
import br.com.atlas.model.Book;

public class LibrarianView extends EmployeeView {

    private final Scanner sc = new Scanner(System.in);

    @Override public int showMenu() {
        System.out.println("\n====== MENU BIBLIOTECÁRIO ======");
        System.out.println("1 - Cadastrar livro");
        System.out.println("2 - Remover livro");
        System.out.println("3 - Adicionar exemplares");
        System.out.println("4 - Buscar livros");
        System.out.println("5 - Gerenciar autores");
        System.out.println("6 - Gerenciar categorias");
        System.out.println("7 - Listar todos os livros");
        System.out.println("0 - Sair");

        return sc.nextInt();
    }

    public int showAuthorMenu() {
        System.out.println("\n--- GERENCIAR AUTORES ---");
        System.out.println("1 - Listar Autores");
        System.out.println("2 - Adicionar Autor");
        System.out.println("3 - Editar Autor");
        System.out.println("4 - Remover Autor");
        System.out.println("5 - Ver livros de um Autor");
        System.out.println("0 - Voltar");
        System.out.print("Opção: ");
        return sc.nextInt();
    }

    public int showCategoryMenu() {
        System.out.println("\n--- GERENCIAR CATEGORIAS ---");
        System.out.println("1 - Listar Categorias");
        System.out.println("2 - Adicionar Categoria");
        System.out.println("3 - Editar Categoria");
        System.out.println("4 - Remover Categoria");
        System.out.println("5 - Ver livros desta Categoria");
        System.out.println("0 - Voltar");
        System.out.print("Opção: ");
        return getSc().nextInt();
    }

    public Book readBookData() {
        sc.nextLine();

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

    public String askAuthorName() {
        System.out.print("Nome do Autor: ");
        return getSc().nextLine();
    }

    public String askCategoryName() {
        System.out.print("Nome da Categoria: ");
        return getSc().nextLine();
    }

    public void showBooks(List<Book> books) {
        System.out.println("\n====== RESULTADO ======");

        if (books == null || books.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        System.out.printf("%-5s %-30s %-15s %-10s\n", "ID", "NOME", "LOCALIZAÇÃO", "PÁGINAS");
        System.out.println("-".repeat(65));
        for (Book b : books) {
            System.out.printf("%-5d %-30s %-15s %-10d\n",
                b.getBookId(),
                b.getBookName(),
                b.getBookLocation() != null ? b.getBookLocation() : "-",
                b.getNumberOfPages());
        }
    }

    public void showAuthors(List<Author> authors) {
        System.out.println("\nID | NOME DO AUTOR");
        System.out.println("-------------------");
        authors.forEach(a -> System.out.printf("%-3d| %s\n", a.getAuthorId(), a.getAuthorName()));
    }

    public int askAuthorId() {
        System.out.print("Digite o ID do Autor: ");
        return sc.nextInt();
    }

    public String readAuthorName() {
        sc.nextLine();
        System.out.print("Novo Nome do Autor: ");
        return sc.nextLine();
    }

    public void showCategories(List<br.com.atlas.model.Category> categories) {
        System.out.println("\nID | NOME DA CATEGORIA");
        System.out.println("-----------------------");
        categories.forEach(c -> System.out.printf("%-3d| %s\n", c.getCategoryId(), c.getCategoryName()));
    }

    public int askCategoryId() {
        System.out.print("Digite o ID da Categoria: ");
        return getSc().nextInt();
    }

    public String readCategoryName() {
        getSc().nextLine();
        System.out.print("Novo Nome da Categoria: ");
        return getSc().nextLine();
    }
}
