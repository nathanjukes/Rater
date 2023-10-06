package Rater.Models.App;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;

public class AppCreateRequest {
    @NotBlank
    private String name;

    @JsonCreator
    public AppCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
