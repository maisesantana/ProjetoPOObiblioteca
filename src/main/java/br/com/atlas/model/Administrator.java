package br.com.atlas.model;

import java.time.LocalDate;

public class Administrator extends Employee {

    public Administrator(String cpf, String name, String socialName, String email, String gender,
                LocalDate birthDate, int password) {
        super(cpf, name, socialName, email, gender, birthDate, password);
    }
}