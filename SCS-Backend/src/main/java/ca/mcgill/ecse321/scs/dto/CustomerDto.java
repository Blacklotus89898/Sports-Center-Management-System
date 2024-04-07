package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.Customer;

public class CustomerDto {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private byte[] image;

    public CustomerDto(Integer id, String name, String email, String password, byte[] image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public CustomerDto(Customer customer) {
        this.id = customer.getAccountId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.password = customer.getPassword();
        this.image = customer.getImage();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}