package br.com.atlas.model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Loan {

    private int loanId;
    private LocalDateTime loanDate;
    private LocalDateTime expectedReturnDate;
    private int renewalsNumber;
    private boolean active;
    private List<Renewal> renewals;
    private Client client;
    private BookCopy bookCopy;
    private ReturnBook returnBook;

    public Loan(Client client, BookCopy bookCopy) {
        
        this.client = client;
        this.bookCopy = bookCopy;
        loanDate = LocalDateTime.now();
        expectedReturnDate = loanDate.plusDays(7);
        renewalsNumber = 0;
        renewals = new ArrayList<>();
    }

    public Loan(int loanId, Client client, BookCopy bookCopy) {
        
        this.loanId = loanId;
        this.client = client;
        this.bookCopy = bookCopy;
        loanDate = LocalDateTime.now();
        expectedReturnDate = loanDate.plusDays(7);
        renewalsNumber = 0;
        renewals = new ArrayList<>();
    }

    public List<Renewal> getRenewals () {
        return renewals;
    }
    public void setRenewals(List<Renewal> renewals) {
        this.renewals = renewals;
    }

    public ReturnBook getReturnBook() {
        return returnBook;
    }
    public void setReturnBook(ReturnBook returnBook) {
        this.returnBook = returnBook;
    }
    
    public boolean canRenew() {
        return renewalsNumber < 3 && active;
    }

    public void finishLoan() {
        this.active = false;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDateTime getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public int getRenewalsNumber() {
        return renewalsNumber;
    }

    public void setRenewalsNumber(int renewalsNumber) {
        this.renewalsNumber = renewalsNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isLate() {
        //ainda sera implementado
        return false;
    }

    public void addRenew(Renewal r) {
        renewals.add(r);
    }
}
