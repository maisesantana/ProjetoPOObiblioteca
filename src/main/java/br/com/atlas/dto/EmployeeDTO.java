package br.com.atlas.dto;

public class EmployeeDTO extends PersonDTO {
    
    private int password;

    public int getPassword() {
        return password;
    }
    public void setPassword(int password) {
        this.password = password;
    }

}
