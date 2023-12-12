package Rater.Models.API.Rules;

import Rater.Models.API.API;

import java.util.Optional;
import java.util.UUID;

public class SearchRuleResponse {
    private Optional<? extends Rule> rule;

    private Optional<UUID> orgId;

    private Optional<UUID> appId;

    private Optional<UUID> serviceId;

    private Optional<UUID> apiId;

    public SearchRuleResponse(Optional<? extends Rule> rule, Optional<API> api) {
        this.rule = rule;
        this.orgId = api.map(API::getOrgId);
        this.appId = api.map(API::getAppId);
        this.serviceId = api.map(API::getServiceId);
        this.apiId = api.map(API::getId);
    }

    public Optional<? extends Rule> getRule() {
        return rule;
    }

    public void setRule(Optional<? extends Rule> rule) {
        this.rule = rule;
    }

    public Optional<UUID> getOrgId() {
        return orgId;
    }

    public void setOrgId(Optional<UUID> orgId) {
        this.orgId = orgId;
    }

    public Optional<UUID> getAppId() {
        return appId;
    }

    public void setAppId(Optional<UUID> appId) {
        this.appId = appId;
    }

    public Optional<UUID> getServiceId() {
        return serviceId;
    }

    public void setServiceId(Optional<UUID> serviceId) {
        this.serviceId = serviceId;
    }

    public Optional<UUID> getApiId() {
        return apiId;
    }

    public void setApiId(Optional<UUID> apiId) {
        this.apiId = apiId;
    }
}
