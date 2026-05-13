package br.com.atlas.controller;

import br.com.atlas.dto.EmployeeDTO;
import br.com.atlas.model.Administrator;
import br.com.atlas.model.Attendant;
import br.com.atlas.model.Employee;
import br.com.atlas.model.Librarian;
import br.com.atlas.model.Person;
import br.com.atlas.service.EmployeeService;
import br.com.atlas.view.AdminView;

import java.util.List;

public class AdminController {

    private final EmployeeService employeeService;
    private final AdminView admv;

    public AdminController(EmployeeService employeeService, AdminView admv) {
        this.employeeService = employeeService;
        this.admv = admv;
    }

    public int menu(int op) {
        switch (op) {
            case 1 -> viewEmployees();
            case 2 -> { int num = register(); if (num == 1) return -1; }
            case 3 -> edit();
            case 4 -> remove();
            case 5 -> search();
            case 0 -> { return op; }
            default -> System.out.println("Digite uma opção válida!");
        }
        return op;
    }

    private void viewEmployees() {
        List<Attendant> atts = employeeService.getAllAttendants();
        List<Librarian> libs = employeeService.getAllLibrarians();
        List<Administrator> ads = employeeService.getAllAdmins();
        admv.showAllEmployees(atts, libs, ads);
    }

    private int register() {
        int kind = admv.selectKindOfEmployee();
        if (kind == 0) return 1;

        EmployeeDTO emp = (EmployeeDTO) admv.doRegister();

        Employee e;
        if (kind == 1) {
            e = new Attendant(emp.getCpf(), emp.getName(), emp.getEmail(), emp.getGender(), emp.getBirthDate(), emp.getPassword());
        } else if (kind == 2) {
            e = new Librarian(emp.getCpf(), emp.getName(), emp.getEmail(), emp.getGender(), emp.getBirthDate(), emp.getPassword());
        } else {
            e = new Administrator(emp.getCpf(), emp.getName(), emp.getEmail(), emp.getGender(), emp.getBirthDate(), emp.getPassword());
        }

        try {
            employeeService.insert(e);
            System.out.println("Funcionário cadastrado com sucesso.");
            return 0;
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            return 2;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 2;
        }
    }

    private void edit() {
        String cpf = admv.passCpf();

        try {
            Employee found = employeeService.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado."));

            boolean confirm = admv.confirmEmployee(found);
            if (!confirm) { System.out.println("Operação cancelada."); return; }

            EmployeeDTO dto = (EmployeeDTO) admv.doEdit(found);

            Employee atualizado;
            if (found instanceof Attendant) {
                atualizado = new Attendant(dto.getCpf(), dto.getName(), dto.getEmail(), dto.getGender(), dto.getBirthDate(), dto.getPassword());
            } else if (found instanceof Librarian) {
                atualizado = new Librarian(dto.getCpf(), dto.getName(), dto.getEmail(), dto.getGender(), dto.getBirthDate(), dto.getPassword());
            } else {
                atualizado = new Administrator(dto.getCpf(), dto.getName(), dto.getEmail(), dto.getGender(), dto.getBirthDate(), dto.getPassword());
            }

            employeeService.update(atualizado);
            System.out.println("Funcionário atualizado com sucesso.");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao atualizar funcionário.");
        }
    }

    private void remove() {
        String cpf = admv.passCpf();

        try {
            Employee found = employeeService.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado."));

            boolean confirm = admv.confirmRemove(found);
            if (!confirm) { System.out.println("Operação cancelada."); return; }

            employeeService.delete(cpf);
            System.out.println("Funcionário removido com sucesso.");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao remover funcionário.");
        }
    }

    private void search() {
        String cpf = admv.passCpf();

        try {
            Person found = employeeService.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado."));

            admv.showSearchResult(found);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao buscar funcionário.");
        }
    }
}