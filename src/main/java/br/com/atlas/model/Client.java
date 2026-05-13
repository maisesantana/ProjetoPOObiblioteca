package br.com.atlas.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Client extends Person {

    private String address;
    private LocalDate startSuspensionDate;
    private LocalDate endSuspensionDate;
    private List<Loan> loans;

    public Client() {}

    public Client(String cpf, String name, String email, char gender,
        LocalDate birthDate, String address) {

        super(cpf, name, email, gender, birthDate);
        this.address = address;
        this.startSuspensionDate = null;
        this.endSuspensionDate = null;
        this.loans = new ArrayList<>();
    }

    public Client(String cpf, String name, String email, char gender,
        LocalDate birthDate, String address,
        LocalDate startSuspensionDate, LocalDate endSuspensionDate) {

        super(cpf, name, email, gender, birthDate);
        this.address = address;
        this.startSuspensionDate = startSuspensionDate;
        this.endSuspensionDate = endSuspensionDate;
        this.loans = new ArrayList<>();
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

    // Verifica se o cliente está suspenso com base na data atual
    public boolean isSuspended() {
        if (endSuspensionDate == null) return false;
        return LocalDate.now().isBefore(endSuspensionDate);
    }

    // Aplica suspensão a partir de hoje por X dias
    public void applySuspension(int days) {
        this.startSuspensionDate = LocalDate.now();
        this.endSuspensionDate = LocalDate.now().plusDays(days);
    }

    // Cliente pode pegar emprestado se não estiver suspenso
    public boolean canBorrow() {
        return !isSuspended();
    }

    public void addLoan(Loan loan) {
        this.loans.add(loan);
    }
}