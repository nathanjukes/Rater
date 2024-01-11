package Rater.Models.Alerts;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrgAlertUpdateRequest {
    private Integer userDenialThreshold;
    private Integer apiDenialThreshold;
    private Integer userSurgeThreshold;
    private Integer apiSurgeThreshold;

    @JsonCreator
    public OrgAlertUpdateRequest(Integer userDenialThreshold, Integer apiDenialThreshold, Integer userSurgeThreshold, Integer apiSurgeThreshold) {
        this.userDenialThreshold = userDenialThreshold;
        this.apiDenialThreshold = apiDenialThreshold;
        this.userSurgeThreshold = userSurgeThreshold;
        this.apiSurgeThreshold = apiSurgeThreshold;
    }

    public Integer getUserDenialThreshold() {
        return userDenialThreshold;
    }

    public Integer getApiDenialThreshold() {
        return apiDenialThreshold;
    }

    public Integer getUserSurgeThreshold() {
        return userSurgeThreshold;
    }

    public Integer getApiSurgeThreshold() {
        return apiSurgeThreshold;
    }
}
