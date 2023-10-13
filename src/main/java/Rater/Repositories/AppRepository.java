package Rater.Repositories;

import Rater.Models.App.App;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppRepository extends JpaRepository<App, UUID> {
    @Query(value = "SELECT * FROM apps WHERE org_id = ?1", nativeQuery = true)
    Optional<List<App>> findByOrgId(UUID orgId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM apps WHERE id = ?1 AND org_id = ?2", nativeQuery = true)
    void deleteByIdAndOrgId(UUID id, UUID orgId);
}