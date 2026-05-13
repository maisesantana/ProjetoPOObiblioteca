package br.com.atlas.model;

import java.time.LocalDate;

public class Administrator extends Employee {

    public Administrator(String cpf, String name, String email, char gender,
            LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate, password);
    }
}