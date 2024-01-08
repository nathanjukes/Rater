package Rater.Models.Org;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrgUpdateRequest {
    @NotNull
    private Boolean healthCheckEnabled;

    @JsonCreator
    public OrgUpdateRequest(Boolean healthCheckEnabled) {
        this.healthCheckEnabled = healthCheckEnabled;
    }

    public Boolean getHealthCheckEnabled() {
        return healthCheckEnabled;
    }
}
