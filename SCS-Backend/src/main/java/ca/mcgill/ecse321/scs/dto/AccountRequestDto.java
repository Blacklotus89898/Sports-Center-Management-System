package ca.mcgill.ecse321.scs.dto;

public class AccountRequestDto {
    // class to handle login requests

    private String email;
    private String password;

    public AccountRequestDto() {}

    public AccountRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }
}
