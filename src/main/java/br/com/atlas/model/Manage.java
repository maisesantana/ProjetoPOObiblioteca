package br.com.atlas.model;

import java.util.List;
import java.util.ArrayList;

class Manage {

    private List<Client> clients;
    private List<Employee> employees;
    private List<Person> people;

    public Manage() {
        clients = new ArrayList<>();
        employees = new ArrayList<>();
        people = new  ArrayList<>();
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

    public List<Person> getPeople() {
        return people;
    }
    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public void addClient(Client c) {
        clients.add(c);
        people.add(c);
    }
    public void removeClient(Client c) {
        clients.remove(c);
        people.remove(c);
    }

    public void addEmployee(Employee e) {
        employees.add(e);
        people.add(e);
    }
    public void removeEmployee(Employee e) {
        employees.remove(e);
        people.remove(e);
    }

    public void addPerson(Person p) {
        people.add(p);
    }
    public void removePerson(Person p) {
        people.remove(p);
    }

    public List<Client> searchClientByName(String name) {
        
        List<Client> temp = new ArrayList<>();
        for (Client c : clients) {
            if (c.getName().contains(name)) {
                temp.add(c);
            }
        }
        return temp;
    }

    public Client searchClientByCpf(String cpf) {
        
        for (Client c : clients) {
            if (c.getCpf().equals(cpf)) {
                return c;
            }
        }
        return null;
    }

    public List<Employee> searchEmployeeByName(String name) {
        
        List<Employee> temp = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getName().contains(name)) {
                temp.add(e);
            }
        }
        return temp;
    }
}