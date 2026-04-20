package br.com.atlas.model;

import java.time.LocalDate;

public class Client extends Person {

    private boolean isStudying;
    private String address;
    private String zipCode;
    private LocalDate startSuspensionDate;
    private LocalDate endSuspensionDate;

    public Client() {
    }

    public Client(String cpf, String name, String socialName, String email, String gender,
                  LocalDate birthDate, String password, String phone, String rg,
                  boolean isStudying, String address, String zipCode) {

        super(cpf, name, socialName, email, gender, birthDate, password, phone, rg);
        this.isStudying = isStudying;
        this.address = address;
        this.zipCode = zipCode;
    }

    public boolean isSuspended() {
        if (endSuspensionDate == null) return false;
        return LocalDate.now().isBefore(endSuspensionDate);
    }

    public void applySuspension(int days) {
        this.startSuspensionDate = LocalDate.now();
        this.endSuspensionDate = LocalDate.now().plusDays(days);
    }

    public boolean canBorrow() {
        return !isSuspended();
    }

    public boolean isStudying() {
        return isStudying;
    }

    public void setStudying(boolean studying) {
        isStudying = studying;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
}