package Rater.Models.API;

import Rater.Models.API.Rules.BaseRule;
import Rater.Models.API.Rules.IdRule;
import Rater.Models.API.Rules.IpRule;
import Rater.Models.API.Rules.RoleRule;
import Rater.Models.BuildComponent;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "apis", uniqueConstraints=@UniqueConstraint(columnNames={"name", "service_id"}))
public class API implements BuildComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private int basicLimit;

    private String flatStructure;

    private String httpMethod;

    @JsonManagedReference
    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL)
    private Set<IdRule> idRules;

    @JsonManagedReference
    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL)
    private Set<IpRule> ipRules;

    @JsonManagedReference
    @OneToMany(mappedBy = "api", cascade = CascadeType.ALL)
    private Set<RoleRule> roleRules;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="service_id")
    private Service service;

    private UUID orgId;

    private UUID appId;

    public API(String name, int apiLimit, Service service, HttpMethod httpMethod, Org org) {
        this.name = name;
        this.basicLimit = apiLimit;
        this.service = service;
        this.flatStructure = calculateFlatStructure();
        this.httpMethod = httpMethod.toString();
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

    public int getBasicLimit() {
        return basicLimit;
    }

    public void setBasicLimit(int apiLimit) {
        this.basicLimit = apiLimit;
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

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Set<IdRule> getIdRules() {
        return idRules;
    }

    public void setIdRules(Set<IdRule> idRules) {
        this.idRules = idRules;
    }

    private String calculateFlatStructure() {
        return service.getFlatStructure() + "/" + getName();
    }

    public BaseRule getBaseRule() {
        return new BaseRule(basicLimit);
    }
}
