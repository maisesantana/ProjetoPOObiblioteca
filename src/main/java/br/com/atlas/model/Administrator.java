package br.com.atlas.model;

import java.time.LocalDate;

public class Administrator extends Employee {

    public Administrator() {
    }

    public Administrator(String cpf, String name, String socialName, String email, String gender,
                         LocalDate birthDate, String password, String phone, String rg) {
        super(cpf, name, socialName, email, gender, birthDate, password, phone, rg);
    }
}