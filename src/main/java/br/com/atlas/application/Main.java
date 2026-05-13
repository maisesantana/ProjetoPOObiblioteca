package br.com.atlas.application;

import br.com.atlas.controller.AdminController;
import br.com.atlas.controller.AttendantController;
import br.com.atlas.controller.LibrarianController;
import br.com.atlas.controller.LoginController;
import br.com.atlas.dao.ClientDAO;
import br.com.atlas.dao.LoanDAO;
import br.com.atlas.dao.RenewalDAO;
import br.com.atlas.dao.ReturnBookDAO;
import br.com.atlas.dao.BookCopyDAO;
import br.com.atlas.model.Administrator;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Librarian;
import br.com.atlas.service.BookCopyService;
import br.com.atlas.service.BookService;
import br.com.atlas.service.ClientService;
import br.com.atlas.service.EmployeeService;
import br.com.atlas.service.LoanService;
import br.com.atlas.service.RenewalService;
import br.com.atlas.service.ReturnBookService;
import br.com.atlas.view.AdminView;
import br.com.atlas.view.AttendantView;
import br.com.atlas.view.LibrarianView;

public class Main {

    public static void main(String[] args) {

        LoginController loginController = new LoginController();

        // Loop principal — mantém o sistema rodando após logout
        while (true) {

            Employee funcionario = loginController.login();

            if (funcionario instanceof Administrator) {

                AdminView admv = new AdminView();
                EmployeeService employeeService = new EmployeeService();
                AdminController admc = new AdminController(employeeService, admv);

                int op = -1;
                do {
                    op = admv.showMenu();

                    // opção 6 = logout (conforme AdminView.showMenu)
                    if (op == 6) {
                        loginController.logout(funcionario);
                        break;
                    }

                    op = admc.menu(op);

                } while (op != 0);

                // op == 0 = fechar o sistema
                if (op == 0) break;

            } else if (funcionario instanceof Librarian) {

                LibrarianView libv = new LibrarianView();
                BookService bookService = new BookService();
                BookCopyService bookCopyService = new BookCopyService();
                LibrarianController libc = new LibrarianController(bookService, bookCopyService, libv);
                libc.start();

                // Librarian não tem logout no menu ainda — volta pro login ao sair
                loginController.logout(funcionario);

            } else if (funcionario instanceof Attendant) {

                AttendantView attv = new AttendantView();

                ClientService clientService = new ClientService(new ClientDAO());
                LoanService loanService = new LoanService(new LoanDAO(), new ClientDAO(), new BookCopyDAO());
                RenewalService renewalService = new RenewalService(new LoanDAO(), new RenewalDAO());
                ReturnBookService returnBookService = new ReturnBookService(new LoanDAO(), new ReturnBookDAO());
                BookService bookService = new BookService();

                AttendantController attc = new AttendantController(
                    clientService, loanService, renewalService,
                    returnBookService, bookService, attv
                );

                attc.start();

                // Attendant não tem logout no menu ainda — volta pro login ao sair
                loginController.logout(funcionario);
            }
        }

        System.out.println("\nSistema encerrado. Até logo!");
    }
}