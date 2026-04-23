package br.com.atlas.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Client extends Person {

    private String address;
    private LocalDate startSuspensionDate;
    private LocalDate endSuspensionDate;
    private List<Loan> loans; 

    public Client(String cpf, String name, String email, String gender, 
        LocalDate birthDate, String adress) {
        
        super(cpf, name, email, gender, birthDate);
        this.address = adress;
        startSuspensionDate = null;
        endSuspensionDate = null;
        loans = new ArrayList<>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getStartSuspensionDate() {
        return startSuspensionDate;
    }

    public void setStartSuspensionDate(LocalDate startSuspensionDate) {
        this.startSuspensionDate = startSuspensionDate;
    }

    public LocalDate getEndSuspensionDate() {
        return endSuspensionDate;
    }

    public void setEndSuspensionDate(LocalDate endSuspensionDate) {
        this.endSuspensionDate = endSuspensionDate;
    }

    public List<Loan> getLoans() {
        return loans;
    }
    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public boolean isSuspended() { //metodo OK
        if (endSuspensionDate == null) return false;
        return LocalDate.now().isBefore(endSuspensionDate);
    }

    public void applySuspension(int days) { // OK
        this.startSuspensionDate = LocalDate.now();
        this.endSuspensionDate = LocalDate.now().plusDays(days);
    }

    public boolean canBorrow() { //OK
        return !isSuspended();
    }

    public void addLoan(Loan loan) {
        this.loans.add(loan);
    }

    public boolean checkClientStatus() {
        if (isSuspended()) return false;

        for (Loan loan : loans) {
            if (loan.isLate()) {
                return false;
            }
        }
        return true;
    }

}