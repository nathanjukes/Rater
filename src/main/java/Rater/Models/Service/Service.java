package Rater.Models.Service;

import Rater.Models.API.API;
import Rater.Models.App.App;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "services", uniqueConstraints=@UniqueConstraint(columnNames = {"name", "app_id"}))
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String flatStructure;

    @JsonManagedReference
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private Set<API> apis;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="app_id")
    private App app;

    public Service(String name, App app) {
        this.name = name;
        this.app = app;
        this.flatStructure = calculateFlatStructure();
    }

    public Service() {

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

    public Set<API> getApis() {
        return apis;
    }

    public void setApis(Set<API> apis) {
        this.apis = apis;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public UUID getAppId() {
        return app.getId();
    }

    public UUID getOrgId() {
        return app.getOrgId();
    }

    public String getFlatStructure() {
        return flatStructure == null ? calculateFlatStructure() : flatStructure;
    }

    public void setFlatStructure(String flatStructure) {
        this.flatStructure = flatStructure;
    }

    private String calculateFlatStructure() {
        return app.getFlatStructure() + "/" + getName();
    }
}