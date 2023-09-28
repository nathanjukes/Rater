package Rater.Services;

import Rater.Models.App;
import Rater.Models.Org;
import Rater.Repositories.AppRepository;
import Rater.Repositories.OrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppService {
    private final AppRepository appRepository;
    private final OrgService orgService;

    @Autowired
    public AppService(AppRepository appRepository, OrgService orgService) {
        this.appRepository = appRepository;
        this.orgService = orgService;
    }

    public void createApp(String name, UUID orgId) {
        Optional<Org> org = orgService.getOrg(orgId);
        App app = new App(name, org.orElseThrow());
        appRepository.save(app);
    }

    public Optional<List<App>> getApps() {
        return Optional.of(appRepository.findAll());
    }

    public Optional<App> getApp(UUID id) {
        return appRepository.findById(id);
    }

    public void deleteApp(UUID id) {
        appRepository.deleteById(id);
    }
}
