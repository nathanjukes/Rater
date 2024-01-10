package Rater.Repositories;

import Rater.Models.Alerts.UserAlert;
import Rater.Models.Service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAlertsRepository extends JpaRepository<UserAlert, UUID> {
    @Query(value = "SELECT user_id FROM org_user_alerts WHERE org_id = ?1", nativeQuery = true)
    Optional<List<String>> findByOrgId(UUID orgId);
}
