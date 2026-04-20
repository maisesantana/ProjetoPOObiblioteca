package br.com.atlas.model;

import java.time.LocalDate;

public class Employee extends Person {

    public Employee() {
    }

    public Employee(String cpf, String name, String socialName, String email, String gender,
                    LocalDate birthDate, String password, String phone, String rg) {

        super(cpf, name, socialName, email, gender, birthDate, password, phone, rg);
    }

    public boolean isAdmin() {
        return this instanceof Administrator;
    }
}