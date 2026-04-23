package br.com.atlas.model;


public class Book {

    private int bookId;
    private String bookName;
    private String bookLocation;
    private int numberOfPages;
    private String bookSubject;
    private String bookOrigin;
    private Publisher publisher;

    public Book() {
    }

    public Book(int bookId, String bookName, String bookLocation, int numberOfPages,
                String bookSubject, String bookOrigin, Publisher publisher) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookLocation = bookLocation;
        this.numberOfPages = numberOfPages;
        this.bookSubject = bookSubject;
        this.bookOrigin = bookOrigin;
        this.publisher = publisher;
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

    public String getBookSubject() {
        return bookSubject;
    }

    public void setBookSubject(String bookSubject) {
        this.bookSubject = bookSubject;
    }

    public String getBookOrigin() {
        return bookOrigin;
    }

    public void setBookOrigin(String bookOrigin) {
        this.bookOrigin = bookOrigin;
    }

}