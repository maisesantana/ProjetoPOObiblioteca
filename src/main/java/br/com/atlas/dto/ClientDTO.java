package br.com.atlas.dto;

public class ClientDTO extends PersonDTO {
    private String address;

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}