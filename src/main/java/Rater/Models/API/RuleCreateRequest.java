package Rater.Models.API;

import Rater.Exceptions.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class RuleCreateRequest {
    private String userId;
    private String userIp;
    private String role;
    @NotNull
    private int limit;
    @NotNull
    private UUID apiId;
    private RuleType ruleType;

    @JsonCreator
    public RuleCreateRequest(String userId, String userIp, String role, int limit, UUID apiId) throws BadRequestException {
        this.userId = userId;
        this.userIp = userIp;
        this.role = role;
        this.limit = limit;
        this.apiId = apiId;

        if (userId != null) {
            if (userIp != null || role != null) {
                throw new BadRequestException();
            } else {
                ruleType = RuleType.id;
            }
        } else if (userIp != null) {
            if (userId != null || role != null) {
                throw new BadRequestException();
            } else {
                ruleType = RuleType.ip;
            }
        } else if (role != null) {
            if (userId != null || userIp != null) {
                throw new BadRequestException();
            } else {
                ruleType = RuleType.role;
            }
        } else {
            throw new BadRequestException("User Id/ Ip / Role must be present");
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public UUID getApiId() {
        return apiId;
    }

    public void setApiId(UUID apiId) {
        this.apiId = apiId;
    }

    public RuleType getRuleType() {
        return ruleType;
    }
}
