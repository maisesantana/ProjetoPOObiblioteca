package br.com.atlas.service;

import java.time.LocalDate;

import br.com.atlas.model.Employee;
import br.com.atlas.model.Manage;
import br.com.atlas.model.Person;

public class Administrator extends Employee {

    public Administrator(String cpf, String name, String email, String gender,
            LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate, password);
    }

    @Override
    public void register(Person e, Manage m) {
        if (e instanceof Employee) { //Só insere se for instancia de cliente!!
            m.addEmployee((Employee) e); //faz conversao de pessoa pra cliente
        }
    }

    @Override
    public void remove(Person e, Manage m) {
        if (e instanceof Employee) { //Só remove se for instancia de cliente!!
            m.removeEmployee((Employee) e); //faz conversao de pessoa pra cliente
        }
    }

    @Override
    public void update(Person e,  Manage m) {
        if (e instanceof Employee) {
            for (Person re : m.getPeople()) { //re = real employee, um funcionario q ja existe
                //se o cpf do funcionario q existe bater com o do novo objeto passado 
                //por parametro, ele vai editar a pessoa existente.
                if (re.getCpf().equals(e.getCpf())) {
                
                    re.setName(e.getName());
                    re.setEmail(e.getEmail());
                    re.setGender(e.getGender());
                    re.setBirthDate(e.getBirthDate());

                    break; //sai do loop qnd encontra!
                }
            }
        }
    }
}