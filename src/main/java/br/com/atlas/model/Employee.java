package br.com.atlas.model;

import java.time.LocalDate;

public abstract class Employee extends Person {

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

    public abstract void register(Person p);
    public abstract void remove(String cpf);
    public abstract void update(Person p);
}