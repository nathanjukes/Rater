package Rater.Services;

import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.API;
import Rater.Models.API.APICreateRequest;
import Rater.Models.API.APIStatus;
import Rater.Models.Org.Org;
import Rater.Repositories.APIRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class APIService {
    private final APIRepository apiRepository;
    private final ServiceService serviceService;

    @Autowired
    public APIService(APIRepository apiRepository, ServiceService serviceService) {
        this.apiRepository = apiRepository;
        this.serviceService = serviceService;
    }

    public Optional<API> getAPI(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return apiRepository.findById(id);
    }

    public Optional<List<API>> getAPIs() {
        return Optional.of(apiRepository.findAll());
    }

    public Optional<List<API>> getAPIs(UUID orgId, UUID appId, UUID serviceId) {
        if (appId != null && serviceId != null) {
            return apiRepository.findByOrgId(orgId, appId, serviceId);
        } else if (appId == null && serviceId != null) {
            return apiRepository.findAPIsByServiceId(orgId, serviceId);
        } else if (appId != null && serviceId == null) {
            return apiRepository.findAPIsByAppId(orgId, appId);
        }

        return apiRepository.findByOrgId(orgId);
    }

    public Optional<API> createAPI(APICreateRequest apiCreateRequest, Org org) throws UnauthorizedException {
        Optional<Rater.Models.Service.Service> service = serviceService.getService(apiCreateRequest.getServiceId());

        if (service.isEmpty() || !service.map(s -> s.getOrgId()).get().equals(org.getId())) {
            throw new UnauthorizedException();
        }

        API api = new API(apiCreateRequest.getName(), 10, service.orElseThrow(), org);
        return Optional.of(apiRepository.save(api));
    }

    public void deleteAPI(UUID id, Org org) {
        apiRepository.deleteByIdAndOrgId(id, org.getId());
    }

    public APIStatus getAPIStatus(API api) {
        return new APIStatus(api, 10);
    }
}
