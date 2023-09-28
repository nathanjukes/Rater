package Rater.Controllers;

import Rater.Models.API;
import Rater.Models.App;
import Rater.Models.Org;
import Rater.Models.Service;
import Rater.Services.APIService;
import Rater.Services.AppService;
import Rater.Services.OrgService;
import Rater.Services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/setup")
public class SetupController {
    private final OrgService orgService;
    private final AppService appService;
    private final ServiceService serviceService;
    private final APIService apiService;

    @Autowired
    public SetupController(OrgService orgService, AppService appService, ServiceService serviceService, APIService apiService) {
        this.orgService = orgService;
        this.appService = appService;
        this.serviceService = serviceService;
        this.apiService = apiService;
    }

    @RequestMapping(value = "/{name}", method = POST)
    public ResponseEntity<String> setupOrg(@PathVariable String name) {
        orgService.createOrg(name);
        return ResponseEntity.ok(name);
    }

    @RequestMapping(value = "/{orgId}/{appName}", method = POST)
    public ResponseEntity<String> setupApp(@PathVariable UUID orgId,
                                           @PathVariable String appName) {
        appService.createApp(appName, orgId);
        return ResponseEntity.ok(appName);
    }

    @RequestMapping(value = "/service/{appId}/{name}", method = POST)
    public ResponseEntity<String> setupService(@PathVariable UUID appId,
                                               @PathVariable String name) {
        serviceService.createService(name, appId);
        return ResponseEntity.ok(name);
    }

    @RequestMapping(value = "/api/{serviceId}/{name}", method = POST)
    public ResponseEntity<String> setupAPI(@PathVariable UUID serviceId,
                                               @PathVariable String name) {
        apiService.createAPI(name, serviceId);
        return ResponseEntity.ok(name);
    }

    @RequestMapping(value = "/org/{orgId}", method = DELETE)
    public ResponseEntity<String> deleteOrg(@PathVariable UUID orgId) {
        orgService.deleteOrg(orgId);
        return ResponseEntity.ok("orgDeleted");
    }

    @RequestMapping(value = "/app/{appId}", method = DELETE)
    public ResponseEntity<String> deleteApp(@PathVariable UUID appId) {
        appService.deleteApp(appId);
        return ResponseEntity.ok("appDeleted");
    }

    @RequestMapping(value = "/service/{serviceId}", method = DELETE)
    public ResponseEntity<String> deleteService(@PathVariable UUID serviceId) {
        serviceService.deleteService(serviceId);
        return ResponseEntity.ok("serviceDeleted");
    }

    @RequestMapping(value = "/api/{apiId}", method = DELETE)
    public ResponseEntity<String> deleteAPI(@PathVariable UUID apiId) {
        apiService.deleteAPI(apiId);
        return ResponseEntity.ok("serviceDeleted");
    }

    @RequestMapping(value = "/org", method = GET)
    public ResponseEntity<Optional<List<Org>>> getOrgs() {
        return ResponseEntity.ok(orgService.getOrgs());
    }

    @RequestMapping(value = "/app", method = GET)
    public ResponseEntity<Optional<List<App>>> getApps() {
        return ResponseEntity.ok(appService.getApps());
    }

    @RequestMapping(value = "/service", method = GET)
    public ResponseEntity<Optional<List<Service>>> getServices() {
        return ResponseEntity.ok(serviceService.getServices());
    }

    @RequestMapping(value = "/api", method = GET)
    public ResponseEntity<Optional<List<API>>> getAPIs() {
        return ResponseEntity.ok(apiService.getAPIs());
    }
}
