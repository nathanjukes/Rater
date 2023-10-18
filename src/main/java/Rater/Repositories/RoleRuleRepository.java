package Rater.Repositories;

import Rater.Models.API.RoleRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRuleRepository extends JpaRepository<RoleRule, UUID> {
}
