package Rater.Models.Metrics;

import Rater.Models.API.API;
import Rater.Models.User.User;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserUsageMetric {
    private String data;
    private Date latestRequestTime;
    private int acceptedRequestCount;
    private int deniedRequestCount;
    private double avgRequests;
    private Date firstRequestTime;
    private String lastRequestedApi;

    public UserUsageMetric(String data, Date latestRequestTime, int acceptedRequestCount, int deniedRequestCount, double avgRequests, Date firstRequestTime, String lastRequestedApi) {
        this.data = data;
        this.latestRequestTime = latestRequestTime;
        this.acceptedRequestCount = acceptedRequestCount;
        this.deniedRequestCount = deniedRequestCount;
        this.avgRequests = avgRequests;
        this.firstRequestTime = firstRequestTime;
        this.lastRequestedApi = lastRequestedApi;
    }

    public String getData() {
        return data;
    }

    public Date getLatestRequestTime() {
        return latestRequestTime;
    }

    public int getAcceptedRequestCount() {
        return acceptedRequestCount;
    }

    public int getDeniedRequestCount() {
        return deniedRequestCount;
    }

    public double getAvgRequests() {
        return avgRequests;
    }

    public Date getFirstRequestTime() {
        return firstRequestTime;
    }

    public String getLastRequestedApi() {
        return lastRequestedApi;
    }

    public static UserUsageMetric fromData(List<Object> dataList, API api) {
        return new UserUsageMetric((String) dataList.get(0), (Date) dataList.get(3), (int)(long) dataList.get(4), (int)(long) dataList.get(5), (int)(long) dataList.get(6), (Date) dataList.get(2), api.getDisplayName());
    }
}
