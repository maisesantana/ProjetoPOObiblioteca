package br.com.atlas.test;

import br.com.atlas.dao.PersonDAO;
import br.com.atlas.model.Person;

import java.time.LocalDate;

public class TestConnection {

    public static void main(String[] args) {

        Person p = new Person();

        p.setCpf("12345678900");
        p.setName("Ana Teste");
        p.setSocialName("Ana");
        p.setEmail("ana@email.com");
        p.setGender("F");
        p.setBirthDate(LocalDate.of(2000, 1, 1));
        p.setPassword("123");
        p.setPhone("77999999999");
        p.setRg("1234567");

        PersonDAO dao = new PersonDAO();
        dao.insert(p);

        System.out.println("✅ Pessoa inserida!");
    }
}