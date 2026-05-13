package br.com.atlas.model;

import java.time.LocalDate;

public class Librarian extends Employee {

    public Librarian(String cpf, String name, String email, char gender,
                    LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate, password);
    }
}