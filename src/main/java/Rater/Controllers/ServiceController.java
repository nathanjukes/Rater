package Rater.Controllers;

import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Org.OrgCreateRequest;
import Rater.Models.Service.Service;
import Rater.Models.Service.ServiceCreateRequest;
import Rater.Models.User.User;
import Rater.Security.SecurityService;
import Rater.Services.ServiceService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static Rater.Security.SecurityService.throwIfNoAuth;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/services")
public class ServiceController {
    private static final Logger log = LogManager.getLogger(ServiceController.class);

    private final ServiceService serviceService;
    private final SecurityService securityService;

    @Autowired
    public ServiceController(ServiceService serviceService, SecurityService securityService) {
        this.serviceService = serviceService;
        this.securityService = securityService;
    }

    @CrossOrigin
    @RequestMapping(value = "", method = POST)
    public ResponseEntity<Optional<Service>> createService(@RequestBody @Valid ServiceCreateRequest serviceCreateRequest) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        log.info("Service Create Request: " + serviceCreateRequest);

        return ResponseEntity.ok(serviceService.createService(serviceCreateRequest, org.orElseThrow()));
    }

    @CrossOrigin
    @PostAuthorize("@securityService.hasOrg(returnObject.body)")
    @RequestMapping(value = "/{serviceId}", method = GET)
    public ResponseEntity<Optional<Service>> getService(@PathVariable UUID serviceId) {
        Optional<Service> serviceOpt = serviceService.getService(serviceId);

        log.info("Get Service Request: " + serviceId);

        if (serviceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(serviceOpt);
    }

    @RequestMapping(value = "", method = GET)
    public ResponseEntity<?> getServices(@RequestParam(required = false) UUID appId) throws UnauthorizedException, InternalServerException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        return ResponseEntity.ok(serviceService.getServices(org.map(Org::getId).get(), appId));
    }

    @CrossOrigin
    @RequestMapping(value = "/{serviceId}", method = DELETE)
    public ResponseEntity<?> deleteService(@PathVariable UUID serviceId) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        log.info("Delete Service Request: " + serviceId);

        if (!serviceService.getService(serviceId).map(Service::getOrgId).equals(org.map(Org::getId))) {
            return ResponseEntity.notFound().build();
        }
        serviceService.deleteService(serviceId, org.get());

        return ResponseEntity.ok("Deleted: " + org.map(o -> o.getName()).orElseThrow() + "/" + serviceId);
    }
}
