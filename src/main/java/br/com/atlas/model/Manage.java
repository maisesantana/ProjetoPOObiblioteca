package br.com.atlas.model;

import java.util.List;
import java.util.ArrayList;

class Manage {

    private List<Client> clients;
    private List<Employee> employees;

    public Manage() {
        clients = new ArrayList<>();
        employees = new ArrayList<>();
    }

    public List<Client> getClients() {
        return clients;
    }
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void addClient(Client c) {
        clients.add(c);
    }
    public void removeClient(Client c) {
        clients.remove(c);
    }

    public void addEmployee(Employee e) {
        employees.add(e);
    }
    public void removeEmployee(Employee e) {
        employees.remove(e);
    }

    public List<Client> searchClientByName(String name) {
        for (Client c : clients) {
            if (c.get)
        }
    }
}