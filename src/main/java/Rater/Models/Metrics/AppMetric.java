package Rater.Models.Metrics;

import Rater.Models.Service.Service;

import java.util.Arrays;
import java.util.List;

public class AppMetric {
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

    public AppMetric(List<Service> serviceList, List<Object[]> highestAcceptedAPIs, List<Object[]> lowestAcceptedAPIs, List<Object[]> metadata, List<Object[]> requestData, List<Object[]> topUsers) {
        createOverviewMetrics(serviceList);
        this.highestAcceptedAPIs = highestAcceptedAPIs;
        this.lowestAcceptedAPIs = lowestAcceptedAPIs;
        this.throughput = (Long) Arrays.stream(metadata.get(0)).toList().get(0);
        this.acceptedRequests = (Long) Arrays.stream(metadata.get(0)).toList().get(1);
        this.deniedRequests = (Long) Arrays.stream(metadata.get(0)).toList().get(2);
        this.requestList = requestData;
        this.topUsers = topUsers;
    }

    private void createOverviewMetrics(List<Service> serviceList) {
        this.serviceCount = serviceList.size();
        this.serviceCount = serviceList.stream().mapToInt(app -> app.getApis().size()).sum();

        this.apiCount = serviceList.stream()
                .mapToInt(service -> service.getApis().size())
                .sum();

        this.uniqueRules = serviceList.stream()
                .flatMap(service -> service.getApis().stream())
                .mapToInt(api -> api.getIdRules().size() + api.getIpRules().size() + api.getRoleRules().size())
                .sum();
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
