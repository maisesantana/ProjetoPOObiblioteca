package br.com.atlas.model;

import java.time.LocalDate;

public class Librarian extends Employee {

    public Librarian(String cpf, String name, String socialName, String email, String gender,
                     LocalDate birthDate, int password) {
        super(cpf, name, socialName, email, gender, birthDate, password);
    }
}