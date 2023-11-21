package Rater.Models.API.Rules;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class RuleGetRequest {
    @NotBlank
    private String data;
    @NotNull
    private RuleType type;
    @NotNull
    private UUID apiId;

    @JsonCreator
    public RuleGetRequest(String data, RuleType type, UUID apiId) {
        this.data = data;
        this.type = type;
        this.apiId = apiId;
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

    public UUID getApiId() {
        return apiId;
    }

    public void setApiId(UUID apiId) {
        this.apiId = apiId;
    }
}
