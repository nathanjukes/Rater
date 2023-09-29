package Rater.Controllers;

import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Models.Org;
import Rater.Models.OrgCreateRequest;
import Rater.Services.OrgService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @RequestMapping(value = "", method = POST)
    public ResponseEntity<Optional<Org>> createOrg(@RequestBody @Valid OrgCreateRequest orgCreateRequest) throws DataConflictException, InternalServerException {
        return ResponseEntity.ok(orgService.createOrg(orgCreateRequest));
    }

    @RequestMapping(value = "/{orgName}", method = GET)
    public ResponseEntity<Optional<Org>> getOrg(@PathVariable String orgName) {
        return ResponseEntity.ok(orgService.getOrg(orgName));
    }

    @RequestMapping(value = "", method = GET)
    public ResponseEntity<?> getOrgs(@RequestParam(required = false) UUID orgId) {
        if (orgId != null) {
            return ResponseEntity.ok(orgService.getOrg(orgId));
        }
        return ResponseEntity.ok(orgService.getOrgs());
    }

    @RequestMapping(value = "", method = DELETE)
    public ResponseEntity<?> deleteOrg() {
        // Get orgId/Name from JWT
        // orgService.deleteOrg(orgId);
        return ResponseEntity.ok("");
    }
}
