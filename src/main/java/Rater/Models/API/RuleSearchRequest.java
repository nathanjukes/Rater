package Rater.Models.API;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class RuleSearchRequest {
    @NotBlank
    private String data;
    @NotNull
    private RuleType type;
    @NotNull
    private UUID serviceId;
    @NotBlank
    private String apiPath;

    @JsonCreator
    public RuleSearchRequest(String data, RuleType type, UUID serviceId, String apiPath) {
        this.data = data;
        this.type = type;
        this.serviceId = serviceId;
        this.apiPath = apiPath;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public RuleType getType() {
        return type;
    }

    public void setType(RuleType type) {
        this.type = type;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    @Override
    public String toString() {
        return "RuleSearchRequest{" +
                "dataLength='" + data.length() + '\'' +
                ", type=" + type +
                ", serviceId=" + serviceId +
                ", apiPath='" + apiPath + '\'' +
                '}';
    }
}
