package Rater.Repositories;

import Rater.Models.API.IdRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IdRuleRepository extends JpaRepository<IdRule, UUID> {
}
