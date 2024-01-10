package Rater.Models.Alerts;

import Rater.Models.Org.Org;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "orgUserAlerts", uniqueConstraints = @UniqueConstraint(columnNames = {"orgId", "userId"}))
public class UserAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "orgId")
    private Org org;

    private UUID userId;

    public UserAlert(Org org, UUID userId) {
        this.org = org;
        this.userId = userId;
    }

    public UserAlert() {

    }

    public UUID getOrgId() {
        return org.getId();
    }

    public UUID getUserId() {
        return userId;
    }
}
