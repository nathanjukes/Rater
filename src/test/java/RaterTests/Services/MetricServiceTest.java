package RaterTests.Services;

import Rater.Controllers.MetricsController;
import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import Rater.Repositories.MetricsRepository;
import Rater.Security.SecurityService;
import Rater.Services.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MetricServiceTest {
    @InjectMocks
    private MetricsService metricsService;
    @Mock
    private AppService appService;
    @Mock
    private ServiceService serviceService;
    @Mock
    private SecurityService securityService;
    @Mock
    private MetricsRepository metricsRepository;
    private Org testOrg;

    @Before
    public void setup() throws InternalServerException, UnauthorizedException {
        testOrg = new Org("TestOrg");
        testOrg.setId(UUID.randomUUID());
    }

    @Test
    public void testGetApiMetrics() throws BadRequestException {
        UUID apiId = UUID.randomUUID();

        metricsService.getApiMetrics(apiId, testOrg.getId(), null, null);

        verify(metricsRepository, times(1)).getAcceptedCount(eq(apiId), any(), any());
        verify(metricsRepository, times(1)).getDeniedCount(eq(apiId), any(), any());
        verify(metricsRepository, times(1)).getTopUsersAccepted(eq(apiId), any(), any(), anyInt());
        verify(metricsRepository, times(1)).getTopUsersDenied(eq(apiId), any(), any(), anyInt());
        verify(metricsRepository, times(1)).getOrgRequestList(eq(testOrg.getId()), any(), any(), eq(apiId), any(), any());
    }

    @Test
    public void testGetMetrics() throws BadRequestException {
        metricsService.getMetrics(testOrg.getId(), null, null, null, null, null);

        verify(metricsRepository, times(1)).getOrgMostAcceptedAPIs(eq(testOrg.getId()), any(), any(), any(), any(), any());
        verify(metricsRepository, times(1)).getOrgLeastAcceptedAPIs(eq(testOrg.getId()), any(), any(), any(), any(), any());
        verify(metricsRepository, times(1)).getOrgMetrics(eq(testOrg.getId()), any(), any(), any(), any(), any());
        verify(metricsRepository, times(1)).getOrgRequestList(eq(testOrg.getId()), any(), any(), any(), any(), any());
        verify(metricsRepository, times(1)).getOrgTopUsers(eq(testOrg.getId()), any(), any(), any(), any(), any());
    }

    @Test
    public void testGetUserUsageMetrics() throws BadRequestException {
        metricsService.getUserUsageMetrics(testOrg.getId(), null, null);

        verify(metricsRepository, times(1)).getUserUsageMetrics(eq(testOrg.getId()));
    }
    @Test
    public void testGetTrackedUserUsageMetrics() throws BadRequestException {
        List<String> in = List.of(UUID.randomUUID().toString());

        metricsService.getTrackedUsersMetrics(testOrg.getId(), in);

        verify(metricsRepository, times(1)).getTrackedUserMetrics(eq(testOrg.getId()), eq(in));
    }

    @Test
    public void testGetUserRequestMetrics() throws BadRequestException {
        UUID userData = UUID.randomUUID();

        metricsService.getUserRequestMetrics(userData.toString(), testOrg.getId(), null, null);

        verify(metricsRepository, times(1)).getUserMetrics(eq(userData.toString()), eq(testOrg.getId()));
    }

    @Test
    public void testGetOrgHealth() {
        metricsService.getOrgHealth(testOrg);

        verify(metricsRepository, times(1)).getOrgHealthMetrics(eq(testOrg.getId()), any(), any());
    }
}
