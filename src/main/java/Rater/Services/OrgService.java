package Rater.Services;

import Rater.Models.Org;
import Rater.Repositories.OrgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrgService {
    private OrgRepository orgRepository;

    @Autowired
    public OrgService(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    public void createOrg(String name) {
        Org org = new Org(name);
        orgRepository.save(org);
    }

    public Optional<Org> getOrg(UUID orgId) {
        return orgRepository.findById(orgId);
    }

    public Optional<List<Org>> getOrgs() {
        return Optional.of(orgRepository.findAll());
    }

    public void deleteOrg(UUID orgId) {
        orgRepository.deleteById(orgId);
    }
}
