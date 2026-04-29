package br.com.atlas.model;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private int bookId;
    private String bookName;
    private String bookLocation;
    private int numberOfPages;
    private String publisher;
    private List<String> authors;
    private List<String> categories;
    private List<BookCopy> copies;

    public Book() {
        this.authors = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.copies = new ArrayList<>();
    }

    public Book(String bookName, String bookLocation, int numberOfPages, String publisher) {

        this.bookName = bookName;
        this.bookLocation = bookLocation;
        this.numberOfPages = numberOfPages;
        this.publisher = publisher;

        this.authors = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.copies = new ArrayList<>();
    }

    public Book(int bookId, String bookName, String bookLocation, int numberOfPages, String publisher) {

        this.bookId = bookId;
        this.bookName = bookName;
        this.bookLocation = bookLocation;
        this.numberOfPages = numberOfPages;
        this.publisher = publisher;

        this.authors = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.copies = new ArrayList<>();
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(String bookLocation) {
        this.bookLocation = bookLocation;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<BookCopy> getCopies() {
        return copies;
    }

    public void addAuthor(String author) {
        authors.add(author);
    }

    public void removeAuthor(String author) {
        authors.remove(author);
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public void removeCategory(String category) {
        categories.remove(category);
    }

    public void addCopy(BookCopy copy) {
        copies.add(copy);
    }

    public void removeCopy(BookCopy copy) {
        copies.remove(copy);
    }

    public BookCopy findAvailableCopy() {
        for (BookCopy c : copies) {
            if (c.isAvailable()) {
                return c; // retorna o primeiro exemplar disponivel
            }
        }
        return null;
    }

    public int totalAvailableCopies() {
        int count = 0;

        for (BookCopy c : copies) {
            if (c.isAvailable()) {
                count++;
            }
        }

        return count;
    }
}