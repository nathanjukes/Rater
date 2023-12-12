package Rater.Repositories;

import Rater.Models.Metrics.RequestMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MetricsRepository extends JpaRepository<RequestMetric, UUID> {
}
