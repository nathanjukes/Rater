package RaterTests.Controllers;

import Rater.Controllers.APIRuleController;
import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.API;
import Rater.Models.API.Rules.RuleCreateRequest;
import Rater.Models.API.Rules.RuleGetRequest;
import Rater.Models.API.Rules.RuleSearchRequest;
import Rater.Models.API.Rules.RuleType;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import Rater.Security.SecurityService;
import Rater.Services.APIRuleService;
import Rater.Services.APIService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApiRuleControllerTest {
    @InjectMocks
    private APIRuleController apiRuleController;
    @Mock
    private APIRuleService apiRuleService;
    @Mock
    private APIService apiService;
    @Mock
    private SecurityService securityService;

    private Org testOrg;
    private App testApp;
    private Service testService;

    @Before
    public void setup() throws InternalServerException, UnauthorizedException {
        testOrg = new Org("TestOrg");
        testOrg.setId(UUID.randomUUID());
        testApp = new App("testApp", testOrg);
        testService = new Service("testService", testApp, testOrg);
        testService.setId(UUID.randomUUID());
        API testApi = new API();
        testApi.setId(UUID.randomUUID());
        when(securityService.getAuthedOrg()).thenReturn(Optional.of(testOrg));
        when(apiService.getAPI(any())).thenReturn(Optional.of(testApi));
        when(apiService.searchAPI(any(), any(), any(), any(), any())).thenReturn(Optional.of(testApi));
    }

    @Test
    public void testCreateAPIRule() throws BadRequestException, UnauthorizedException, InternalServerException {
        RuleCreateRequest ruleCreateRequest = new RuleCreateRequest("test", null, null, 10, UUID.randomUUID());

        apiRuleController.createAPIRule(ruleCreateRequest);

        verify(apiRuleService, times(1)).createAPIRule(eq(ruleCreateRequest), any(), any());
    }

    @Test
    public void testCreateAPIRuleBadData() throws UnauthorizedException {
        try {
            RuleCreateRequest ruleCreateRequest = new RuleCreateRequest(null, null, null, 10, UUID.randomUUID());
            apiRuleController.createAPIRule(ruleCreateRequest);
            fail();
        } catch (Exception ex) {
            // passed
        }

        verify(apiRuleService, times(0)).createAPIRule(any(), any(), any());
    }

    @Test
    public void testGetAPIRule() throws InternalServerException, UnauthorizedException {
        RuleGetRequest ruleGetRequest = new RuleGetRequest("data", RuleType.ip, UUID.randomUUID());
        apiRuleController.getApiRule(ruleGetRequest);

        verify(apiRuleService, times(1)).getRule(eq(ruleGetRequest), any(), eq(testOrg));
    }

    @Test
    public void testSearchAPIRule() throws InternalServerException, UnauthorizedException {
        RuleSearchRequest ruleSearchRequest = new RuleSearchRequest("data", RuleType.ip, UUID.randomUUID(), "GET:/users");
        apiRuleController.searchForApiRule(ruleSearchRequest);

        verify(apiRuleService, times(1)).searchRule(eq(ruleSearchRequest), any(), any(), eq(testOrg));
    }
}
