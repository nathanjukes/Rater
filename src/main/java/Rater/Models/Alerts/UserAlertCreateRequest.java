package Rater.Models.Alerts;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class UserAlertCreateRequest {
    @NotNull
    private UUID userId;

    @JsonCreator
    public UserAlertCreateRequest(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
