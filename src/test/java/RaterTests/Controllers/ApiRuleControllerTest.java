package RaterTests.Controllers;

import Rater.Controllers.APIRuleController;
import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.Rules.RuleCreateRequest;
import Rater.Models.API.Rules.RuleGetRequest;
import Rater.Models.API.Rules.RuleType;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import Rater.Security.SecurityService;
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
        when(securityService.getAuthedOrg()).thenReturn(Optional.of(testOrg));
    }

    @Test
    public void testCreateAPIRule() throws BadRequestException, UnauthorizedException, InternalServerException {
        RuleCreateRequest ruleCreateRequest = new RuleCreateRequest("test", null, null, 10, UUID.randomUUID());

        apiRuleController.createAPIRule(ruleCreateRequest);

        verify(apiService, times(1)).createAPIRule(eq(ruleCreateRequest), any());
    }

    @Test
    public void testCreateAPIRuleBadData() throws BadRequestException, UnauthorizedException, InternalServerException {
        try {
            RuleCreateRequest ruleCreateRequest = new RuleCreateRequest(null, null, null, 10, UUID.randomUUID());
            apiRuleController.createAPIRule(ruleCreateRequest);
            fail();
        } catch (Exception ex) {
            // passed
        }

        verify(apiService, times(0)).createAPIRule(any(), any());
    }

    @Test
    public void testGetAPIRule() throws BadRequestException, InternalServerException, UnauthorizedException {
        RuleGetRequest ruleGetRequest = new RuleGetRequest("data", RuleType.ip, UUID.randomUUID());
        apiRuleController.getApiRule(ruleGetRequest);

        verify(apiService, times(1)).getRule(eq(ruleGetRequest), eq(testOrg));
    }
}
