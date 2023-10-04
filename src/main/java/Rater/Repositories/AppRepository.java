package Rater.Repositories;

import Rater.Models.API;
import Rater.Models.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppRepository extends JpaRepository<App, UUID> {
    Optional<App> findByFlatStructure(String flatStructure);
}