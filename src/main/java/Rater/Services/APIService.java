package Rater.Services;

import Rater.Models.API;
import Rater.Repositories.APIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class APIService {
    private final APIRepository apiRepository;

    @Autowired
    public APIService(APIRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    public Optional<API> getById(UUID id) {
        return apiRepository.findById(id);
    }

    public Optional<API> getByName(String name) {
        return apiRepository.findByApi(name);
    }

    public Optional<List<API>> getAll() {
        return Optional.of(apiRepository.findAll());
    }

    public Optional<API> createTarget(String apiName) {
        API api = new API(apiName, UUID.randomUUID(), 10);
        return Optional.of(apiRepository.save(api));
    }
}
