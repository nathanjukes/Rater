package Rater.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "apis", uniqueConstraints=@UniqueConstraint(columnNames={"apiName", "service_id"}))
public class API {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String apiName;
    private int apiLimit;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="service_id")
    private Service service;

    @JsonCreator
    public API(String apiName, int apiLimit, Service service) {
        this.apiName = apiName;
        this.apiLimit = apiLimit;
        this.service = service;
    }

    public API() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
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
}
