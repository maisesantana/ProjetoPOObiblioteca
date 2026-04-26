package br.com.atlas.model;

public class BookCopy {

    private int bookCopyId;
    private Book book;
    private boolean available;

    public BookCopy() {}

    public BookCopy(Book book) {
        this.book = book;
        available = true;
    }

    public BookCopy(int bookCopyId, Book book, boolean available) {
        this.bookCopyId = bookCopyId;
        this.book = book;
        this.available = available;
    }

    public int getBookCopyId() {
        return bookCopyId;
    }

    public void setBookCopyId(int bookCopyId) {
        this.bookCopyId = bookCopyId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}