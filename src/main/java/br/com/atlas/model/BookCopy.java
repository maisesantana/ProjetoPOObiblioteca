package br.com.atlas.model;

public class BookCopy {

    private int bookCopyId;
    private Book book;
    private boolean statusAvailable;
    private int publicationYear;

    public BookCopy() {
    }

    public BookCopy(int bookCopyId, Book book, boolean statusAvailable, int publicationYear) {
        this.bookCopyId = bookCopyId;
        this.book = book;
        this.statusAvailable = statusAvailable;
        this.publicationYear = publicationYear;
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

    public boolean isStatusAvailable() {
        return statusAvailable;
    }

    public void setStatusAvailable(boolean statusAvailable) {
        this.statusAvailable = statusAvailable;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
}