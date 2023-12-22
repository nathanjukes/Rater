package Rater.Controllers;

import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Metrics.ApiMetric;
import Rater.Models.Metrics.OrgMetric;
import Rater.Models.Metrics.UserUsageMetric;
import Rater.Models.Org.Org;
import Rater.Security.SecurityService;
import Rater.Services.MetricsService;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static Rater.Security.SecurityService.throwIfNoAuth;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/metrics")
public class MetricsController {
    private static final Logger log = LogManager.getLogger(MetricsController.class);

    private final MetricsService metricsService;
    private final SecurityService securityService;

    @Autowired
    public MetricsController(MetricsService metricsService, SecurityService securityService) {
        this.metricsService = metricsService;
        this.securityService = securityService;
    }

    @CrossOrigin
    @RequestMapping(value = "/apis/{apiId}", method = GET)
    public ResponseEntity<ApiMetric> getApiMetric(@PathVariable UUID apiId, @RequestParam @Nullable Instant startTime, @RequestParam @Nullable Instant endTime) throws InternalServerException, UnauthorizedException, BadRequestException {
        Optional<Org> org = securityService.getAuthedOrg();
        securityService.throwIfNoAuth(org, apiId);

        return ResponseEntity.ok(metricsService.getApiMetrics(apiId, org.map(Org::getId).orElseThrow(UnauthorizedException::new), startTime, endTime));
    }

    @CrossOrigin
    @RequestMapping(value = "/orgs", method = GET)
    public ResponseEntity<OrgMetric> getOrgMetrics(@RequestParam @Nullable Instant startTime, @RequestParam @Nullable Instant endTime) throws InternalServerException, UnauthorizedException, BadRequestException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        return ResponseEntity.ok(metricsService.getOrgMetrics(org.map(Org::getId).orElseThrow(UnauthorizedException::new), startTime, endTime));
    }

    @CrossOrigin
    @RequestMapping(value = "/users", method = GET)
    public ResponseEntity<List<UserUsageMetric>> getUserMetrics(@RequestParam @Nullable Instant startTime, @RequestParam @Nullable Instant endTime) throws InternalServerException, UnauthorizedException, BadRequestException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        return ResponseEntity.ok(metricsService.getUserUsageMetrics(org.map(Org::getId).orElseThrow(UnauthorizedException::new), startTime, endTime));
    }
}
