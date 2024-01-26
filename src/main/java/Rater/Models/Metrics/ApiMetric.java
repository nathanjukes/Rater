package Rater.Models.Metrics;

import java.util.*;
import java.util.stream.Collectors;

public class ApiMetric {
    private double acceptedCount;
    private double deniedCount;
    private double totalThroughput;
    private double successRate;
    private Map<String, Long> topUsersAccepted;
    private Map<String, Long> topUsersDenied;
    private List<Object[]> requestList;

    public ApiMetric(Double acceptedCount, Double deniedCount, List<Object[]> topUsersAccepted, List<Object[]> topUsersDenied, List<Object[]> requestList) {
        this.acceptedCount = acceptedCount;
        this.deniedCount = deniedCount;
        this.totalThroughput = acceptedCount + deniedCount;
        this.successRate = acceptedCount / totalThroughput;
        this.topUsersAccepted = castToMap(topUsersAccepted);
        this.topUsersDenied = castToMap(topUsersDenied);
        this.requestList = requestList;
    }

    public double getAcceptedCount() {
        return acceptedCount;
    }

    public double getDeniedCount() {
        return deniedCount;
    }

    public double getTotalThroughput() {
        return totalThroughput;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public Map<String, Long> getTopUsersAccepted() {
        return topUsersAccepted;
    }

    public Map<String, Long> getTopUsersDenied() {
        return topUsersDenied;
    }

    public List<Object[]> getRequestList() {
        return requestList;
    }

    private Map<String, Long> castToMap(List<Object[]> data) {
        return data.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1],
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }
}
