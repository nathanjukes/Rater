package Rater.Repositories;

import Rater.Models.App.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppRepository extends JpaRepository<App, UUID> {
    Optional<App> findByFlatStructure(String flatStructure);
    Optional<App> deleteByFlatStructure(String flatStructure);
}