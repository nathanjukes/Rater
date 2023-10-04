package Rater.Controllers;

import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Models.Org;
import Rater.Models.OrgCreateRequest;
import Rater.Models.User;
import Rater.Security.SecurityService;
import Rater.Security.UserDetailsServiceImpl;
import Rater.Services.OrgService;
import Rater.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController
@RequestMapping("/orgs")
public class OrgController {
    private final OrgService orgService;
    private final SecurityService securityService;

    @Autowired
    public OrgController(OrgService orgService, SecurityService securityService) {
        this.orgService = orgService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "", method = POST)
    public ResponseEntity<Optional<Org>> createOrg(@RequestBody @Valid OrgCreateRequest orgCreateRequest) throws DataConflictException, InternalServerException {
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

    @RequestMapping(value = "", method = DELETE)
    public ResponseEntity<?> deleteOrg() throws InternalServerException {
        // Get orgId/Name from JWT
        // orgService.deleteOrg(orgId);
        Optional<User> user = securityService.getAuthedUser();

        return ResponseEntity.ok("Delete: " + user.map(u -> u.getOrg().getName()).orElseThrow());
    }
}
