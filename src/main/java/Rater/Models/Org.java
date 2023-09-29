package Rater.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orgs", uniqueConstraints=@UniqueConstraint(columnNames = {"name", "id"}))
public class Org {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "org", cascade = CascadeType.ALL)
    private Set<App> apps;

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

    public static Org from(OrgCreateRequest orgCreateRequest) {
        return new Org(orgCreateRequest.getName());
    }
}
