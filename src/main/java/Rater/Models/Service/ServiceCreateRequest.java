package Rater.Models.Service;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ServiceCreateRequest {
    @NotBlank
    private String name;
    @NotNull
    private UUID appId;

    @JsonCreator
    public ServiceCreateRequest(String name, UUID appId) {
        this.name = name;
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public UUID getAppId() {
        return appId;
    }
}
