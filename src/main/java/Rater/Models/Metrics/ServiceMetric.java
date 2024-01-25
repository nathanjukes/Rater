package Rater.Models.Metrics;

import Rater.Models.API.API;
import Rater.Models.Service.Service;

import java.util.Arrays;
import java.util.List;

public class ServiceMetric {
    private int apiCount;

    private int uniqueRules;

    private Long throughput;

    private Long acceptedRequests;

    private Long deniedRequests;


    private List<Object[]> highestAcceptedAPIs;

    private List<Object[]> lowestAcceptedAPIs;

    private List<Object[]> requestList;
    private List<Object[]> topUsers;

    public ServiceMetric(List<API> apiList, List<Object[]> highestAcceptedAPIs, List<Object[]> lowestAcceptedAPIs, List<Object[]> metadata, List<Object[]> requestData, List<Object[]> topUsers) {
        createOverviewMetrics(apiList);
        this.highestAcceptedAPIs = highestAcceptedAPIs;
        this.lowestAcceptedAPIs = lowestAcceptedAPIs;
        this.throughput = (Long) Arrays.stream(metadata.get(0)).toList().get(0);
        this.acceptedRequests = (Long) Arrays.stream(metadata.get(0)).toList().get(1);
        this.deniedRequests = (Long) Arrays.stream(metadata.get(0)).toList().get(2);
        this.requestList = requestData;
        this.topUsers = topUsers;
    }

    private void createOverviewMetrics(List<API> apiList) {
        this.apiCount = apiList.size();

        this.uniqueRules = apiList.stream()
                .mapToInt(api -> api.getIdRules().size() + api.getIpRules().size() + api.getRoleRules().size())
                .sum();
    }

    public int getApiCount() {
        return apiCount;
    }

    public int getUniqueRules() {
        return uniqueRules;
    }

    public Long getThroughput() {
        return throughput;
    }

    public Long getAcceptedRequests() {
        return acceptedRequests;
    }

    public Long getDeniedRequests() {
        return deniedRequests;
    }

    public List<Object[]> getHighestAcceptedAPIs() {
        return highestAcceptedAPIs;
    }

    public List<Object[]> getLowestAcceptedAPIs() {
        return lowestAcceptedAPIs;
    }

    public List<Object[]> getRequestList() {
        return requestList;
    }

    public List<Object[]> getTopUsers() {
        return topUsers;
    }
}
