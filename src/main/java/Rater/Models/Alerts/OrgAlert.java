package Rater.Models.Alerts;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import java.util.UUID;

@Entity
@Table(name = "alerts")
public class OrgAlert {
    @Id
    private UUID orgId;
    private int deniedCount;
    private int totalCount;

    public OrgAlert(UUID orgId, int deniedCount, int totalCount) {
        this.orgId = orgId;
        this.deniedCount = deniedCount;
        this.totalCount = totalCount;
    }

    public OrgAlert() {

    }

    public UUID getOrgId() {
        return orgId;
    }

    public int getDeniedCount() {
        return deniedCount;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
