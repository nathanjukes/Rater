package Rater.Models.API.Rules;

import Rater.Models.API.API;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

import static Rater.Models.API.Rules.CustomRuleType.custom;


@Entity
@Table(name = "role_rules", uniqueConstraints=@UniqueConstraint(columnNames={"role", "api_id"}))
public class RoleRule implements Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String role;
    private int useLimit;
    private int timePeriod;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="api_id")
    private API api;

    public RoleRule(String role, int limit, API api) {
        this.role = role;
        this.useLimit = limit;
        this.timePeriod = 60; // Default to 60secs
        this.api = api;
    }

    public RoleRule() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    @Override
    public int getUseLimit() {
        return useLimit;
    }

    public void setUseLimit(int limit) {
        this.useLimit = limit;
    }

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int seconds) {
        this.timePeriod = seconds;
    }

    public API getApi() {
        return api;
    }

    public void setApi(API api) {
        this.api = api;
    }

    public static RoleRule from(RuleCreateRequest ruleCreateRequest, API api) {
        return new RoleRule(ruleCreateRequest.getRole(), ruleCreateRequest.getLimit(), api);
    }

    @Override
    public double getPermittedRatePerSecond() {
        return (double) useLimit / timePeriod;
    }

    @Override
    public CustomRuleType getCustomRuleType() {
        return custom;
    }
}
