package br.com.atlas.application;

// import java.time.LocalDate;

import br.com.atlas.controller.AdminController;
import br.com.atlas.controller.LibrarianController;
import br.com.atlas.controller.LoginController;
import br.com.atlas.model.*;
import br.com.atlas.view.AdminView;

public class Main {
    public static void main(String[] args) {

        // FAZ O LOGIN PRIMEIRO
        LoginController loginController = new LoginController();
        Employee funcionario = loginController.login();

        // REDIRECIONA PARA O MENU CORRETO CONFORME O CARGO
        if (funcionario instanceof Administrator adm) {
            AdminView admv = new AdminView();
            AdminController admc = new AdminController(adm, admv);

            int op = -1;
            do {
                op = admv.showMenu();
                op = admc.menu(op);
            } while (op != 0);

        } else if (funcionario instanceof Librarian lib) {
                br.com.atlas.view.LibrarianView libv = new br.com.atlas.view.LibrarianView();
                LibrarianController libc = new LibrarianController(lib, libv);
                libc.start();

        } else if (funcionario instanceof Attendant att) {
            // quando o menu do atendente estiver pronto, vai aqui
            System.out.println("Menu do atendente em construção...");
        }
    }
}
