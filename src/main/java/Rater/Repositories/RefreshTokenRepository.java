package Rater.Repositories;

import Rater.Models.Auth.RefreshToken;
import Rater.Models.Org.Org;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Query(value = "SELECT * FROM refresh_tokens WHERE token = ?1", nativeQuery = true)
    Optional<RefreshToken> findByRefreshToken(UUID refreshToken);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM refresh_tokens WHERE user_id = ?1", nativeQuery = true)
    void deleteByUserId(UUID userId);
}
