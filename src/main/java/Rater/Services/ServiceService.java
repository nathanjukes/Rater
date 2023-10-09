package Rater.Services;

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

    public Optional<Rater.Models.Service.Service> createService(ServiceCreateRequest serviceCreateRequest, Org org) {
        Optional<App> app = appService.getApp(serviceCreateRequest.getAppId());
        Rater.Models.Service.Service service = new Rater.Models.Service.Service(serviceCreateRequest.getName(), app.orElseThrow());
        return Optional.ofNullable(serviceRepository.save(service));
    }

    public Optional<List<Rater.Models.Service.Service>> getServices() {
        return Optional.of(serviceRepository.findAll());
    }

    public Optional<Rater.Models.Service.Service> getService(UUID id) {
        return serviceRepository.findById(id);
    }

    public void deleteService(UUID id) {
        serviceRepository.deleteById(id);
    }
}
