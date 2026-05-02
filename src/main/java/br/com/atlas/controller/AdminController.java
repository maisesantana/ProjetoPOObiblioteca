package br.com.atlas.controller;

import br.com.atlas.model.Administrator;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Librarian;
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
                List<Attendant> atts = adm.getAllAttendants();
                List<Librarian> libs = adm.getAllLibrarians();
                List<Administrator> ads = adm.getAllAdmins();

                admv.showAllEmployees(atts, libs, ads);
                return op;
            case 2:
                if (admv.registerP() == null) {
                    return op;
                } else {
                    try {
                        adm.register(admv.registerP());
                        System.out.println("Funcionario cadastrado com sucesso.");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                return op;
            case 0: 
                return op;
            default:
                System.out.println("Digite uma opção válida!");
                return op;
        }
    }
}