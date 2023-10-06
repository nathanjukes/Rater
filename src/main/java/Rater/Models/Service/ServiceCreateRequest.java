package Rater.Models.Service;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;

public class ServiceCreateRequest {
    @NotBlank
    private String name;

    @JsonCreator
    public ServiceCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
