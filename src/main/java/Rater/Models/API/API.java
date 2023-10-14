package Rater.Models.API;

import Rater.Models.BuildComponent;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "apis", uniqueConstraints=@UniqueConstraint(columnNames={"name", "service_id"}))
public class API implements BuildComponent {
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

    private UUID orgId;

    private UUID appId;

    public API(String name, int apiLimit, Service service, Org org) {
        this.name = name;
        this.apiLimit = apiLimit;
        this.service = service;
        this.flatStructure = calculateFlatStructure();
        this.orgId = org.getId();
        this.appId = service.getAppId();
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
        return appId;
    }

    public UUID getOrgId() {
        return orgId;
    }

    public String getFlatStructure() {
        return flatStructure == null ? calculateFlatStructure() : flatStructure;
    }

    public void setFlatStructure(String flatStructure) {
        this.flatStructure = flatStructure;
    }

    private String calculateFlatStructure() {
        return service.getFlatStructure() + "/" + getName();
    }
}
