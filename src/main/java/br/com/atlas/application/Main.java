package br.com.atlas.application;

import java.time.LocalDate;

import br.com.atlas.controller.AdminController;
import br.com.atlas.model.Administrator;
import br.com.atlas.view.AdminView;

public class Main {
    public static void main(String[] args) {
        //aki sera colocado o login

        Administrator adm = new Administrator("111222333-00", "Ellen", "ellen@atlas.com", 'F', LocalDate.of(2005, 4, 5), 123);
        AdminView admv = new AdminView();
        AdminController admc = new AdminController(adm, admv);

        int op = -1;
        do {
            op = admv.showMenu();
            op = admc.menu(op);
        } while (op != 0);
    }
}
