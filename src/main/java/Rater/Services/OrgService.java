package Rater.Services;

import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Models.Org;
import Rater.Models.OrgCreateRequest;
import Rater.Repositories.OrgRepository;
import com.sun.jdi.InternalException;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    public Optional<Org> createOrg(OrgCreateRequest orgCreateRequest) throws DataConflictException, InternalServerException {
        Org org = Org.from(orgCreateRequest);

        try {
            return Optional.ofNullable(orgRepository.save(org));
        } catch (DataIntegrityViolationException ex) {
            throw new DataConflictException("Org name in use");
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
        orgRepository.deleteById(orgId);
    }
}
