package Rater.Controllers;

import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.App.App;
import Rater.Models.App.AppCreateRequest;
import Rater.Models.Org.Org;
import Rater.Models.User.User;
import Rater.Security.SecurityService;
import Rater.Services.AppService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static Rater.Security.SecurityService.throwIfNoAuth;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/apps")
public class AppController {
    private static final Logger log = LogManager.getLogger(AppController.class);

    private final AppService appService;
    private final SecurityService securityService;

    @Autowired
    public AppController(AppService appService, SecurityService securityService) {
        this.appService = appService;
        this.securityService = securityService;
    }

    @CrossOrigin
    @RequestMapping(value = "", method = POST)
    public ResponseEntity<Optional<App>> createApp(@RequestBody @Valid AppCreateRequest appCreateRequest) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        log.info("Create App Request: " + appCreateRequest.toString());

        return ResponseEntity.ok(appService.createApp(appCreateRequest, org.get()));
    }

    @CrossOrigin
    @PostAuthorize("@securityService.hasOrg(returnObject.body)")
    @RequestMapping(value = "/{appId}", method = GET)
    public ResponseEntity<Optional<App>> getApp(@PathVariable UUID appId) {
        Optional<App> appOpt = appService.getApp(appId);

        log.info("Get App Request: " + appId);

        if (appOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(appOpt);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = GET)
    public ResponseEntity<?> getApps() throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        return ResponseEntity.ok(appService.getApps(org.map(Org::getId).get()));
    }

    @CrossOrigin
    @RequestMapping(value = "/{appId}", method = DELETE)
    public ResponseEntity<?> deleteApp(@PathVariable UUID appId) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        log.info("Delete App Request: " + appId);

        // Validate org auth
        if (!appService.getApp(appId).map(App::getOrgId).equals(org.map(Org::getId))) {
           return ResponseEntity.notFound().build();
        }
        appService.deleteApp(appId, org.get());

        return ResponseEntity.ok("Deleted: " + org.map(o -> o.getName()).get() + "/" + appId);
    }
}
