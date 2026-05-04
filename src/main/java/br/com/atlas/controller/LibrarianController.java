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
            op = view.showMenu();

            switch (op) {
                case 1 -> registerBook();
                case 2 -> removeBook();
                case 3 -> addCopies();
                case 4 -> searchBooks();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }

        } while (op != 0);
    }

    private void registerBook() {
        Book b = view.readBookData();
        librarian.registerBook(b);
        System.out.println("Livro cadastrado com sucesso!");
    }

    private void removeBook() {
        int id = view.askBookId();
        librarian.removeBook(id);
        System.out.println("Livro removido!");
    }

    private void addCopies() {
        int id = view.askBookId();
        int qnt = view.askQuantity();
        librarian.addCopies(id, qnt);
        System.out.println("Exemplares adicionados!");
    }

    private void searchBooks() {
        String name = view.askBookName();
        List<Book> books = librarian.searchBooks(name);
        view.showBooks(books);
    }
}