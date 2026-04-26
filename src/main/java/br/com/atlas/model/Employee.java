package br.com.atlas.model;

import java.time.LocalDate;

import br.com.atlas.service.Administrator;

public class Employee extends Person {

    private int password;

    public Employee(String cpf, String name, String email, String gender,
                LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate);
        this.password = password;
    }

    public boolean isAdmin() {
        return this instanceof Administrator;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void register(Person p,  Manage m) {
        m.addPerson(p);
    }

    public void remove(Person p,  Manage m) {
        m.removePerson(p);
    }

    public void update(Person p,  Manage m) {
        for (Person rp : m.getPeople()) { //rp = real person, uma pessoa q ja existe
            //se o cpf da pessoa q existe bater com o do novo objeto passado 
            //por parametro, ele vai editar a pessoa existente.
            if (rp.getCpf().equals(p.getCpf())) {
                
                rp.setName(p.getName());
                rp.setEmail(p.getEmail());
                rp.setGender(p.getGender());
                rp.setBirthDate(p.getBirthDate());

                break; //sai do loop qnd encontra!
            }
        }
    }
}