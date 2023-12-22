package Rater.Services;

import Rater.Controllers.MetricsController;
import Rater.Exceptions.BadRequestException;
import Rater.Models.API.API;
import Rater.Models.Metrics.ApiMetric;
import Rater.Models.Metrics.OrgMetric;
import Rater.Models.Metrics.UserUsageMetric;
import Rater.Models.User.User;
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
    private APIService apiService;

    public MetricsService(MetricsRepository metricsRepository, AppService appService, APIService apiService) {
        this.metricsRepository = metricsRepository;
        this.appService = appService;
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

        return new ApiMetric(acceptedRate, deniedRate, topUsersAccepted, topUsersDenied);
    }

    public OrgMetric getOrgMetrics(UUID orgId, Instant lowerBound, Instant upperBound) throws BadRequestException {
        lowerBound = lowerBound == null ? Instant.now().minusSeconds(SECONDS_IN_DAY) : lowerBound;
        upperBound = upperBound == null ? Instant.now() : upperBound;
        if (lowerBound.isAfter(upperBound) || upperBound.minusSeconds(SECONDS_IN_DAY + 1000).isAfter(lowerBound)) {
            log.info("Get Org Metrics Request Denied, bad time inputs: {} {}", lowerBound, upperBound);
            throw new BadRequestException();
        }

        Date lb = Date.from(lowerBound);
        Date ub = Date.from(upperBound);

        return new OrgMetric(appService.getApps(orgId).orElse(Collections.emptyList()));
    }

    public List<UserUsageMetric> getUserUsageMetrics(UUID orgId, Instant lowerBound, Instant upperBound) throws BadRequestException {
        lowerBound = lowerBound == null ? Instant.now().minusSeconds(SECONDS_IN_DAY) : lowerBound;
        upperBound = upperBound == null ? Instant.now() : upperBound;
        if (lowerBound.isAfter(upperBound) || upperBound.minusSeconds(SECONDS_IN_DAY + 1000).isAfter(lowerBound)) {
            log.info("Get Org Metrics Request Denied, bad time inputs: {} {}", lowerBound, upperBound);
            throw new BadRequestException();
        }

        Date lb = Date.from(lowerBound);
        Date ub = Date.from(upperBound);

        List<Object[]> userUsageMetrics = metricsRepository.getUserUsageMetrics(orgId);
        return collectUserUsageMetrics(userUsageMetrics, orgId);
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
}

