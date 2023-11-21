package Rater.Models.API.Rules;

import Rater.Models.API.API;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

import static Rater.Models.API.Rules.CustomRuleType.custom;

@Entity
@Table(name = "id_rules", uniqueConstraints=@UniqueConstraint(columnNames={"userId", "api_id"}))
public class IdRule implements Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String userId;
    private int useLimit;
    private int timePeriod;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="api_id")
    private API api;

    public IdRule(String id, int limit, API api) {
        this.userId = id;
        this.useLimit = limit;
        this.timePeriod = 60; // Default to 60secs
        this.api = api;
    }

    public IdRule() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public static IdRule from(RuleCreateRequest ruleCreateRequest, API api) {
        return new IdRule(ruleCreateRequest.getUserId(), ruleCreateRequest.getLimit(), api);
    }

    @Override
    public double getPermittedRate() {
        return (double) useLimit / timePeriod;
    }

    @Override
    public CustomRuleType getCustomRuleType() {
        return custom;
    }
}
