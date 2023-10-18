package RaterTests.Services;

import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.API;
import Rater.Models.API.APICreateRequest;
import Rater.Models.API.IdRule;
import Rater.Models.API.RuleCreateRequest;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import Rater.Repositories.APIRepository;
import Rater.Repositories.IdRuleRepository;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApiServiceTest {
    @InjectMocks
    private APIService apiService;
    @Mock
    private APIRepository apiRepository;
    @Mock
    private IdRuleRepository idRuleRepository;
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
        API testAPI = new API("testApi", 10, testService, testOrg);

        when(apiRepository.findById(any())).thenReturn(Optional.of(testAPI));
        when(apiRepository.findByOrgId(any())).thenReturn(Optional.of(List.of(testAPI)));
        when(apiRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))).thenReturn(Optional.empty());
    }

    @Test
    public void testGetAPIById() {
        Optional<API> api = apiService.getAPI(UUID.randomUUID());
        assertEquals("testApi", api.map(API::getName).orElse(null));
    }

    @Test
    public void testGetAPIByIdDoesNotExist() {
        Optional<API> api = apiService.getAPI(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertEquals(null, api.map(API::getName).orElse(null));
    }

    @Test
    public void testGetAPINullId() {
        Optional<API> api = apiService.getAPI(null);
        assertEquals(Optional.empty(), api);
    }

    @Test
    public void testCreateAPI() throws UnauthorizedException {
        APICreateRequest request = new APICreateRequest("Testingapi", testService.getId());
        API api = new API(request.getName(), 10, testService, testOrg);

        when(apiRepository.save(any())).thenReturn(api);
        when(serviceService.getService(any())).thenReturn(Optional.ofNullable(testService));

        Optional<API> result = apiService.createAPI(request, testOrg);

        assertTrue(result.isPresent());
        assertEquals("Testingapi", result.get().getName());

        verify(apiRepository, times(1)).save(any());
    }

    @Test
    public void testCreateAPIDatabaseIssue() {
        APICreateRequest request = new APICreateRequest("Testapi", testService.getId());
        API api = new API(request.getName(), 10, testService, testOrg);

        when(apiRepository.save(any())).thenThrow(new RuntimeException("something bad"));

        try {
            apiService.createAPI(request, testOrg);
            fail("Expected InternalServerException");
        } catch (Exception e) {
            // passed
        }

        verify(apiRepository, times(0)).save(api);
    }

    @Test
    public void testAPIDelete() {
        apiService.deleteAPI(UUID.randomUUID(), testOrg);

        verify(apiRepository, times(1)).deleteByIdAndOrgId(any(), any());
    }

    @Test
    public void testAPIDeleteWithOrgId() {
        UUID apiId = UUID.randomUUID();
        apiService.deleteAPI(apiId, testOrg);

        verify(apiRepository, times(1)).deleteByIdAndOrgId(eq(apiId), eq(testOrg.getId()));
    }

    @Test
    public void testGetAllAPIs() {
        List<API> apiList = List.of(new API("testApi", 10, testService, testOrg));

        List<API> comparisonList = apiService.getAPIs(UUID.randomUUID(), null, null).get();

        for (int i = 0; i < apiList.size(); i++) {
            assertEquals(apiList.get(i).getName(), comparisonList.get(i).getName());
        }
    }

    @Test
    public void testCreateAPIRule() throws BadRequestException, UnauthorizedException {
        RuleCreateRequest ruleCreateRequest = new RuleCreateRequest("test", null, null, 10, UUID.randomUUID());
        API testApi = new API("test", 10, testService, testOrg);

        when(apiService.getAPI(ruleCreateRequest.getApiId())).thenReturn(Optional.of(testApi));
        when(idRuleRepository.save(any())).thenReturn(IdRule.from(ruleCreateRequest, testApi));

        apiService.createAPIRule(ruleCreateRequest, testOrg);

        verify(idRuleRepository, times(1)).save(any());
    }

    @Test
    public void testCreateAPIRuleBadAPI() throws BadRequestException {
        RuleCreateRequest ruleCreateRequest = new RuleCreateRequest("test", null, null, 10, UUID.randomUUID());

        when(apiService.getAPI(ruleCreateRequest.getApiId())).thenReturn(Optional.empty());

        try {
            apiService.createAPIRule(ruleCreateRequest, null);
            fail();
        } catch (Exception e) {
            // passed
        }
    }
}
