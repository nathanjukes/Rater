package Rater.Services;

import Rater.Models.App.App;
import Rater.Models.App.AppCreateRequest;
import Rater.Models.Org.Org;
import Rater.Repositories.AppRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static Rater.Util.FlatStructure.getFlatStructure;

@Service
@Transactional
public class AppService {
    private final AppRepository appRepository;
    private final OrgService orgService;

    @Autowired
    public AppService(AppRepository appRepository, OrgService orgService) {
        this.appRepository = appRepository;
        this.orgService = orgService;
    }

    public Optional<App> createApp(AppCreateRequest appCreateRequest, Org org) {
        App app = App.from(appCreateRequest, org);
        return Optional.ofNullable(appRepository.save(app));
    }

    public Optional<List<App>> getApps() {
        return Optional.of(appRepository.findAll());
    }

    public Optional<App> getApp(UUID id) {
        return appRepository.findById(id);
    }

    public Optional<App> getApp(Org org, String name) {
        final String flatStructure = getFlatStructure(List.of(org.getName(), name));
        return appRepository.findByFlatStructure(flatStructure);
    }

    public void deleteApp(UUID id) {
        appRepository.deleteById(id);
    }

    public void deleteApp(String name, Org org) {
        final String flatStructure = getFlatStructure(List.of(org.getName(), name));
        appRepository.deleteByFlatStructure(flatStructure);
    }
}
