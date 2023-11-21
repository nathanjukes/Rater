package Rater.Repositories;

import Rater.Models.API.Rules.RoleRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRuleRepository extends JpaRepository<RoleRule, UUID> {
    Optional<RoleRule> findByRoleAndApiId(String role, UUID apiId);
}
