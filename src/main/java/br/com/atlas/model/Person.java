package br.com.atlas.model;

import java.time.LocalDate;

public class Person {

    private String cpf;
    private String name;
    private String socialName;
    private String email;
    private String gender;
    private LocalDate birthDate;
    private String password;
    private String phone;
    private String rg;

    public Person() {
    }

    public Person(String cpf, String name, String socialName, String email, String gender,
                  LocalDate birthDate, String password, String phone, String rg) {
        this.cpf = cpf;
        this.name = name;
        this.socialName = socialName;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.password = password;
        this.phone = phone;
        this.rg = rg;
    }

    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
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

    public String getSocialName() {
        return socialName;
    }

    public void setSocialName(String socialName) {
        this.socialName = socialName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }
}