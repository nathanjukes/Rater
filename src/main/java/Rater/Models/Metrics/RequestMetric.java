package Rater.Models.Metrics;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "metrics", indexes = @Index(name = "idx_timestamp", columnList = "timestamp"))
public class RequestMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID apiId;

    private String userData;

    private boolean requestAccepted;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    private UUID orgId;

    private UUID appId;

    private UUID serviceId;

    private int sum;

    public RequestMetric() {

    }

    public UUID getId() {
        return id;
    }

    public UUID getApiId() {
        return apiId;
    }

    public UUID getOrgId() {
        return orgId;
    }

    public UUID getAppId() {
        return appId;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getUserData() {
        return userData;
    }

    public boolean isRequestAccepted() {
        return requestAccepted;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getSum() {
        return sum;
    }
}
