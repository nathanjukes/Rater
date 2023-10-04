package Rater.Controllers;

import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Models.App;
import Rater.Models.Org;
import Rater.Models.OrgCreateRequest;
import Rater.Models.User;
import Rater.Security.SecurityService;
import Rater.Services.AppService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
}
