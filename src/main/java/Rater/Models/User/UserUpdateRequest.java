package Rater.Models.User;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserUpdateRequest {
    @NotBlank
    private UserRole role;

    @JsonCreator
    public UserUpdateRequest(String role) {
        this.role = UserRole.valueOf(role);
    }

    public UserRole getRole() {
        return this.role;
    }
}
