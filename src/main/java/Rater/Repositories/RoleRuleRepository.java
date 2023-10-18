package Rater.Repositories;

import Rater.Models.API.IdRule;
import Rater.Models.API.RoleRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRuleRepository extends JpaRepository<RoleRule, UUID> {
    Optional<RoleRule> findByRole(String role);
}
