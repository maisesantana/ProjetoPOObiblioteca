package br.com.atlas.model;

public class BookCategory {

    private Book book;
    private Category category;

    public BookCategory() {
    }

    public BookCategory(Book book, Category category) {
        this.book = book;
        this.category = category;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
