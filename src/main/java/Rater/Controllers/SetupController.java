package Rater.Controllers;

import Rater.Models.Org;
import Rater.Services.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/setup")
public class SetupController {
    private OrgService orgService;

    @Autowired
    public SetupController(OrgService orgService) {
        this.orgService = orgService;
    }

    @RequestMapping(value = "/{name}", method = POST)
    public ResponseEntity<String> setupOrg(@PathVariable String name) {
        orgService.createOrg(name);
        return ResponseEntity.ok(name);
    }

    @RequestMapping(value = "/{orgId}/{appName}", method = POST)
    public ResponseEntity<String> setupApp(@PathVariable UUID orgId,
                                           @PathVariable String appName) {
        orgService.createOrg(name);
        return ResponseEntity.ok(name);
    }

    @RequestMapping(value = "/org", method = GET)
    public ResponseEntity<Optional<List<Org>>> getOrgs() {
        return ResponseEntity.ok(orgService.getOrgs());
    }
}
