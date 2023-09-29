package Rater.Controllers;

import Rater.Models.API;
import Rater.Models.APIStatus;
import Rater.Models.RateLimitResponse;
import Rater.Services.APIService;
import Rater.Util.FlatStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static Rater.Util.FlatStructure.getFlatStructure;

// Needs to  be able to:
// Check if an endpoint is over the rate limit
// Check if an endpoint is over the rate limit for a specified ROLE/Id
// Set rate limit on API id

@RestController
@RequestMapping("/rateLimit")
public class RateLimitController {
    private final APIService apiService;

    @Autowired
    public RateLimitController(APIService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping(value = "/{orgName}/{appName}/{serviceName}/{apiName}", method = GET)
    public ResponseEntity<RateLimitResponse> getRateLimitResponse(@PathVariable String orgName,
                                                                  @PathVariable String appName,
                                                                  @PathVariable String serviceName,
                                                                  @PathVariable String apiName) {
        // For example:
        // Lets say microsoft/appId/serviceId/api can handle 10 requests per second OVERALL
        // Return if this is exceeded

        // 1. Auth (To Come)

        // 2. Get API required, in the future orgName will be redundant as we will get that from the token
        String flatStructure = getFlatStructure(List.of(orgName, appName, serviceName, apiName));
        Optional<API> api = apiService.getAPI(flatStructure);
        APIStatus apiStatus = apiService.getAPIStatus(api.orElseThrow());

        // 3. Craft RateLimitResponse
        RateLimitResponse rateLimitResponse = RateLimitResponse.from(api.orElseThrow(), apiStatus);

        return ResponseEntity.ok(rateLimitResponse);
    }
}