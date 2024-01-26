package Rater.Services;

import Rater.Exceptions.BadRequestException;
import Rater.Models.API.API;
import Rater.Models.Metrics.*;
import Rater.Models.Org.Org;
import Rater.Models.Org.OrgHealth;
import Rater.Repositories.MetricsRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Transactional
public class MetricsService {
    private static final Logger log = LogManager.getLogger(MetricsService.class);

    private final int SECONDS_IN_DAY = 86400;
    private MetricsRepository metricsRepository;
    private AppService appService;
    private ServiceService serviceService;
    private APIService apiService;

    public MetricsService(MetricsRepository metricsRepository, AppService appService, ServiceService serviceService, APIService apiService) {
        this.metricsRepository = metricsRepository;
        this.appService = appService;
        this.serviceService = serviceService;
        this.apiService = apiService;
    }

    public ApiMetric getApiMetrics(UUID apiId, UUID orgId, Instant lowerBound, Instant upperBound) throws BadRequestException {
        lowerBound = lowerBound == null ? Instant.now().minusSeconds(SECONDS_IN_DAY) : lowerBound;
        upperBound = upperBound == null ? Instant.now() : upperBound;
        if (lowerBound.isAfter(upperBound) || upperBound.minusSeconds(SECONDS_IN_DAY + 1000).isAfter(lowerBound)) {
            log.info("Get API Metrics Request Denied, bad time inputs: {} {}", lowerBound, upperBound);
            throw new BadRequestException();
        }

        Date lb = Date.from(lowerBound);
        Date ub = Date.from(upperBound);

        Double acceptedRate = metricsRepository.getAcceptedCount(apiId, lb, ub);
        Double deniedRate = metricsRepository.getDeniedCount(apiId, lb, ub);
        List<Object[]> topUsersAccepted = metricsRepository.getTopUsersAccepted(apiId, lb, ub, 10);
        List<Object[]> topUsersDenied = metricsRepository.getTopUsersDenied(apiId, lb, ub, 10);
        List<Object[]> requestData = metricsRepository.getOrgRequestList(orgId, null, null, apiId, lb, ub);

        return new ApiMetric(acceptedRate, deniedRate, topUsersAccepted, topUsersDenied, requestData);
    }

    public Optional<?> getMetrics(UUID orgId, UUID appId, UUID serviceId, UUID apiId, Instant lowerBound, Instant upperBound) throws BadRequestException {
        lowerBound = lowerBound == null ? Instant.now().minusSeconds(SECONDS_IN_DAY) : lowerBound;
        upperBound = upperBound == null ? Instant.now() : upperBound;
        if (lowerBound.isAfter(upperBound) || upperBound.minusSeconds(SECONDS_IN_DAY + 1000).isAfter(lowerBound)) {
            log.info("Get Metrics Request Denied, bad time inputs: {} {}", lowerBound, upperBound);
            throw new BadRequestException();
        }

        Date lb = Date.from(lowerBound);
        Date ub = Date.from(upperBound);

        //List<Object[]> orgMetrics = metricsRepository.getOrgMetrics(orgId);
        List<Object[]> highestAcceptedAPIs = metricsRepository.getOrgMostAcceptedAPIs(orgId, appId, serviceId, apiId);
        List<Object[]> lowestAcceptedAPIs = metricsRepository.getOrgLeastAcceptedAPIs(orgId, appId, serviceId, apiId);
        List<Object[]> metadataMetrics = metricsRepository.getOrgMetrics(orgId, appId, serviceId, apiId);
        List<Object[]> requestData = metricsRepository.getOrgRequestList(orgId, appId, serviceId, apiId, lb, ub);
        List<Object[]> topUsers = metricsRepository.getOrgTopUsers(orgId, appId, serviceId, apiId, lb, ub);

        if (apiId != null) {
            return Optional.empty();
        } else if (serviceId != null) {
            return Optional.of(new ServiceMetric(
                    apiService.getAPIs(orgId, appId, serviceId).orElse(Collections.emptyList()),
                    highestAcceptedAPIs,
                    lowestAcceptedAPIs,
                    metadataMetrics,
                    requestData,
                    topUsers)
            );
        } else if (appId != null) {
            return Optional.of(new AppMetric(
                    serviceService.getServices(orgId, appId).orElse(Collections.emptyList()),
                    highestAcceptedAPIs,
                    lowestAcceptedAPIs,
                    metadataMetrics,
                    requestData,
                    topUsers)
            );        }
        return Optional.of(new OrgMetric(
                appService.getApps(orgId).orElse(Collections.emptyList()),
                highestAcceptedAPIs,
                lowestAcceptedAPIs,
                metadataMetrics,
                requestData,
                topUsers)
        );
    }

