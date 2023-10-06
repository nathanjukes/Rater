package Rater.Services;

import Rater.Models.API.API;
import Rater.Models.API.APIStatus;
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
        return apiRepository.findById(id);
    }

    public Optional<API> getAPI(String flatStructure) {
        return apiRepository.findByFlatStructure(flatStructure);
    }

    public Optional<List<API>> getAPIs() {
        return Optional.of(apiRepository.findAll());
    }

    public Optional<API> createAPI(String apiName, UUID serviceId) {
        Optional<Rater.Models.Service.Service> service = serviceService.getService(serviceId);
        API api = new API(apiName, 10, service.orElseThrow());
        return Optional.of(apiRepository.save(api));
    }

    public void deleteAPI(UUID id) {
        apiRepository.deleteById(id);
    }

    public APIStatus getAPIStatus(API api) {
        return new APIStatus(api, 10);
    }
}
