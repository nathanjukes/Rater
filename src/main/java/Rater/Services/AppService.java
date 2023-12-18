package Rater.Services;

import Rater.Models.App.App;
import Rater.Models.App.AppCreateRequest;
import Rater.Models.Org.Org;
import Rater.Repositories.AppRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AppService {
    private static final Logger log = LogManager.getLogger(AppService.class);

    private final AppRepository appRepository;
    private final ServiceService serviceService;

    @Autowired
    public AppService(AppRepository appRepository, ServiceService serviceService) {
        this.appRepository = appRepository;
        this.serviceService = serviceService;
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

    public void deleteApp(UUID appId, Org org) {
        Optional<App> app = getApp(appId);
        deleteApp(app.orElseThrow());
    }

    public void deleteApp(App app) {
        if (app.getServices() != null && !app.getServices().isEmpty()) {
            for (Rater.Models.Service.Service s : app.getServices()) {
                serviceService.deleteService(s);
            }
        }
        appRepository.delete(app);
    }
}
