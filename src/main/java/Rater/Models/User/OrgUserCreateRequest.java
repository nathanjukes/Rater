package Rater.Models.User;

import Rater.Exceptions.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static Rater.Models.User.UserRole.owner;

public class OrgUserCreateRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private UUID orgId;

    @NotNull
    private UserRole role;

    @JsonCreator
    public OrgUserCreateRequest(String email, String password, UUID orgId, String role) throws BadRequestException {
        this.email = email.toLowerCase();
        this.password = password;
        this.orgId = orgId;
        this.role = UserRole.valueOf(role);

        if (this.role.equals(owner)) {
            throw new BadRequestException();
        }
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

    public UUID getOrgId() {
        return orgId;
    }

    public void setOrgId(UUID orgId) {
        this.orgId = orgId;
    }

    public UserRole getRole() {
        return role;
    }

    public void encode(PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }
}
