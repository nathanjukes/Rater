package Rater.Models.Alerts;



import jakarta.persistence.*;


import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "alerts")
public class OrgAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID orgId;
    private String data;
    private Long deniedCount;
    private Long totalCount;
    private Date startTime;
    private Date endTime;
    private boolean isUser;

    public OrgAlert(UUID orgId, String data, Long deniedCount, Long totalCount, Date startTime, Date endTime, boolean isUser) {
        this.orgId = orgId;
        this.data = data;
        this.deniedCount = deniedCount;
        this.totalCount = totalCount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isUser = isUser;
    }

    public OrgAlert() {

    }

    public UUID getId() {
        return id;
    }

    public UUID getOrgId() {
        return orgId;
    }

    public String getData() {
        return data;
    }

    public Long getDeniedCount() {
        return deniedCount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public boolean isUser() {
        return isUser;
    }
}
