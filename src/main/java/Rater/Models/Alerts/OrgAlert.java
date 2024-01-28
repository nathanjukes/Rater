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
    private int deniedCount;
    private int totalCount;
    private Date startTime;
    private Date endTime;
    private boolean isUser;

    public OrgAlert(UUID orgId, String data, int deniedCount, int totalCount, Date startTime, Date endTime, boolean isUser) {
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

    public UUID getOrgId() {
        return orgId;
    }

    public String getData() {
        return data;
    }

    public int getDeniedCount() {
        return deniedCount;
    }

    public int getTotalCount() {
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
