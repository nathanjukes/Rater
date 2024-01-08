package Rater.Services;

import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Org.OrgCreateRequest;
import Rater.Models.Org.OrgUpdateRequest;
import Rater.Repositories.OrgRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrgService {
    private static final Logger log = LogManager.getLogger(OrgService.class);

    private OrgRepository orgRepository;
    private AppService appService;

    @Autowired
    public OrgService(OrgRepository orgRepository, AppService appService) {
        this.orgRepository = orgRepository;
        this.appService = appService;
    }

    public Optional<Org> createOrg(OrgCreateRequest orgCreateRequest) throws InternalServerException, DataConflictException {
        Org org = Org.from(orgCreateRequest);

        if (orgRepository.existsByName(org.getName())) {
            log.info("Org Create Denied - Duplicate Name: " + org.getName());
            throw new DataConflictException();
        }

        try {
            return Optional.of(orgRepository.save(org));
        } catch (Exception ex) {
            throw new InternalServerException();
        }
    }

    public Optional<Org> getOrg(UUID orgId) {
        return orgRepository.findById(orgId);
    }

    public Optional<Org> getOrg(String orgName) {
        return orgRepository.findByName(orgName);
    }

    public Optional<List<Org>> getOrgs() {
        return Optional.of(orgRepository.findAll());
    }

    public void deleteOrg(UUID orgId) {
        Optional<Org> org = getOrg(orgId);
        for (App a : org.map(Org::getApps).orElse(Collections.emptySet())) {
            appService.deleteApp(a);
        }
        orgRepository.deleteById(orgId);
    }

    public Optional<Org> updateOrg(UUID orgId, OrgUpdateRequest orgUpdateRequest) {
        Optional<Org> org = getOrg(orgId);
        if (org.isPresent()) {
            org.get().setHealthPageEnabled(orgUpdateRequest.getHealthCheckEnabled());
            return Optional.of(orgRepository.save(org.get()));
        }
        return Optional.empty();
    }
}
