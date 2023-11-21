package Rater.Models.API.Rules;

import Rater.Models.API.API;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

import static Rater.Models.API.Rules.CustomRuleType.custom;


@Entity
@Table(name = "ip_rules", uniqueConstraints=@UniqueConstraint(columnNames={"userIp", "api_id"}))
public class IpRule implements Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String userIp;
    private int useLimit;
    private int timePeriod;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="api_id")
    private API api;

    public IpRule(String ip, int limit, API api) {
        this.userIp = ip;
        this.useLimit = limit;
        this.timePeriod = 60; // Default to 60secs
        this.api = api;
    }

    public IpRule() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userId) {
        this.userIp = userId;
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

    public static IpRule from(RuleCreateRequest ruleCreateRequest, API api) {
        return new IpRule(ruleCreateRequest.getUserIp(), ruleCreateRequest.getLimit(), api);
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
