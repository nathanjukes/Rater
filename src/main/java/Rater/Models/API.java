package Rater.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "apis", uniqueConstraints=@UniqueConstraint(columnNames={"name", "service_id", "id"}))
public class API {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private int apiLimit;

    private String flatStructure;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="service_id")
    private Service service;

    @JsonCreator
    public API(String name, int apiLimit, Service service) {
        this.name = name;
        this.apiLimit = apiLimit;
        this.service = service;
        this.flatStructure = calculateFlatStructure();
    }

    public API() {

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

    public int getApiLimit() {
        return apiLimit;
    }

    public void setApiLimit(int apiLimit) {
        this.apiLimit = apiLimit;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public UUID getServiceId() {
        return service.getId();
    }

    public UUID getAppId() {
        return service.getAppId();
    }

    public UUID getOrgId() {
        return service.getOrgId();
    }

    public String getFlatStructure() {
        return flatStructure == null ? calculateFlatStructure() : flatStructure;
    }

    public void setFlatStructure(String flatStructure) {
        this.flatStructure = flatStructure;
    }

    private String calculateFlatStructure() {
        Service service = getService();
        App app = service.getApp();
        Org org = app.getOrg();
        return org.getName() + "/" + app.getName() + "/" + service.getName() + "/" + getName();
    }
}
