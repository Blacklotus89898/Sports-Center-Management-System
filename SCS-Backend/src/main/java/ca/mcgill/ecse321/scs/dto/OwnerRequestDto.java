package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.Owner;

public class OwnerRequestDto {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private byte[] image;

    public OwnerRequestDto() {
    }

    public OwnerRequestDto(Integer id, String name, String email, String password, byte[] image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public OwnerRequestDto(Owner owner) {
        this(owner.getAccountId(), owner.getName(), owner.getEmail(), owner.getPassword(), owner.getImage());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public byte[] getImage() {
        return this.image;
    }
}
