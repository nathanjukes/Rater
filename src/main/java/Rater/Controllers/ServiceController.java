package Rater.Controllers;

import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Org.OrgCreateRequest;
import Rater.Models.Service.Service;
import Rater.Models.Service.ServiceCreateRequest;
import Rater.Models.User.User;
import Rater.Security.SecurityService;
import Rater.Services.ServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/services")
public class ServiceController {
    private final ServiceService serviceService;
    private final SecurityService securityService;

    @Autowired
    public ServiceController(ServiceService serviceService, SecurityService securityService) {
        this.serviceService = serviceService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "", method = POST)
    public ResponseEntity<Optional<Service>> createService(@RequestBody @Valid ServiceCreateRequest serviceCreateRequest) throws DataConflictException, InternalServerException {
        Optional<Org> org = securityService.getAuthedOrg();
        // Needs auth to verify app belongs to org in auth
        return ResponseEntity.ok(serviceService.createService(serviceCreateRequest, org.orElseThrow()));
    }

    @RequestMapping(value = "/{serviceId}", method = GET)
    public ResponseEntity<?> getService(@PathVariable UUID serviceId) throws InternalServerException {
        Optional<Org> org = securityService.getAuthedOrg();
        // Auth here
        Optional<Service> serviceOpt = serviceService.getService(serviceId);
        return serviceOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "", method = GET)
    public ResponseEntity<?> getServices(@RequestParam(required = false) UUID serviceId) {
        if (serviceId != null) {
            return ResponseEntity.ok(serviceService.getService(serviceId));
        }
        return ResponseEntity.ok(serviceService.getServices());
    }

    @RequestMapping(value = "/{service}", method = DELETE)
    public ResponseEntity<?> deleteService(@PathVariable UUID service) throws InternalServerException {
        Optional<Org> org = securityService.getAuthedOrg();
        //serviceService.deleteService(app, org);

        return ResponseEntity.ok("Deleted: " + org.map(o -> o.getName()).orElseThrow() + "/" + service);
    }
}
