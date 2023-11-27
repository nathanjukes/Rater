package Rater.Services;

import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.*;
import Rater.Models.Org.Org;
import Rater.Repositories.APIRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class APIService {
    private static final Logger log = LogManager.getLogger(APIService.class);

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
            log.info("API Create Denied, Invalid Service: " + apiCreateRequest.toString());
            throw new UnauthorizedException();
        }

        API api = new API(apiCreateRequest.getName(), apiCreateRequest.getBasicLimit(), service.orElseThrow(), apiCreateRequest.getHttpMethod(), org);
        return Optional.of(apiRepository.save(api));
    }

    public Optional<API> searchAPI(String apiName, String fullApi, HttpMethod httpMethod, UUID serviceId, Org org) {
        // api name + service id pair is unique so should be searchable
        Optional<API> api = apiRepository.searchApiByFullName(fullApi, httpMethod.toString(), serviceId, org.getId());

        if (api.isEmpty()) {
            return apiRepository.searchApiByName(apiName, httpMethod.toString(), serviceId, org.getId());
        }

        return api;
    }

    public void deleteAPI(UUID id, Org org) {
        apiRepository.deleteByIdAndOrgId(id, org.getId());
    }
}
