package br.com.atlas.controller;

import br.com.atlas.model.Book;
import br.com.atlas.model.Librarian;
import br.com.atlas.view.LibrarianView;
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

    // Método auxiliar
    private void pressEnterToContinue() {

        System.out.println(
            "\nPressione Enter para continuar..."
        );

        new java.util.Scanner(System.in)
            .nextLine();
    }
}