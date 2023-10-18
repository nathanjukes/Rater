package Rater.Repositories;

import Rater.Models.API.IpRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IpRuleRepository extends JpaRepository<IpRule, UUID> {
    Optional<IpRule> findByUserIp(String userIp);
}
