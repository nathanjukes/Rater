package Rater.Models.API;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class APICreateRequest {
    @NotBlank
    private String name;
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
