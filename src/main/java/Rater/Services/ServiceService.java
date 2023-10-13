package Rater.Services;

import Rater.Exceptions.UnauthorizedException;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Service.ServiceCreateRequest;
import Rater.Repositories.ServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final AppService appService;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, AppService appService) {
        this.serviceRepository = serviceRepository;
        this.appService = appService;
    }

    public Optional<Rater.Models.Service.Service> createService(ServiceCreateRequest serviceCreateRequest, Org org) throws UnauthorizedException {
        Optional<App> app = appService.getApp(serviceCreateRequest.getAppId());

        if (app.isEmpty() || !app.map(App::getOrgId).get().equals(org.getId())) {
            throw new UnauthorizedException();
        }

        Rater.Models.Service.Service service = new Rater.Models.Service.Service(serviceCreateRequest.getName(), app.orElseThrow(), org);

        return Optional.ofNullable(serviceRepository.save(service));
    }

    public Optional<List<Rater.Models.Service.Service>> getServices(UUID orgId, UUID appId) {
        if (appId == null) {
            return serviceRepository.findByOrgId(orgId);
        }
        return serviceRepository.findByOrgIdAndAppId(orgId, appId);
    }

    public Optional<Rater.Models.Service.Service> getService(UUID id) {
        return serviceRepository.findById(id);
    }

    public void deleteService(UUID id, Org org) {
        serviceRepository.deleteByIdAndOrgId(id, org.getId());
    }
}
