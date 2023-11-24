package Rater.Controllers;

import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.*;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import Rater.Models.Service.ServiceCreateRequest;
import Rater.Security.SecurityService;
import Rater.Services.APIService;
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
@RequestMapping("/apis")
public class APIController {
    private static final Logger log = LogManager.getLogger(APIController.class);

    private final APIService apiService;
    private final SecurityService securityService;

    @Autowired
    public APIController(APIService apiService, SecurityService securityService) {
        this.apiService = apiService;
        this.securityService = securityService;
    }

    @CrossOrigin
    @RequestMapping(value = "", method = POST)
    public ResponseEntity<Optional<API>> createAPI(@RequestBody @Valid APICreateRequest apiCreateRequest) throws DataConflictException, InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        log.info("Create API Request: ", apiCreateRequest.toString());

        return ResponseEntity.ok(apiService.createAPI(apiCreateRequest, org.orElseThrow()));
    }

    @CrossOrigin
    @PostAuthorize("@securityService.hasOrg(returnObject.body)")
    @RequestMapping(value = "/{apiId}", method = GET)
    public ResponseEntity<Optional<API>> getAPI(@PathVariable UUID apiId) throws UnauthorizedException, InternalServerException {
        Optional<API> apiOpt = apiService.getAPI(apiId);

        log.info("Get API Request: ", apiId);

        if (apiOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(apiOpt);
    }

    @RequestMapping(value = "", method = GET)
    public ResponseEntity<?> getAPIs(@RequestParam(required = false) UUID appId,
                                     @RequestParam(required = false) UUID serviceId) throws UnauthorizedException, InternalServerException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        return ResponseEntity.ok(apiService.getAPIs(org.map(Org::getId).get(), appId, serviceId));
    }

    @RequestMapping(value = "/{apiId}", method = DELETE)
    public ResponseEntity<?> deleteAPI(@PathVariable UUID apiId) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        log.info("Delete API Request: " + apiId);

        if (!apiService.getAPI(apiId).map(API::getOrgId).equals(org.map(Org::getId))) {
            return ResponseEntity.notFound().build();
        }
        apiService.deleteAPI(apiId, org.get());
        return ResponseEntity.ok("Deleted: " + org.map(o -> o.getName()).orElseThrow() + "/" + apiId);
    }
}
