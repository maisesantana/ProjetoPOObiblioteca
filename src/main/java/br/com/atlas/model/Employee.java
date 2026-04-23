package br.com.atlas.model;

import java.time.LocalDate;

public class Employee extends Person {

    private int password;

    public Employee(String cpf, String name, String email, String gender,
                    LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate);
        this.password = password;
    }

    public boolean isAdmin() {
        return this instanceof Administrator;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void register(Person p) {

    }
}