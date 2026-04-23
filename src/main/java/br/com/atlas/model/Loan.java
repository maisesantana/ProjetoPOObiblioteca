package br.com.atlas.model;

import java.time.LocalDateTime;

public class Loan {

    private int loanId;
    private Client client;
    private BookCopy bookCopy;
    private LocalDateTime loanDate;
    private LocalDateTime expectedReturnDate;
    private int renewals;
    private boolean active;

    public Loan() {
    }

    public Loan(int loanId, Client client, BookCopy bookCopy,
                LocalDateTime loanDate, LocalDateTime expectedReturnDate,
                int renewals, boolean active) {
        this.loanId = loanId;
        this.client = client;
        this.bookCopy = bookCopy;
        this.loanDate = loanDate;
        this.expectedReturnDate = expectedReturnDate;
        this.renewals = renewals;
        this.active = active;
    }

    public boolean canRenew() {
        return renewals < 3 && active;
    }

    public void renew() {
        if (canRenew()) {
            expectedReturnDate = expectedReturnDate.plusDays(8);
            renewals++;
        }
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

    public int getRenewals() {
        return renewals;
    }

    public void setRenewals(int renewals) {
        this.renewals = renewals;
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
}
