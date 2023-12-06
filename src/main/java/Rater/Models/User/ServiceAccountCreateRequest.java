package Rater.Models.User;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class ServiceAccountCreateRequest {
    private UUID serviceId;
    private String password;

    public ServiceAccountCreateRequest(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getPassword() {
        return password;
    }

    public void encode(PasswordEncoder encoder) {
        this.password = encoder.encode(serviceId.toString());
    }
}
