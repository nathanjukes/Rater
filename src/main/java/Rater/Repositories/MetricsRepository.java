package Rater.Repositories;

import Rater.Models.Metrics.RequestMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface MetricsRepository extends JpaRepository<RequestMetric, UUID> {
    @Query(value = "SELECT count(request_accepted) FROM metrics WHERE api_id = ?1 AND timestamp > ?2 AND timestamp < ?3 AND request_accepted = 'true'", nativeQuery = true)
    double getAcceptedCount(UUID apiId, Date lb, Date ub);
    @Query(value = "SELECT count(request_accepted) FROM metrics WHERE api_id = ?1 AND timestamp > ?2 AND timestamp < ?3 AND request_accepted = 'false'", nativeQuery = true)
    double getDeniedCount(UUID apiId, Date lb, Date ub);

    @Query(value = "SELECT user_data, COUNT(user_data) FROM metrics WHERE api_id = ?1 AND timestamp > ?2 AND timestamp < ?3 AND request_accepted = 'true' GROUP BY user_data ORDER BY COUNT(user_data) DESC LIMIT ?4", nativeQuery = true)
    List<Object[]> getTopUsersAccepted(UUID apiId, Date lb, Date ub, int limit);

    @Query(value = "SELECT user_data, COUNT(user_data) FROM metrics WHERE api_id = ?1 AND timestamp > ?2 AND timestamp < ?3 AND request_accepted = 'false' GROUP BY user_data ORDER BY COUNT(user_data) DESC LIMIT ?4", nativeQuery = true)
    List<Object[]> getTopUsersDenied(UUID apiId, Date lb, Date ub, int limit);

    @Query(value = "SELECT m.user_data, (SELECT api_id FROM metrics m2 WHERE m2.user_data = m.user_data ORDER BY timestamp DESC LIMIT 1) AS apiIdLastRequest, MIN(m.timestamp) AS firstRequestTime, MAX(m.timestamp) AS lastRequestTime, SUM(CASE WHEN m.request_accepted = true THEN 1 ELSE 0 END) AS requestsAccepted, SUM(CASE WHEN m.request_accepted = false THEN 1 ELSE 0 END) AS requestsDenied, COUNT(*) / COUNT(DISTINCT CAST(m.timestamp AS DATE)) AS avgRequestsPerDay FROM metrics m WHERE org_id = ?1 GROUP BY m.user_data", nativeQuery = true)
    List<Object[]> getUserUsageMetrics(UUID orgId);

    @Query(value = "SELECT m.user_data, (SELECT api_id FROM metrics m2 WHERE m2.user_data = m.user_data ORDER BY timestamp DESC LIMIT 1) AS apiIdLastRequest, MIN(m.timestamp) AS firstRequestTime, MAX(m.timestamp) AS lastRequestTime, SUM(CASE WHEN m.request_accepted = true THEN 1 ELSE 0 END) AS requestsAccepted, SUM(CASE WHEN m.request_accepted = false THEN 1 ELSE 0 END) AS requestsDenied, COUNT(*) / COUNT(DISTINCT CAST(m.timestamp AS DATE)) AS avgRequestsPerDay FROM metrics m WHERE org_id = ?1 AND m.user_data IN ?2 GROUP BY m.user_data", nativeQuery = true)
    List<Object[]> getTrackedUserMetrics(UUID orgId, List<String> ids);

    @Query(value = "SELECT api_id, timestamp, request_accepted FROM metrics WHERE user_data = ?1 AND org_id = ?2 ORDER BY timestamp DESC", nativeQuery = true)
    List<Object[]> getUserMetrics(String userData, UUID orgId);

    @Query(value = "SELECT count(*), COUNT(CASE WHEN request_accepted THEN 1 END) AS accepted_count, COUNT(CASE WHEN NOT(request_accepted) THEN 1 END) as denied_count FROM metrics WHERE org_id = ?1", nativeQuery = true)
    List<Object[]> getOrgMetrics(UUID orgId);

    @Query(value = "SELECT m.api_id, a.name, a.http_method, a.flat_structure, COUNT(m.api_id) AS acceptedRequests FROM metrics m JOIN apis a ON m.api_id = a.id WHERE m.org_id = ?1 AND m.request_accepted = 'true' GROUP BY m.api_id, a.name, a.http_method, a.flat_structure ORDER BY acceptedRequests DESC LIMIT 5", nativeQuery = true)
    List<Object[]> getOrgMostAcceptedAPIs(UUID orgId);

    @Query(value = "SELECT m.api_id, a.name, a.http_method, a.flat_structure, COUNT(m.api_id) AS deniedRequests FROM metrics m JOIN apis a ON m.api_id = a.id WHERE m.org_id = ?1 AND m.request_accepted = 'false' GROUP BY m.api_id, a.name, a.http_method, a.flat_structure ORDER BY deniedRequests DESC LIMIT 5", nativeQuery = true)
    List<Object[]> getOrgLeastAcceptedAPIs(UUID orgId);
}
