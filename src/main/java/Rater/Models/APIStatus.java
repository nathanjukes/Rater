package Rater.Models;

import java.util.UUID;

public class APIStatus {
    private UUID apiId;
    private int apiLimit;
    private int currentLoad;

    public APIStatus(API api, int currentLoad) {
        this.apiId = api.getId();
        this.apiLimit = api.getApiLimit();
        this.currentLoad = currentLoad;
    }

    public UUID getApiId() {
        return apiId;
    }

    public void setApiId(UUID apiId) {
        this.apiId = apiId;
    }

    public int getApiLimit() {
        return apiLimit;
    }

    public void setApiLimit(int apiLimit) {
        this.apiLimit = apiLimit;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }

    public boolean getExceeded() {
        return currentLoad > apiLimit;
    }
}
