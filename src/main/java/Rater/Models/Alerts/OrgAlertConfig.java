package Rater.Models.Alerts;

import Rater.Models.API.API;
import Rater.Models.Org.Org;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "org_alert_config", uniqueConstraints=@UniqueConstraint(columnNames={"org_id"}))
public class OrgAlertConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne
    @JoinColumn(name = "org_id")
    private Org org;
    private Integer userDenialThreshold;
    private Integer apiDenialThreshold;
    private Integer userSurgeThreshold;
    private Integer apiSurgeThreshold;

    private static final Integer USER_DENIAL_BASE_LIMIT = 100;
    private static final Integer API_DENIAL_BASE_LIMIT = 300;
    private static final Integer USER_SURGE_BASE_LIMIT = 200;
    private static final Integer API_SURGE_BASE_LIMIT = 500;

    public OrgAlertConfig(Org org, Integer userDenialThreshold, Integer apiDenialThreshold, Integer userSurgeThreshold, Integer apiSurgeThreshold) {
        this.org = org;
        setUserDenialThreshold(userDenialThreshold);
        setApiDenialThreshold(apiDenialThreshold);
        setUserSurgeThreshold(userSurgeThreshold);
        setApiSurgeThreshold(apiSurgeThreshold);
    }

    public OrgAlertConfig() {

    }

    public Integer getUserDenialThreshold() {
        return userDenialThreshold;
    }

    public void setUserDenialThreshold(Integer userDenialThreshold) {
        this.userDenialThreshold = userDenialThreshold == null ? USER_DENIAL_BASE_LIMIT : userDenialThreshold;
    }

    public Integer getApiDenialThreshold() {
        return apiDenialThreshold;
    }

    public void setApiDenialThreshold(Integer apiDenialThreshold) {
        this.apiDenialThreshold = apiDenialThreshold == null ? API_DENIAL_BASE_LIMIT : apiDenialThreshold;
    }

    public Integer getUserSurgeThreshold() {
        return userSurgeThreshold;
    }

    public void setUserSurgeThreshold(Integer userSurgeThreshold) {
        this.userSurgeThreshold = userSurgeThreshold == null ? USER_SURGE_BASE_LIMIT : userSurgeThreshold;
    }

    public Integer getApiSurgeThreshold() {
        return apiSurgeThreshold;
    }

    public void setApiSurgeThreshold(Integer apiSurgeThreshold) {
        this.apiSurgeThreshold = apiSurgeThreshold == null ? API_SURGE_BASE_LIMIT : apiSurgeThreshold;
    }

    public void updateSettings(OrgAlertUpdateRequest orgAlertUpdateRequest) {
        setUserDenialThreshold(orgAlertUpdateRequest.getUserDenialThreshold());
        setApiDenialThreshold(orgAlertUpdateRequest.getApiDenialThreshold());
        setUserSurgeThreshold(orgAlertUpdateRequest.getUserSurgeThreshold());
        setApiSurgeThreshold(orgAlertUpdateRequest.getApiSurgeThreshold());
    }

    public static OrgAlertConfig from(OrgAlertUpdateRequest orgAlertUpdateRequest, Org org) {
        return new OrgAlertConfig(org,
                orgAlertUpdateRequest.getUserDenialThreshold(),
                orgAlertUpdateRequest.getApiDenialThreshold(),
                orgAlertUpdateRequest.getUserSurgeThreshold(),
                orgAlertUpdateRequest.getApiSurgeThreshold());
    }

    public static OrgAlertConfig from(Org org) {
        return new OrgAlertConfig(org, null, null, null, null);
    }
}
