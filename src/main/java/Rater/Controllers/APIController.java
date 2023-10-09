package Rater.Controllers;

import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Models.API.API;
import Rater.Models.API.APICreateRequest;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import Rater.Models.Service.ServiceCreateRequest;
import Rater.Security.SecurityService;
import Rater.Services.APIService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/apis")
public class APIController {
    private final APIService apiService;
    private final SecurityService securityService;

    @Autowired
    public APIController(APIService apiService, SecurityService securityService) {
        this.apiService = apiService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "", method = POST)
    public ResponseEntity<Optional<API>> createAPI(@RequestBody @Valid APICreateRequest apiCreateRequest) throws DataConflictException, InternalServerException {
        Optional<Org> org = securityService.getAuthedOrg();
        // Needs auth to verify app belongs to org in auth
        return ResponseEntity.ok(apiService.createAPI(apiCreateRequest, org.orElseThrow()));
    }

    @RequestMapping(value = "/{apiId}", method = GET)
    public ResponseEntity<?> getAPI(@PathVariable UUID apiId) throws InternalServerException {
        Optional<Org> org = securityService.getAuthedOrg();
        // Auth here
        Optional<API> apiOpt = apiService.getAPI(apiId);
        return apiOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "", method = GET)
    public ResponseEntity<?> getAPIs(@RequestParam(required = false) UUID apiId) {
        if (apiId != null) {
            return ResponseEntity.ok(apiService.getAPI(apiId));
        }
        return ResponseEntity.ok(apiService.getAPIs());
    }

    @RequestMapping(value = "/{api}", method = DELETE)
    public ResponseEntity<?> deleteAPI(@PathVariable UUID api) throws InternalServerException {
        Optional<Org> org = securityService.getAuthedOrg();
        //serviceService.deleteService(app, org);

        return ResponseEntity.ok("Deleted: " + org.map(o -> o.getName()).orElseThrow() + "/" + api);
    }
}
