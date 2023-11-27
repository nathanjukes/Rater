package RaterTests.Services;

import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.API;
import Rater.Models.API.HttpMethod;
import Rater.Models.API.Rules.*;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import Rater.Repositories.APIRepository;
import Rater.Repositories.IdRuleRepository;
import Rater.Repositories.IpRuleRepository;
import Rater.Repositories.RoleRuleRepository;
import Rater.Services.APIRuleService;
import Rater.Services.APIService;
import Rater.Services.AppService;
import Rater.Services.ServiceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static Rater.Models.API.HttpMethod.GET;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApiRuleServiceTest {
    @InjectMocks
    private APIRuleService apiRuleService;
    @Mock
    private APIService apiService;
    @Mock
    private APIRepository apiRepository;
    @Mock
    private IdRuleRepository idRuleRepository;
    @Mock
    private IpRuleRepository ipRuleRepository;
    @Mock
    private RoleRuleRepository roleRuleRepository;
    @Mock
    private AppService appService;
    @Mock
    private ServiceService serviceService;

    private Org testOrg;
    private App testApp;
    private Service testService;
    @Before
    public void setup() {
        testOrg = new Org("test");
        testOrg.setId(UUID.randomUUID());
        testApp = new App("testApp", testOrg);
        testService = new Service("testService", testApp, testOrg);
        API testAPI = new API("testApi", 10, testService, GET, testOrg);

        when(apiRepository.findById(any())).thenReturn(Optional.of(testAPI));
        when(apiRepository.findByOrgId(any())).thenReturn(Optional.of(List.of(testAPI)));
        when(apiRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))).thenReturn(Optional.empty());
    }


    @Test
    public void testCreateAPIRule() throws BadRequestException, UnauthorizedException {
        RuleCreateRequest ruleCreateRequest = new RuleCreateRequest("test", null, null, 10, UUID.randomUUID());
        API testApi = new API("test", 10, testService, GET, testOrg);

        when(apiService.getAPI(ruleCreateRequest.getApiId())).thenReturn(Optional.of(testApi));
        when(idRuleRepository.save(any())).thenReturn(IdRule.from(ruleCreateRequest, testApi));

        apiRuleService.createAPIRule(ruleCreateRequest, testOrg);

        verify(idRuleRepository, times(1)).save(any());
    }

    @Test
    public void testCreateAPIRuleBadAPI() throws BadRequestException {
        RuleCreateRequest ruleCreateRequest = new RuleCreateRequest("test", null, null, 10, UUID.randomUUID());

        when(apiService.getAPI(ruleCreateRequest.getApiId())).thenReturn(Optional.empty());

        try {
            apiRuleService.createAPIRule(ruleCreateRequest, null);
            fail();
        } catch (Exception e) {
            // passed
        }
    }

    @Test
    public void testGetAPIRuleId() throws BadRequestException, InternalServerException, UnauthorizedException {
        RuleGetRequest ruleGetRequest = new RuleGetRequest("data", RuleType.id, UUID.randomUUID());
        API testApi = new API("test", 10, testService, GET, testOrg);
        testApi.setId(UUID.randomUUID());

        when(apiService.getAPI(ruleGetRequest.getApiId())).thenReturn(Optional.of(testApi));

        apiRuleService.getRule(ruleGetRequest, testOrg);

        verify(idRuleRepository, times(1)).findByUserIdAndApiId(any(), any());
    }

    @Test
    public void testGetAPIRuleIp() throws BadRequestException, InternalServerException, UnauthorizedException {
        RuleGetRequest ruleGetRequest = new RuleGetRequest("data", RuleType.ip, UUID.randomUUID());
        API testApi = new API("test", 10, testService, GET, testOrg);
        testApi.setId(UUID.randomUUID());

        when(apiService.getAPI(ruleGetRequest.getApiId())).thenReturn(Optional.of(testApi));

        apiRuleService.getRule(ruleGetRequest, testOrg);

        verify(ipRuleRepository, times(1)).findByUserIpAndApiId(any(), any());
    }

    @Test
    public void testGetAPIRuleRole() throws BadRequestException, InternalServerException, UnauthorizedException {
        RuleGetRequest ruleGetRequest = new RuleGetRequest("data", RuleType.role, UUID.randomUUID());
        API testApi = new API("test", 10, testService, GET, testOrg);
        testApi.setId(UUID.randomUUID());

        when(apiService.getAPI(ruleGetRequest.getApiId())).thenReturn(Optional.of(testApi));

        apiRuleService.getRule(ruleGetRequest, testOrg);

        verify(roleRuleRepository, times(1)).findByRoleAndApiId(any(), any());
    }

    @Test
    public void testSearchAPIRule() throws UnauthorizedException {
        RuleSearchRequest ruleSearchRequest = new RuleSearchRequest("data", RuleType.ip, UUID.randomUUID(), "GET:/users");
        API testApi = new API("test", 10, testService, GET, testOrg);
        testApi.setId(UUID.randomUUID());

        when(apiService.searchAPI(eq("users"), eq("users"), eq(HttpMethod.GET), eq(ruleSearchRequest.getServiceId()), eq(testOrg))).thenReturn(Optional.of(testApi));
        apiRuleService.searchRule(ruleSearchRequest, testOrg);

        verify(ipRuleRepository, times(1)).findByUserIpAndApiId(any(), any());
    }
}
