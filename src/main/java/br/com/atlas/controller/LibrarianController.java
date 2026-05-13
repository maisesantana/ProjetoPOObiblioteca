package br.com.atlas.controller;

import br.com.atlas.dao.AuthorDAO;
import br.com.atlas.dao.CategoryDAO;
import br.com.atlas.model.Author;
import br.com.atlas.model.Book;
import br.com.atlas.service.BookCopyService;
import br.com.atlas.service.BookService;
import br.com.atlas.view.LibrarianView;

import java.sql.Connection;
import java.util.List;

public class LibrarianController {

    private final BookService bookService;
    private final BookCopyService bookCopyService;
    private final LibrarianView view;

    public LibrarianController(BookService bookService, BookCopyService bookCopyService, LibrarianView view) {
        this.bookService = bookService;
        this.bookCopyService = bookCopyService;
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
        Book b = view.readBookData();

        b.getAuthors().add(view.askAuthorName());
        b.getCategories().add(view.askCategoryName());

        try {
            bookService.insert(b);
            System.out.println("✅ Livro cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro no cadastro: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void removeBook() {
        try {
            bookService.delete(view.askBookId());
            System.out.println("✅ Livro removido!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao remover: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void addCopies() {
        try {
            bookCopyService.addCopies(view.askBookId(), view.askQuantity());
            System.out.println("✅ Exemplares adicionados!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao adicionar exemplares: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void searchBooks() {
        try {
            List<Book> books = bookService.findByName(view.askBookName());
            view.showBooks(books);
        } catch (Exception e) {
            System.out.println("❌ Erro na busca: " + e.getMessage());
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
                        dao.insert(new Author(view.readAuthorName()));
                        System.out.println("✅ Autor cadastrado!");
                    }
                    case 3 -> {
                        int id = view.askAuthorId();
                        dao.update(new Author(id, view.readAuthorName()));
                        System.out.println("✅ Autor atualizado!");
                    }
                    case 4 -> {
                        dao.delete(view.askAuthorId());
                        System.out.println("✅ Autor removido!");
                    }
                    case 5 -> {
                        List<String> books = dao.findBooksByAuthor(view.askAuthorId());
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
                        dao.insert(new br.com.atlas.model.Category(view.readCategoryName()));
                        System.out.println("✅ Categoria cadastrada!");
                    }
                    case 3 -> {
                        int id = view.askCategoryId();
                        dao.update(new br.com.atlas.model.Category(id, view.readCategoryName()));
                        System.out.println("✅ Categoria atualizada!");
                    }
                    case 4 -> {
                        dao.delete(view.askCategoryId());
                        System.out.println("✅ Categoria removida!");
                    }
                    case 5 -> {
                        List<String> books = dao.findBooksByCategory(view.askCategoryId());
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

    private void pressEnterToContinue() {
        System.out.println("\nPressione Enter para continuar...");
        new java.util.Scanner(System.in).nextLine();
    }
}