package br.com.atlas.model;

import java.time.LocalDate;

public class Attendant extends Employee {

    public Attendant(String cpf, String name, String email, String gender,
            LocalDate birthDate, int password) {
        super(cpf, name, email, gender, birthDate, password);
    }

    @Override
    public void register(Person c, Manage m) {
        if (c instanceof Client) { //Só insere se for instancia de cliente!!
            m.addClient((Client) c); //faz conversao de pessoa pra cliente
        }
    }

    @Override
    public void remove(Person c, Manage m) {
        if (c instanceof Client) { //Só remove se for instancia de cliente!!
            m.removeClient((Client) c); //faz conversao de pessoa pra cliente
        }
    }

    @Override
    public void update(Person c,  Manage m) {
        if (c instanceof Client) {
            for (Person rc : m.getPeople()) { //rc = real client, um cliente q ja existe
                //se o cpf do cliente q existe bater com o do novo objeto passado 
                //por parametro, ele vai editar a pessoa existente.
                if (rc.getCpf().equals(c.getCpf())) {
                
                    rc.setName(c.getName());
                    rc.setEmail(c.getEmail());
                    rc.setGender(c.getGender());
                    rc.setBirthDate(c.getBirthDate());

                    break; //sai do loop qnd encontra!
                }
            }
        }
    }
}