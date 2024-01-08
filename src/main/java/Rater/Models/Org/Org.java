package Rater.Models.Org;

import Rater.Models.App.App;
import Rater.Models.User.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orgs", uniqueConstraints=@UniqueConstraint(columnNames = {"name"}))
public class Org {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "org", cascade = CascadeType.ALL)
    private Set<App> apps;

    @JsonManagedReference
    @OneToMany(mappedBy = "org", cascade = CascadeType.ALL)
    private Set<User> users;

    private Boolean healthPageEnabled;

    public Org(String name) {
        this.name = name;
    }

    public Org() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<App> getApps() {
        return apps;
    }

    public void setApps(Set<App> apps) {
        this.apps = apps;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public boolean isHealthPageEnabled() {
        return healthPageEnabled == null ? false : healthPageEnabled;
    }

    public void setHealthPageEnabled(Boolean healthPageEnabled) {
        this.healthPageEnabled = healthPageEnabled;
    }

    public static Org from(OrgCreateRequest orgCreateRequest) {
        return new Org(orgCreateRequest.getName());
    }
}
