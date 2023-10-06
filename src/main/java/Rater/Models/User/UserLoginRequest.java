package Rater.Models.User;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class UserLoginRequest {
    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @JsonCreator
    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
