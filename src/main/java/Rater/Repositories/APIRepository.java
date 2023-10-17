package Rater.Repositories;

import Rater.Models.API.API;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface APIRepository extends JpaRepository<API, UUID> {
    @Query(value = "SELECT * FROM apis WHERE org_id = ?1 AND app_id = ?2 AND service_id =?3", nativeQuery = true)
    Optional<List<API>> findByOrgId(UUID orgId, UUID appId, UUID serviceId);

    @Query(value = "SELECT * FROM apis WHERE org_id = ?1 AND app_id = ?2", nativeQuery = true)
    Optional<List<API>> findAPIsByAppId(UUID orgId, UUID appId);

    @Query(value = "SELECT * FROM apis WHERE org_id = ?1 AND service_id = ?2", nativeQuery = true)
    Optional<List<API>> findAPIsByServiceId(UUID orgId, UUID serviceId);

    @Query(value = "SELECT * FROM apis WHERE org_id = ?1", nativeQuery = true)
    Optional<List<API>> findByOrgId(UUID orgId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM apis WHERE id = ?1 AND org_id = ?2", nativeQuery = true)
    void deleteByIdAndOrgId(UUID id, UUID orgId);
}
