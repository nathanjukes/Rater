package Rater.Models.Alerts;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class UserAlertCreateRequest {
    @NotNull
    private String userData;

    @JsonCreator
    public UserAlertCreateRequest(String userData) {
        this.userData = userData;
    }

    public String getUserData() {
        return userData;
    }
}
