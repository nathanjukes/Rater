package Rater.Models.Alerts;

import Rater.Models.Org.Org;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "orgUserAlerts", uniqueConstraints = @UniqueConstraint(columnNames = {"orgId", "userData"}))
public class UserAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "orgId")
    private Org org;

    private String userData;

    public UserAlert(Org org, String userData) {
        this.org = org;
        this.userData = userData;
    }

    public UserAlert() {

    }

    public UUID getOrgId() {
        return org.getId();
    }

    public String getUserData() {
        return userData;
    }
}
