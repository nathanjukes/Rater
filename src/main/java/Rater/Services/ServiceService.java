package Rater.Services;

import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.API;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Service.ServiceCreateRequest;
import Rater.Models.User.ServiceAccountCreateRequest;
import Rater.Repositories.ServiceRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ServiceService {
    private static final Logger log = LogManager.getLogger(ServiceService.class);

    private final ServiceRepository serviceRepository;
    private final UserService userService;
    private final APIService apiService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, UserService userService, APIService apiService, PasswordEncoder passwordEncoder) {
        this.serviceRepository = serviceRepository;
        this.userService = userService;
        this.apiService = apiService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Rater.Models.Service.Service> createService(ServiceCreateRequest serviceCreateRequest, Optional<App> app, Org org) throws UnauthorizedException {
        if (app.isEmpty() || !app.map(App::getOrgId).get().equals(org.getId())) {
            log.info("Service Create Denied, Invalid App: " + serviceCreateRequest.toString());
            throw new UnauthorizedException();
        }

        Rater.Models.Service.Service service = new Rater.Models.Service.Service(serviceCreateRequest.getName(), app.orElseThrow(), org);

        return Optional.ofNullable(serviceRepository.save(service));
    }

    public void createServiceAccount(ServiceAccountCreateRequest serviceAccountCreateRequest, Org org) {
        userService.createUser(serviceAccountCreateRequest, org, passwordEncoder);
    }

    public Optional<List<Rater.Models.Service.Service>> getServices(UUID orgId, UUID appId) {
        if (appId == null) {
            return serviceRepository.findByOrgId(orgId);
        }
        return serviceRepository.findByOrgIdAndAppId(orgId, appId);
    }

    public Optional<Rater.Models.Service.Service> getService(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        return serviceRepository.findById(id);
    }

    public void deleteService(UUID id, Org org) {
        Optional<Rater.Models.Service.Service> service = getService(id);
        deleteService(service.orElseThrow());
        deleteServiceAccount(id, org);
    }

    public void deleteService(Rater.Models.Service.Service service) {
        if (service.getApis() != null && !service.getApis().isEmpty()) {
            for (API a : service.getApis()) {
                apiService.deleteAPI(a);
            }
        }
        serviceRepository.delete(service);
    }

    public void deleteServiceAccount(UUID id, Org org) {
        userService.deleteUser(id.toString(), org);
    }
}
