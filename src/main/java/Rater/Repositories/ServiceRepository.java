package Rater.Repositories;

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
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    @Query(value = "SELECT * FROM services WHERE org_id = ?1", nativeQuery = true)
    Optional<List<Service>> findByOrgId(UUID orgId);
    @Query(value = "SELECT * FROM services WHERE org_id = ?1 AND app_id = ?2", nativeQuery = true)
    Optional<List<Service>> findByOrgIdAndAppId(UUID orgId, UUID appId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM services WHERE id = ?1 AND org_id = ?2", nativeQuery = true)
    void deleteByIdAndOrgId(UUID id, UUID orgId);
}
