package Rater.Repositories;

import Rater.Models.API.API;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface APIRepository extends JpaRepository<API, UUID> {
    Optional<API> findByFlatStructure(String flatStructure);
}
