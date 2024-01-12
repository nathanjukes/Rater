package Rater.Repositories;

import Rater.Models.Alerts.UserAlert;
import Rater.Models.Service.Service;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAlertsRepository extends JpaRepository<UserAlert, UUID> {
    @Query(value = "SELECT user_data FROM org_user_alerts WHERE org_id = ?1", nativeQuery = true)
    Optional<List<String>> findByOrgId(UUID orgId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_data WHERE org_id = ?1 AND user_data = ?2", nativeQuery = true)
    void deleteByUserData(UUID orgId, String userData);
}
