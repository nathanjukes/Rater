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
    @NotNull
    private HttpMethod httpMethod;
    @NotNull
    private int basicLimit;

    @JsonCreator
    public APICreateRequest(String name, UUID serviceId, HttpMethod httpMethod, Integer basicLimit) {
        this.name = name;
        this.serviceId = serviceId;
        this.httpMethod = httpMethod;
        this.basicLimit = basicLimit;
    }

    public String getName() {
        return name;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public int getBasicLimit() {
        return basicLimit;
    }
}
