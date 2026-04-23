package br.com.atlas.model;

import java.time.LocalDate;

public class Attendant extends Employee {

    public Attendant(String cpf, String name, String email, String gender,
            LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate, password);
    }
}