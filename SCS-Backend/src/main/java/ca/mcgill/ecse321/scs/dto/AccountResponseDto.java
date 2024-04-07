package ca.mcgill.ecse321.scs.dto;

import java.time.LocalDate;

import ca.mcgill.ecse321.scs.model.Account;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.model.Instructor;
import ca.mcgill.ecse321.scs.model.Owner;
import ca.mcgill.ecse321.scs.dto.RoleDto.Role;;

public class AccountResponseDto {
    // class to handle login responses

    private Integer id;
    private Role role;
    private String name;
    private String email;
    private String password;
    private LocalDate creationDate;
    private byte[] image;

    public AccountResponseDto() {
    }

    public AccountResponseDto(Integer id, Role role, String name, String email, String password, LocalDate creationDate, byte[] image) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
        this.creationDate = creationDate;
        this.image = image;
    }

    public AccountResponseDto(Account account) {
        Role accRole = null;
        if (account instanceof Customer) {
            accRole = Role.CUSTOMER;
        } else if (account instanceof Instructor) {
            accRole = Role.INSTRUCTOR;
        } else if (account instanceof Owner) {
            accRole = Role.OWNER;
        }

        this.id = account.getAccountId();
        this.role = accRole;
        this.name = account.getName();
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.creationDate = account.getCreationDate().toLocalDate();
        this.image = account.getImage();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public Role getRole() {
        return this.role;
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
