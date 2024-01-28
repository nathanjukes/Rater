package Rater.Repositories;

import Rater.Models.Alerts.OrgAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlertsRepository extends JpaRepository<OrgAlert, UUID> {
    @Query(value = "SELECT * FROM alerts WHERE org_id = ?1", nativeQuery = true)
    Optional<List<OrgAlert>> getOrgAlerts(UUID orgId);

    @Query(value = "SELECT org_alert_config.org_id as org_id, m.api_id, SUM(CASE WHEN m.request_accepted = false THEN 1 ELSE 0 END) as denied, COUNT(*) as total FROM org_alert_config JOIN metrics m ON org_alert_config.org_id = m.org_id WHERE m.timestamp > ?1 AND m.timestamp < ?2 GROUP BY org_alert_config.org_id, org_alert_config.api_denial_threshold, org_alert_config.api_surge_threshold, m.api_id HAVING SUM(CASE WHEN m.request_accepted = false THEN 1 ELSE 0 END) >= org_alert_config.api_denial_threshold OR COUNT(*) >= org_alert_config.api_surge_threshold", nativeQuery = true)
    List<Object[]> getApiAlertData(Date lb, Date ub);

    @Query(value = "SELECT org_alert_config.org_id as org_id, m.user_data, SUM(CASE WHEN m.request_accepted = false THEN 1 ELSE 0 END) as denied, COUNT(*) as total FROM org_alert_config JOIN metrics m ON org_alert_config.org_id = m.org_id WHERE m.timestamp > ?1 AND m.timestamp < ?2 GROUP BY org_alert_config.org_id, org_alert_config.api_denial_threshold, org_alert_config.api_surge_threshold, m.user_data HAVING SUM(CASE WHEN m.request_accepted = false THEN 1 ELSE 0 END) >= org_alert_config.api_denial_threshold OR COUNT(*) >= org_alert_config.api_surge_threshold", nativeQuery = true)
    List<Object[]> getUserAlertData(Date lb, Date ub);
}
