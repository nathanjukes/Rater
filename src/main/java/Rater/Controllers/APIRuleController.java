package Rater.Controllers;

import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.Rules.Rule;
import Rater.Models.API.Rules.RuleCreateRequest;
import Rater.Models.API.Rules.RuleGetRequest;
import Rater.Models.API.Rules.RuleSearchRequest;
import Rater.Models.Org.Org;
import Rater.Security.SecurityService;
import Rater.Services.APIService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static Rater.Security.SecurityService.throwIfNoAuth;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/apis/rules")
public class APIRuleController {
    private static final Logger log = LogManager.getLogger(APIRuleController.class);

    private final APIService apiService;
    private final SecurityService securityService;

    @Autowired
    public APIRuleController(APIService apiService, SecurityService securityService) {
        this.apiService = apiService;
        this.securityService = securityService;
    }

    @CrossOrigin
    @RequestMapping(value = "/create", method = POST)
    public ResponseEntity<Optional<Rule>> createAPIRule(@RequestBody @Valid RuleCreateRequest ruleCreateRequest) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        log.info("Create API Rule Request: " + ruleCreateRequest.toString());

        return ResponseEntity.ok(apiService.createAPIRule(ruleCreateRequest, org.orElseThrow()));
    }

    @RequestMapping(value = "", method = POST)
    public ResponseEntity<Optional<? extends Rule>> getApiRule(@RequestBody @Valid RuleGetRequest ruleGetRequest) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        log.info("Get Rule Request: " + ruleGetRequest.toString());

        return ResponseEntity.ok(apiService.getRule(ruleGetRequest, org.orElseThrow()));
    }

    @RequestMapping(value = "/search", method = POST)
    public ResponseEntity<Optional<? extends Rule>> searchForApiRule(@RequestBody @Valid RuleSearchRequest ruleSearchRequest) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        log.info("Search Rule Request: " + ruleSearchRequest.toString());

        return ResponseEntity.ok(apiService.searchRule(ruleSearchRequest, org.orElseThrow()));
    }
}
