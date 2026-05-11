package br.com.atlas.controller;

import br.com.atlas.dao.AuthorDAO;
import br.com.atlas.dao.CategoryDAO;
import br.com.atlas.model.Author;
import br.com.atlas.model.Book;
import br.com.atlas.model.Librarian;
import br.com.atlas.view.LibrarianView;

import java.sql.Connection;
import java.util.List;

public class LibrarianController {

    private Librarian librarian;
    private LibrarianView view;

    public LibrarianController(Librarian librarian, LibrarianView view) {
        this.librarian = librarian;
        this.view = view;
    }

    public void start() {

        int op;

        do {

            br.com.atlas.view.EmployeeView.clearScreen();

            op = view.showMenu();

            switch (op) {

                case 1 -> registerBook();

                case 2 -> removeBook();

                case 3 -> addCopies();

                case 4 -> searchBooks();

                case 5 -> manageAuthors();

                case 6 -> manageCategories();

                case 0 -> System.out.println("Saindo...");

                default -> System.out.println("❌ Opção inválida!");
            }

        } while (op != 0);
    }

    private void registerBook() {

        // Dados principais do livro
        Book b = view.readBookData();

        // Nome do autor
        String authorName = view.askAuthorName();

        // Nome da categoria
        String categoryName = view.askCategoryName();

        // Adiciona ao objeto Book
        b.getAuthors().add(authorName);

        b.getCategories().add(categoryName);

        try {

            librarian.completeBookRegistration(b);

            System.out.println(
                "✅ Livro cadastrado com sucesso!"
            );

        } catch (Exception e) {

            System.out.println(
                "❌ Erro no cadastro: " + e.getMessage()
            );

            e.printStackTrace();
        }

        pressEnterToContinue();
    }

    private void removeBook() {

        try {

            int id = view.askBookId();

            librarian.removeBook(id);

            System.out.println("✅ Livro removido!");

        } catch (Exception e) {

            System.out.println(
                "❌ Erro ao remover: " + e.getMessage()
            );
        }

        pressEnterToContinue();
    }

    private void addCopies() {

        try {

            int id = view.askBookId();

            int qnt = view.askQuantity();

            librarian.addCopies(id, qnt);

            System.out.println(
                "✅ Exemplares adicionados!"
            );

        } catch (Exception e) {

            System.out.println(
                "❌ Erro ao adicionar exemplares: "
                + e.getMessage()
            );
        }

        pressEnterToContinue();
    }

    private void searchBooks() {

        try {

            String name = view.askBookName();

            List<Book> books =
                librarian.searchBooks(name);

            view.showBooks(books);

        } catch (Exception e) {

            System.out.println(
                "❌ Erro na busca: " + e.getMessage()
            );
        }

        pressEnterToContinue();
    }

    private void manageAuthors() {
        int op;
        try (Connection conn = br.com.atlas.util.ConnectionDb.getConexao()) {
            AuthorDAO dao = new AuthorDAO(conn);
            do {
                br.com.atlas.view.EmployeeView.clearScreen();
                op = view.showAuthorMenu();
                switch (op) {
                    case 1 -> view.showAuthors(dao.findAll());
                    case 2 -> {
                        String name = view.readAuthorName();
                        dao.insert(new Author(name));
                        System.out.println("✅ Autor cadastrado!");
                    }
                    case 3 -> {
                        int id = view.askAuthorId();
                        String name = view.readAuthorName();
                        dao.update(new Author(id, name));
                        System.out.println("✅ Autor atualizado!");
                    }
                    case 4 -> {
                        dao.delete(view.askAuthorId());
                        System.out.println("✅ Autor removido!");
                    }
                    case 5 -> {
                        int id = view.askAuthorId();
                        List<String> books = dao.findBooksByAuthor(id);
                        System.out.println("\n📚 Livros deste autor:");
                        if (books.isEmpty()) System.out.println("Nenhum livro vinculado.");
                        else books.forEach(b -> System.out.println("- " + b));
                    }
                }
                if (op != 0) pressEnterToContinue();
            } while (op != 0);
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private void manageCategories() {
        int op;
        try (Connection conn = br.com.atlas.util.ConnectionDb.getConexao()) {
            CategoryDAO dao = new CategoryDAO(conn);
            do {
                br.com.atlas.view.EmployeeView.clearScreen();
                op = view.showCategoryMenu();
                
                switch (op) {
                    case 1 -> view.showCategories(dao.findAll());
                    case 2 -> {
                        String name = view.readCategoryName();
                        dao.insert(new br.com.atlas.model.Category(name));
                        System.out.println("✅ Categoria cadastrada!");
                    }
                    case 3 -> {
                        int id = view.askCategoryId();
                        String name = view.readCategoryName();
                        dao.update(new br.com.atlas.model.Category(id, name));
                        System.out.println("✅ Categoria atualizada!");
                    }
                    case 4 -> {
                        dao.delete(view.askCategoryId());
                        System.out.println("✅ Categoria removida!");
                    }
                    case 5 -> {
                        int id = view.askCategoryId();
                        List<String> books = dao.findBooksByCategory(id);
                        System.out.println("\n📚 Livros nesta categoria:");
                        if (books.isEmpty()) System.out.println("Nenhum livro vinculado.");
                        else books.forEach(b -> System.out.println("- " + b));
                    }
                }
                if (op != 0) pressEnterToContinue();
            } while (op != 0);
        } catch (Exception e) {
            System.out.println("❌ Erro em Categorias: " + e.getMessage());
        }
    }

    // Método auxiliar
    private void pressEnterToContinue() {

        System.out.println(
            "\nPressione Enter para continuar..."
        );

        new java.util.Scanner(System.in)
            .nextLine();
    }
}