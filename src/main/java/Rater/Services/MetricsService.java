package Rater.Services;

import Rater.Models.Metrics.RequestMetric;
import Rater.Repositories.MetricsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MetricsService {
    private MetricsRepository metricsRepository;

    public MetricsService(MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }
}
