package Rater.Repositories;

import Rater.Models.API;
import Rater.Models.Org;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrgRepository extends JpaRepository<Org, UUID> {
}
