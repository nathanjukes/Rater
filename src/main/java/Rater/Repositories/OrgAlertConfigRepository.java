package Rater.Repositories;

import Rater.Models.Alerts.OrgAlertConfig;
import Rater.Models.Alerts.UserAlert;
import Rater.Models.Service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrgAlertConfigRepository extends JpaRepository<OrgAlertConfig, UUID> {
    @Query(value = "SELECT * FROM org_alert_config WHERE org_id = ?1", nativeQuery = true)
    Optional<OrgAlertConfig> findByOrgId(UUID orgId);
}
