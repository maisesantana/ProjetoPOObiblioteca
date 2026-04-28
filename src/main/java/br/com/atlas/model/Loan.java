package br.com.atlas.model;

import java.util.List;
import java.util.ArrayList;
//import java.util.Date; comentei aq pq tava falando q ñ tava sendo usado
import java.time.LocalDateTime;

public class Loan {

    private int loanId;
    private LocalDateTime loanDate;
    private LocalDateTime expectedReturnDate;
    private boolean active;
    private List<Renewal> renewals;
    private Client client;
    private BookCopy bookCopy;
    private ReturnBook returnBook;

    public Loan(Client client, BookCopy bookCopy) { //cria novo emprestimo q vai pro banco
        
        this.client = client;
        this.bookCopy = bookCopy;
        loanDate = LocalDateTime.now();
        expectedReturnDate = loanDate.plusDays(7);
        renewals = new ArrayList<>();
        this.active = true; //pra ativar o emprestimo q eu to inserindo no banco
    }

    public Loan(int loanId, Client client, BookCopy bookCopy, LocalDateTime loanDate, LocalDateTime expectedReturnDate, boolean active) { //reconstroi o objeto a partir dos dados do banco
        
        this.loanId = loanId;
        this.client = client;
        this.bookCopy = bookCopy;
        this.loanDate = loanDate; //agr sim ele pega a data
        this.expectedReturnDate = expectedReturnDate; //usa oq o banco mandou
        renewals = new ArrayList<>();
        this.active = active; // preserva o status q ta no banco
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
        return renewals.size() < 3 && active;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addRenew(Renewal r) {
        renewals.add(r);
    }
}
