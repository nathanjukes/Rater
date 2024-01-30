package Rater.Models.Metrics;

import Rater.Models.API.API;
import Rater.Models.App.App;
import Rater.Models.Service.Service;

import java.util.*;
import java.util.stream.Collectors;

public class OrgMetric {
    private int appCount;

    private int serviceCount;

    private int apiCount;

    private int uniqueRules;

    private Long throughput;

    private Long acceptedRequests;

    private Long deniedRequests;


    private List<Object[]> highestAcceptedAPIs;

    private List<Object[]> lowestAcceptedAPIs;

    private List<Object[]> requestList;
    private List<Object[]> topUsers;

    public OrgMetric(List<App> appList, List<Object[]> highestAcceptedAPIs, List<Object[]> lowestAcceptedAPIs, List<Object[]> metadata, List<Object[]> requestData, List<Object[]> topUsers) {
        createOverviewMetrics(appList);
        this.highestAcceptedAPIs = highestAcceptedAPIs;
        this.lowestAcceptedAPIs = lowestAcceptedAPIs;
        this.requestList = requestData;
        this.topUsers = topUsers;

        if (metadata != null && metadata.size() > 0) {
            this.throughput = (Long) Arrays.stream(metadata.get(0)).toList().get(0);
            this.acceptedRequests = (Long) Arrays.stream(metadata.get(0)).toList().get(1);
            this.deniedRequests = (Long) Arrays.stream(metadata.get(0)).toList().get(2);
        }
    }

    private void createOverviewMetrics(List<App> appList) {
        this.appCount = appList.size();
        this.serviceCount = appList.stream().mapToInt(app -> app.getServices().size()).sum();

        this.apiCount = appList.stream()
                .flatMap(app -> app.getServices().stream())
                .mapToInt(service -> service.getApis().size())
                .sum();

        this.uniqueRules = appList.stream()
                .flatMap(app -> app.getServices().stream())
                .flatMap(service -> service.getApis().stream())
                .mapToInt(api -> api.getIdRules().size() + api.getIpRules().size() + api.getRoleRules().size())
                .sum();
    }

    public int getAppCount() {
        return appCount;
    }

    public int getServiceCount() {
        return serviceCount;
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
