package br.com.atlas.model;

import java.time.LocalDate;

public class Person {

    private String cpf;
    private String name;
    private String email;
    private char gender;
    private LocalDate birthDate;

    public Person() {}

    public Person(String cpf, String name, String email, char gender, LocalDate birthDate) {
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }
    
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}