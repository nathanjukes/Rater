package Rater.Controllers;

import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Alerts.OrgAlert;
import Rater.Models.Alerts.OrgAlertUpdateRequest;
import Rater.Models.Alerts.UserAlertCreateRequest;
import Rater.Models.Metrics.UserUsageMetric;
import Rater.Models.Org.Org;
import Rater.Security.SecurityService;
import Rater.Services.AlertsService;
import Rater.Services.OrgService;
import Rater.Services.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static Rater.Security.SecurityService.throwIfNoAuth;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/alerts")
public class AlertsController {
    private static final Logger log = LogManager.getLogger(AlertsController.class);

    private final SecurityService securityService;
    private final OrgService orgService;
    private final UserService userService;
    private final AlertsService alertsService;

    @Autowired
    public AlertsController(SecurityService securityService, OrgService orgService, UserService userService, AlertsService alertsService) {
        this.securityService = securityService;
        this.orgService = orgService;
        this.userService = userService;
        this.alertsService = alertsService;
    }

    @CrossOrigin
    @RequestMapping(value = "", method = GET)
    public ResponseEntity<Optional<List<OrgAlert>>> getAlerts() throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        return ResponseEntity.ok(alertsService.getAlerts(org.map(Org::getId).orElseThrow()));
    }

    @CrossOrigin
    @RequestMapping(value = "/{alertId}", method = DELETE)
    public ResponseEntity<?> deleteAlert(@PathVariable UUID alertId) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        // Try to delete, will throw an error if not completed
        alertsService.deleteAlert(org.map(Org::getId).orElseThrow(), alertId);

        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @RequestMapping(value = "/users", method = POST)
    public ResponseEntity<?> createUserAlert(@RequestBody @Valid UserAlertCreateRequest userAlertCreateRequest) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        alertsService.configureUserAlert(org.orElseThrow(), userAlertCreateRequest.getUserData());
        log.info("Saved user alert for OrgId: {} UserData: {}", org.map(Org::getId).orElseThrow(), userAlertCreateRequest.getUserData());

        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @RequestMapping(value = "/users/{userData}", method = DELETE)
    public ResponseEntity<?> deleteUserAlert(@PathVariable String userData) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        log.info("Delete user alert for OrgId: {} UserData: {}", org.map(Org::getId).orElseThrow(), userData);
        alertsService.deleteUserAlert(org.orElseThrow(), userData);

        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @RequestMapping(value = "/users", method = GET)
    public ResponseEntity<List<UserUsageMetric>> getUserAlerts() throws InternalServerException, UnauthorizedException, BadRequestException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        return ResponseEntity.ok(alertsService.getUserAlerts(org.map(Org::getId).orElseThrow()));
    }

    @CrossOrigin
    @RequestMapping(value = "/settings", method = GET)
    public ResponseEntity<Optional<?>> getOrgAlertSettings() throws InternalServerException, UnauthorizedException, BadRequestException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        return ResponseEntity.ok(alertsService.getOrgAlertSettings(org.orElseThrow()));
    }

    @CrossOrigin
    @RequestMapping(value = "/settings", method = POST)
    public ResponseEntity<List<UserUsageMetric>> setOrgAlertSettings(@RequestBody @Valid OrgAlertUpdateRequest orgAlertUpdateRequest) throws InternalServerException, UnauthorizedException, BadRequestException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        alertsService.saveOrgAlertSettings(org.orElseThrow(), orgAlertUpdateRequest);

        return ResponseEntity.ok().build();
    }
}
