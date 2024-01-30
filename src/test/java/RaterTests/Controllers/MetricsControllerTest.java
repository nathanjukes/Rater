package RaterTests.Controllers;

import Rater.Controllers.MetricsController;
import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import Rater.Security.SecurityService;
import Rater.Services.APIRuleService;
import Rater.Services.APIService;
import Rater.Services.MetricsService;
import Rater.Services.ServiceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MetricsControllerTest {
    @InjectMocks
    private MetricsController metricsController;
    @Mock
    private MetricsService metricsService;
    @Mock
    private ServiceService serviceService;
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
        testApp.setId(UUID.randomUUID());
        testService = new Service("testService", testApp, testOrg);
        testService.setId(UUID.randomUUID());
        testService.setApp(testApp);
        when(securityService.getAuthedOrg()).thenReturn(Optional.of(testOrg));
        when(serviceService.getService(any())).thenReturn(Optional.ofNullable(testService));
    }

    @Test
    public void testGetApiMetrics() throws InternalServerException, UnauthorizedException, BadRequestException {
        UUID apiId = UUID.randomUUID();

        metricsController.getApiMetric(apiId, null, null);

        verify(metricsService, times(1)).getApiMetrics(eq(apiId), eq(testOrg.getId()), any(), any());
    }

    @Test
    public void testGetOrgMetrics() throws InternalServerException, UnauthorizedException, BadRequestException {
        metricsController.getOrgMetrics(null, null);

        verify(metricsService, times(1)).getMetrics(eq(testOrg.getId()), any(), any(), any(), any(), any());
    }

    @Test
    public void testGetAppMetrics() throws InternalServerException, UnauthorizedException, BadRequestException {
        UUID appId = UUID.randomUUID();

        metricsController.getAppMetrics(appId,null, null);

        verify(metricsService, times(1)).getMetrics(eq(testOrg.getId()), eq(appId), any(), any(), any(), any());
    }

    @Test
    public void testGetServiceMetrics() throws InternalServerException, UnauthorizedException, BadRequestException {
        UUID serviceId = UUID.randomUUID();

        metricsController.getServiceMetrics(serviceId,null, null);

        verify(metricsService, times(1)).getMetrics(eq(testOrg.getId()), any(), eq(serviceId), any(), any(), any());
    }

    @Test
    public void testGetUserMetrics() throws InternalServerException, UnauthorizedException, BadRequestException {
        metricsController.getUserMetrics(null, null);

        verify(metricsService, times(1)).getUserUsageMetrics(eq(testOrg.getId()), any(), any());
    }

    @Test
    public void testGetUserRequestMetrics() throws InternalServerException, UnauthorizedException, BadRequestException {
        UUID userData = UUID.randomUUID();

        metricsController.getUserMetrics(userData.toString(), null, null);

        verify(metricsService, times(1)).getUserRequestMetrics(eq(userData.toString()), eq(testOrg.getId()), any(), any());
    }
}
