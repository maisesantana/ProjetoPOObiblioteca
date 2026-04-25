package br.com.atlas.model;

import java.util.ArrayList;
import java.util.List;

public class Collection {

    private List<Book> books;

    public Collection() {
        books = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
    }
    public void removeBook(Book book) {
        books.remove(book);
    }

    public List<Book> searchBookByTitle(String title) {
        List<Book> temp = new ArrayList<>();
        for (Book b : books) {
            if (b.getBookName().contains(title)) {
                temp.add(b);
            }
        }
        return temp;
    }

    public List<Book> searchBookByAuthors(String author) {
        List<Book> temp = new ArrayList<>();
        for (Book b : books) {
            for (String a : b.getAuthors()) {
                if (a.equals(author)) {
                    temp.add(b);
                }
            }
        }
        return temp;
    }

    public Book searchBookById(int id) {
        for (Book b : books) {
            if (b.getBookId() == id) {
                return b;
            }
        }
        return null;
    }
}