package br.com.atlas.model;

import java.time.LocalDate;

public class Employee extends Person {

    private int password;

    public Employee(String cpf, String name, String socialName, String email, String gender,
                    LocalDate birthDate, int password) {
        super(cpf, name, socialName, email, gender, birthDate);
        this.password = password;
    }

    public boolean isAdmin() {
        return this instanceof Administrator;
    }
}