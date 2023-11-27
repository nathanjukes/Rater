package Rater.Controllers;

import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Org.Org;
import Rater.Models.Org.OrgCreateRequest;
import Rater.Security.SecurityService;
import Rater.Services.OrgService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static Rater.Security.SecurityService.throwIfNoAuth;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/orgs")
public class OrgController {
    private static final Logger log = LogManager.getLogger(OrgController.class);

    private final OrgService orgService;
    private final SecurityService securityService;

    @Autowired
    public OrgController(OrgService orgService, SecurityService securityService) {
        this.orgService = orgService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "", method = POST)
    public ResponseEntity<Optional<Org>> createOrg(@RequestBody @Valid OrgCreateRequest orgCreateRequest) throws DataConflictException, InternalServerException, BadRequestException {
        log.info("Create Org Request: " + orgCreateRequest);

        return ResponseEntity.ok(orgService.createOrg(orgCreateRequest));
    }

    @PreAuthorize("@securityService.hasOrg(#org)")
    @RequestMapping(value = "/{org}", method = GET)
    public ResponseEntity<Optional<Org>> getOrg(@PathVariable String org) {
        return ResponseEntity.ok(orgService.getOrg(org));
    }

    @CrossOrigin
    @RequestMapping(value = "", method = GET)
    public ResponseEntity<?> getOrgs(@RequestParam(required = false) UUID orgId) {
        if (orgId != null) {
            return ResponseEntity.ok(orgService.getOrg(orgId));
        }
        return ResponseEntity.ok(orgService.getOrgs());
    }

    @CrossOrigin
    @RequestMapping(value = "/me", method = GET)
    public ResponseEntity<Optional<Org>> getOrgFromAuthToken() throws InternalServerException, UnauthorizedException {
        return ResponseEntity.ok(securityService.getAuthedOrg());
    }

    @RequestMapping(value = "", method = DELETE)
    public ResponseEntity<?> deleteOrg() throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        log.info("Delete Org Request: " + org.map(Org::getId).get());

        orgService.deleteOrg(org.map(Org::getId).orElseThrow(UnauthorizedException::new));

        return ResponseEntity.ok("Deleted: " + org.map(u -> u.getName()).orElseThrow(UnauthorizedException::new));
    }
}
