package Rater.Repositories;

import Rater.Models.User.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE org_id = ?1 AND email LIKE '%@%'", nativeQuery = true)
    List<User> findByOrgId(UUID orgId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE email = ?1", nativeQuery = true)
    void deleteByEmail(String email);
}
