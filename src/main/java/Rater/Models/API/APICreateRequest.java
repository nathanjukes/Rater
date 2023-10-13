package Rater.Models.API;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class APICreateRequest {
    @NotBlank
    private String name;
    @NotNull
    private UUID serviceId;

    @JsonCreator
    public APICreateRequest(String name, UUID serviceId) {
        this.name = name;
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public UUID getServiceId() {
        return serviceId;
    }
}
