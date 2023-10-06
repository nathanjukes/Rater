package Rater.Models.App;

import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "apps", uniqueConstraints=@UniqueConstraint(columnNames = {"name", "org_id"}))
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String flatStructure;

    @JsonManagedReference
    @OneToMany(mappedBy = "app", cascade = CascadeType.ALL)
    private Set<Service> services;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="org_id")//, nullable=false)
    private Org org;

    public App(String name, Org org) {
        this.name = name;
        this.org = org;
        this.flatStructure = calculateFlatStructure();
    }

    public App() {

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

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
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

    public String getFlatStructure() {
        return flatStructure == null ? calculateFlatStructure() : flatStructure;
    }

    public void setFlatStructure(String flatStructure) {
        this.flatStructure = flatStructure;
    }

    private String calculateFlatStructure() {
        return org.getName() + "/" + getName();
    }

    public static App from(AppCreateRequest appCreateRequest, Org org) {
        return new App(appCreateRequest.getName(), org);
    }
}
