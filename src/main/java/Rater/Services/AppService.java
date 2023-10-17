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

    public Optional<List<App>> getApps(UUID orgId) {
        return appRepository.findByOrgId(orgId);
    }

    public Optional<App> getApp(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return appRepository.findById(id);
    }

    public void deleteApp(UUID id) {
        appRepository.deleteById(id);
    }

    public void deleteApp(UUID appId, Org org) {
        appRepository.deleteByIdAndOrgId(appId, org.getId());
    }
}
