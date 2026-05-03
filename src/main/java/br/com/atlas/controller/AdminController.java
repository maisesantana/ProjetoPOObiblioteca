package br.com.atlas.controller;

import br.com.atlas.model.Administrator;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Librarian;
import br.com.atlas.model.Person;
import br.com.atlas.view.AdminView;

import java.util.List;

public class AdminController {

    private Administrator adm;
    private AdminView admv;

    public AdminController (Administrator adm, AdminView admv) {
        this.adm = adm;
        this.admv = admv;
    }

    public int menu(int op) {

        switch (op) {
            case 1:
                viewEmployees();
                return op;

            case 2:
                int num = register();
                if (num == 1) return -1;
                return op;

            case 3:
                edit();
                return op;

            case 0:
                return op;

            default:
                System.out.println("Digite uma opção válida!");
                return op;
        }
    }

    private void viewEmployees() {
        List<Attendant> atts = adm.getAllAttendants();
        List<Librarian> libs = adm.getAllLibrarians();
        List<Administrator> ads = adm.getAllAdmins();

        admv.showAllEmployees(atts, libs, ads);
    }

    private int register() {
        Person p = admv.registerP();

        if (p == null) return 1;

        try {
            adm.register(p);
            System.out.println("Funcionário cadastrado com sucesso.");
            return 0;

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); // regra de negócio
            return 2;

        } catch (Exception e) {
            System.out.println("Erro inesperado ao cadastrar funcionário.");
            return 2;
        }
    }

    private void edit() {
        String cpf = admv.passCpf();

        try {
            Person p = adm.findPersonByCpf(cpf);

            // aqui você continua o fluxo depois
            System.out.println("Funcionário encontrado: " + p.getName());

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); // CPF não existe

        } catch (Exception e) {
            System.out.println("Erro inesperado ao buscar funcionário.");
        }
    }
}