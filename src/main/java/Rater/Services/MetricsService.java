package Rater.Services;

import Rater.Exceptions.BadRequestException;
import Rater.Models.API.ApiMetric;
import Rater.Models.Metrics.RequestMetric;
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

    public MetricsService(MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }

    public ApiMetric getApiMetrics(UUID apiId, UUID orgId, Instant lowerBound, Instant upperBound) throws BadRequestException {
        lowerBound = lowerBound == null ? Instant.now().minusSeconds(SECONDS_IN_DAY) : lowerBound;
        upperBound = upperBound == null ? Instant.now() : upperBound;
        if (lowerBound.isAfter(upperBound) || upperBound.minusSeconds(SECONDS_IN_DAY).isAfter(lowerBound)) {
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
}
