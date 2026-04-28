package br.com.atlas.model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Loan {

    private int loanId;
    private LocalDateTime loanDate;
    private LocalDateTime expectedReturnDate;
    private boolean active;

    private List<Renewal> renewals; // usado pelo Attendant
    private int renewalCount; // usado pelo banco

    private Client client;
    private BookCopy bookCopy;
    private ReturnBook returnBook;

    // NOVO EMPRÉSTIMO
    public Loan(Client client, BookCopy bookCopy) {

        this.client = client;
        this.bookCopy = bookCopy;

        this.loanDate = LocalDateTime.now();
        this.expectedReturnDate = loanDate.plusDays(7);

        this.renewals = new ArrayList<>();
        this.renewalCount = 0;

        this.active = true;
    }

    // OBJETO DO BANCO
    public Loan(int loanId, Client client, BookCopy bookCopy,
                LocalDateTime loanDate, LocalDateTime expectedReturnDate, boolean active) {

        this.loanId = loanId;
        this.client = client;
        this.bookCopy = bookCopy;

        this.loanDate = loanDate;
        this.expectedReturnDate = expectedReturnDate;

        this.renewals = new ArrayList<>();
        this.renewalCount = 0; // será preenchido pelo DAO

        this.active = active;
    }

    public boolean canRenew() {
        return getRenewalTotal() < 3 && active;
    }

    public void finishLoan() {
        this.active = false;
    }

    public void addRenew(Renewal r) {
        renewals.add(r);
        renewalCount++; 
    }

    public int getRenewalTotal() {
        // se lista tiver dados, usa ela
        if (!renewals.isEmpty()) {
            return renewals.size();
        }
        // senão, usa valor do banco
        return renewalCount;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Renewal> getRenewals() {
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

    public int getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(int renewalCount) {
        this.renewalCount = renewalCount;
    }
}