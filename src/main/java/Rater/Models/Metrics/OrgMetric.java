package Rater.Models.Metrics;

import Rater.Models.API.API;
import Rater.Models.App.App;
import Rater.Models.Service.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrgMetric {
    private int appCount;

    private int serviceCount;

    private int apiCount;

    private int uniqueRules;

    public OrgMetric(List<App> appList) {
        createOverviewMetrics(appList);
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
