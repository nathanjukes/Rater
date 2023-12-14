package Rater.Services;

import Rater.Exceptions.BadRequestException;
import Rater.Models.Metrics.ApiMetric;
import Rater.Models.Metrics.OrgMetric;
import Rater.Repositories.MetricsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Transactional
public class MetricsService {
    private final int SECONDS_IN_DAY = 86400;
    private MetricsRepository metricsRepository;
    private AppService appService;

    public MetricsService(MetricsRepository metricsRepository, AppService appService) {
        this.metricsRepository = metricsRepository;
        this.appService = appService;
    }

    public ApiMetric getApiMetrics(UUID apiId, UUID orgId, Instant lowerBound, Instant upperBound) throws BadRequestException {
        lowerBound = lowerBound == null ? Instant.now().minusSeconds(SECONDS_IN_DAY) : lowerBound;
        upperBound = upperBound == null ? Instant.now() : upperBound;
        if (lowerBound.isAfter(upperBound) || upperBound.minusSeconds(SECONDS_IN_DAY + 1000).isAfter(lowerBound)) {
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
            throw new BadRequestException();
        }

        Date lb = Date.from(lowerBound);
        Date ub = Date.from(upperBound);

        return new OrgMetric(appService.getApps(orgId).orElse(Collections.emptyList()));
    }
}
