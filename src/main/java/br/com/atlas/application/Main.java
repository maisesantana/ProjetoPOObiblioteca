package br.com.atlas.application;

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
            // 1. Criamos a View para ler as entradas do teclado
            br.com.atlas.view.AttendantView attv = new br.com.atlas.view.AttendantView();
            // 2. Criamos o Controller passando o atendente logado e a view
            br.com.atlas.controller.AttendantController attc = new br.com.atlas.controller.AttendantController(att, attv);
            // 3. Chamamos o método start() que contém o "do-while" do menu
            attc.start();

        }
    }
}
        
