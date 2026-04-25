package br.com.atlas.model;

import java.time.LocalDateTime;

public class ReturnBook {

    private int returnId;
    private LocalDateTime returnDate;
    private Loan loan;

    public ReturnBook(int returnId, LocalDateTime returnDate, Loan loan) {
        this.returnId = returnId;
        this.returnDate = returnDate;
        this.loan = loan;
    }

    public ReturnBook(LocalDateTime returnDate, Loan loan) {
        this.returnDate = returnDate;
        this.loan = loan;
    }

    public int calculateDelayDays() {
        if (returnDate.isAfter(loan.getExpectedReturnDate())) {
            return (int) java.time.Duration.between(
                    loan.getExpectedReturnDate(), returnDate).toDays();
        }
        return 0;
    }

    public boolean isLate() {
        if (calculateDelayDays() != 0) {
            return true;
        }
        return false;
    }

    public int calculateSuspensionDays() {
        return calculateDelayDays() * 2;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
