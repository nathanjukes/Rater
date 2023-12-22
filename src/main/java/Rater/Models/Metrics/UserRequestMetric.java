package Rater.Models.Metrics;

import Rater.Models.API.API;

import java.util.Date;
import java.util.List;

public class UserRequestMetric {
    private String api;
    private Date time;
    private String status;

    public UserRequestMetric(String api, Date time, Boolean requestAccepted) {
        this.api = api;
        this.time = time;
        this.status = requestAccepted ? "accepted" : "denied";
    }

    public String getApi() {
        return api;
    }

    public Date getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public static UserRequestMetric fromData(List<Object> dataList, API api) {
        return new UserRequestMetric(api.getDisplayName(), (Date) dataList.get(1), (boolean) dataList.get(2));
    }}