    public List<UserUsageMetric> getUserUsageMetrics(UUID orgId, Instant lowerBound, Instant upperBound) throws BadRequestException {
        lowerBound = lowerBound == null ? Instant.now().minusSeconds(SECONDS_IN_DAY) : lowerBound;
        upperBound = upperBound == null ? Instant.now() : upperBound;
        if (lowerBound.isAfter(upperBound) || upperBound.minusSeconds(SECONDS_IN_DAY + 1000).isAfter(lowerBound)) {
            log.info("Get user usage metrics request denied, bad time inputs: {} {}", lowerBound, upperBound);
            throw new BadRequestException();
        }

        Date lb = Date.from(lowerBound);
        Date ub = Date.from(upperBound);

        List<Object[]> userUsageMetrics = metricsRepository.getUserUsageMetrics(orgId);
        return collectUserUsageMetrics(userUsageMetrics, orgId);
    }

    public List<UserUsageMetric> getTrackedUsersMetrics(UUID orgId, List<String> trackedUsers) throws BadRequestException {
        List<Object[]> trackedUserMetrics = metricsRepository.getTrackedUserMetrics(orgId, trackedUsers);
        return collectUserUsageMetrics(trackedUserMetrics, orgId);
    }

    public List<UserRequestMetric> getUserRequestMetrics(String userData, UUID orgId, Instant lowerBound, Instant upperBound) throws BadRequestException {
        lowerBound = lowerBound == null ? Instant.now().minusSeconds(SECONDS_IN_DAY) : lowerBound;
        upperBound = upperBound == null ? Instant.now() : upperBound;
        if (lowerBound.isAfter(upperBound) || upperBound.minusSeconds(SECONDS_IN_DAY + 1000).isAfter(lowerBound)) {
            log.info("Get user request metric denied, bad time inputs: {} {}", lowerBound, upperBound);
            throw new BadRequestException();
        }

        Date lb = Date.from(lowerBound);
        Date ub = Date.from(upperBound);

        List<Object[]> userRequestMetrics = metricsRepository.getUserMetrics(userData, orgId);
        return collectUserRequestMetrics(userRequestMetrics, orgId);
    }

    public Optional<OrgHealth> getOrgHealth(Org org) {
        return Optional.ofNullable(OrgHealth.from(org));
    }

    private List<UserUsageMetric> collectUserUsageMetrics(List<Object[]> userData, UUID orgId) {
        // Object[0].0 = id
        // Object[0].1 = api.id
        // Object[0].2 = first request time
        // Object[0].3 = last request time
        // Object[0].4 = accepted req count
        // Object[0].5 = denied req count
        // Object[0].6 = avg req / day
        List<UserUsageMetric> metrics = new ArrayList<>();

        for (var i : userData) {
            List<Object> dataList = Arrays.stream(i).toList();
            UUID apiId = (UUID) dataList.get(1);
            Optional<API> api = apiService.getAPI(apiId, orgId);
            if (api.isPresent()) {
                metrics.add(UserUsageMetric.fromData(dataList, api.get()));
            }
        }

        return metrics;
    }

    private List<UserRequestMetric> collectUserRequestMetrics(List<Object[]> userData, UUID orgId) {
        List<UserRequestMetric> metrics = new ArrayList<>();

        for (var i : userData) {
            List<Object> dataList = Arrays.stream(i).toList();
            UUID apiId = (UUID) dataList.get(0);
            Optional<API> api = apiService.getAPI(apiId, orgId);
            if (api.isPresent()) {
                metrics.add(UserRequestMetric.fromData(dataList, api.get()));
            }
        }
        return metrics;
    }
}
