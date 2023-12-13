package Rater.Models.API;

import java.util.*;
import java.util.stream.Collectors;

public class ApiMetric {
    private double acceptedCount;
    private double deniedCount;
    private double totalThroughput;
    private Map<UUID, Long> topUsersAccepted;
    private Map<UUID, Long> topUsersDenied;

    public ApiMetric(Double acceptedCount, Double deniedCount, List<Object[]> topUsersAccepted, List<Object[]> topUsersDenied) {
        this.acceptedCount = acceptedCount;
        this.deniedCount = deniedCount;
        this.totalThroughput = acceptedCount + deniedCount;
        this.topUsersAccepted = castToMap(topUsersAccepted);
        this.topUsersDenied = castToMap(topUsersDenied);
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

    public Map<UUID, Long> getTopUsersAccepted() {
        return topUsersAccepted;
    }

    public Map<UUID, Long> getTopUsersDenied() {
        return topUsersDenied;
    }

    private Map<UUID, Long> castToMap(List<Object[]> data) {
        return data.stream()
                .collect(Collectors.toMap(
                        arr -> UUID.fromString((String) arr[0]),
                        arr -> (Long) arr[1],
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }
}
