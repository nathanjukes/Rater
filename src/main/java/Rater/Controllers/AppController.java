package Rater.Controllers;

import Rater.Exceptions.InternalServerException;
import Rater.Models.App.App;
import Rater.Models.App.AppCreateRequest;
import Rater.Models.Org.Org;
import Rater.Models.User.User;
import Rater.Security.SecurityService;
import Rater.Services.AppService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/apps")
public class AppController {
    private final AppService appService;
    private final SecurityService securityService;

    @Autowired
    public AppController(AppService appService, SecurityService securityService) {
        this.appService = appService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "", method = POST)
    public ResponseEntity<Optional<App>> createApp(@RequestBody @Valid AppCreateRequest appCreateRequest) throws InternalServerException {
        Optional<User> user = securityService.getAuthedUser();
        Org org = user.map(u -> u.getOrg()).orElseThrow();

        return ResponseEntity.ok(appService.createApp(appCreateRequest, org));
    }

    @RequestMapping(value = "/{app}", method = GET)
    public ResponseEntity<?> getApp(@PathVariable String app) throws InternalServerException {
        Optional<User> user = securityService.getAuthedUser();
        Org org = user.map(u -> u.getOrg()).orElseThrow();

        Optional<App> appOpt = appService.getApp(org, app);
        return appOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "", method = GET)
    public ResponseEntity<?> getApps(@RequestParam(required = false) UUID appId) {
        if (appId != null) {
            return ResponseEntity.ok(appService.getApp(appId));
        }
        return ResponseEntity.ok(appService.getApps());
    }

    @RequestMapping(value = "/{app}", method = DELETE)
    public ResponseEntity<?> deleteApp(@PathVariable String app) throws InternalServerException {
        // Get orgId/Name from JWT
        // orgService.deleteOrg(orgId);
        Optional<User> user = securityService.getAuthedUser();
        Org org = user.map(u -> u.getOrg()).orElseThrow();
        appService.deleteApp(app, org);

        return ResponseEntity.ok("Deleted: " + user.map(u -> u.getOrg().getName()).orElseThrow() + "/" + app);
    }
}
