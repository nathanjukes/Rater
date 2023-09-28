package Rater.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "apiDetails", uniqueConstraints=@UniqueConstraint(columnNames={"api", "orgId"}))
public class API {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String api;
    private UUID orgId;
    private int apiLimit;

    /*@ManyToOne
    @JoinColumn(name="service_id", nullable=false)
    private Service service;*/

    @JsonCreator
    public API(String api, UUID orgId, int apiLimit) {
        this.api = api;
        this.orgId = orgId;
        this.apiLimit = apiLimit;
    }

    public API() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public UUID getOrgId() {
        return orgId;
    }

    public void setOrgId(UUID orgId) {
        this.orgId = orgId;
    }

    public int getApiLimit() {
        return apiLimit;
    }

    public void setApiLimit(int apiLimit) {
        this.apiLimit = apiLimit;
    }
}
