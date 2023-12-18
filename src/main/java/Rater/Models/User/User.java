package Rater.Models.User;

import Rater.Models.Org.Org;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.UUID;

import static Rater.Models.User.UserRole.*;

@Entity
@Table(name = "users", uniqueConstraints=@UniqueConstraint(columnNames = {"email"}))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String email;

    private String password;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="org_id")//, nullable=false)
    private Org org;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String email, String password, Org org, UserRole role) {
        this.email = email;
        this.password = password;
        this.org = org;
        this.role = role;
    }

    public User() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    public UUID getOrgId() {
        return org.getId();
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public static User from(UserCreateRequest userCreateRequest, Org org) {
        return new User(userCreateRequest.getEmail(), userCreateRequest.getPassword(), org, owner);
    }

    public static User from(OrgUserCreateRequest userCreateRequest, Org org) {
        return new User(userCreateRequest.getEmail(), userCreateRequest.getPassword(), org, user);
    }

    public static User from(ServiceAccountCreateRequest serviceAccountCreateRequest, Org org) {
        return new User(serviceAccountCreateRequest.getServiceId().toString(), serviceAccountCreateRequest.getPassword(), org, owner);
    }
}
