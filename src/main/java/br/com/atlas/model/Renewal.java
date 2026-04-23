package br.com.atlas.model;

import java.time.LocalDateTime;

public class Renewal {

    private int renewalId;
    private LocalDateTime newReturnDate;
    private int renewalNumber;
    private Loan loan;

    public Renewal(int renewalId, LocalDateTime newReturnDate, int renewalNumber, Loan loan) {
        this.renewalId = renewalId;
        this.newReturnDate = newReturnDate;
        this.renewalNumber = renewalNumber;
        this.loan = loan;
    }

    public Renewal(LocalDateTime newReturnDate, int renewalNumber, Loan loan) {
        this.newReturnDate = newReturnDate;
        this.renewalNumber = renewalNumber;
        this.loan = loan;
    }

    public int getRenewalId() {
        return renewalId;
    }

    public void setRenewalId(int renewalId) {
        this.renewalId = renewalId;
    }

    public LocalDateTime getNewReturnDate() {
        return newReturnDate;
    }

    public void setNewReturnDate(LocalDateTime newReturnDate) {
        this.newReturnDate = newReturnDate;
    }

    public int getRenewalNumber() {
        return renewalNumber;
    }

    public void setRenewalNumber(int renewalNumber) {
        this.renewalNumber = renewalNumber;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}