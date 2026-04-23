package br.com.atlas.model;

import java.time.LocalDate;

public class Person {

    private String cpf;
    private String name;
    private String email;
    private String gender;
    private LocalDate birthDate;

    public Person(String cpf, String name, String email, String gender, LocalDate birthDate) {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}