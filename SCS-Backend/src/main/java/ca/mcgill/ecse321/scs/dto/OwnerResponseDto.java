package ca.mcgill.ecse321.scs.dto;

import java.time.LocalDate;

import ca.mcgill.ecse321.scs.model.Owner;

public class OwnerResponseDto {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private LocalDate creationDate;
    private byte[] image;

    public OwnerResponseDto() {
    }

    public OwnerResponseDto(Integer id, String name, String email, String password, LocalDate creationDate, byte[] image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.creationDate = creationDate;
        this.image = image;
    }

    public OwnerResponseDto(Owner owner) {
        this(owner.getAccountId(), owner.getName(), owner.getEmail(), owner.getPassword(), owner.getCreationDate().toLocalDate(), owner.getImage());
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

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
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

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public byte[] getImage() {
        return this.image;
    }
}
