package Rater.Services;

import Rater.Models.Org;
import Rater.Repositories.OrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppService {
    private AppRepository appRepository;

    @Autowired
    public AppService(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void createOrg(String name) {
        Org org = new Org(name);
        orgRepository.save(org);
    }

    public Optional<List<Org>> getOrgs() {
        return Optional.of(orgRepository.findAll());
    }
}
