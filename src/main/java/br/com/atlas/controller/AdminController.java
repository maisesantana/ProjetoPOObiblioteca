package br.com.atlas.controller;

import br.com.atlas.dto.EmployeeDTO;
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
                if (num == 1) return op = -1;
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
        
        int kind = admv.selectKindOfEmployee();
        
        if (kind == 0) {
            return 1;
        }

        EmployeeDTO emp = (EmployeeDTO) admv.doRegister();

        Person p;

        if (kind == 1) {
            p = new Attendant(emp.getCpf(), emp.getName(), emp.getEmail(), emp.getGender(), emp.getBirthDate(), emp.getPassword());
        } else if (kind == 2) {
            p = new Librarian(emp.getCpf(), emp.getName(), emp.getEmail(), emp.getGender(), emp.getBirthDate(), emp.getPassword());
        } else {
            p = new Administrator(emp.getCpf(), emp.getName(), emp.getEmail(), emp.getGender(), emp.getBirthDate(), emp.getPassword());
        }

        try {
            adm.register(p);
            System.out.println("Funcionário cadastrado com sucesso.");
            return 0;

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); // regra de negócio
            return 2;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 2;
        }
    }

    private void edit() {
        String cpf = admv.passCpf();
        try {
            Person p = adm.findPersonByCpf(cpf); //tenta achar por cpf e imprime o nome do funcionario encontrado

            // confirmar antes de editar
            boolean confirm = admv.confirmEmployee(p);
            if (!confirm) {
                System.out.println("Operação cancelada.");
            return;
            }

            EmployeeDTO dto = (EmployeeDTO) admv.doEdit(p);

            Person atualizado;

            // reconstrói o objeto com os novos dados
            if (p instanceof Attendant) {
                atualizado = new Attendant(dto.getCpf(), dto.getName(), dto.getEmail(), dto.getGender(), dto.getBirthDate(), dto.getPassword());
            } else if (p instanceof Librarian) {
                atualizado = new Librarian(dto.getCpf(), dto.getName(), dto.getEmail(), dto.getGender(), dto.getBirthDate(), dto.getPassword());
            } else {
                atualizado = new Administrator(dto.getCpf(), dto.getName(), dto.getEmail(), dto.getGender(), dto.getBirthDate(), dto.getPassword());
            }

            adm.update(atualizado);

            System.out.println("Funcionário atualizado com sucesso.");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao atualizar funcionário.");
        }
    }
}