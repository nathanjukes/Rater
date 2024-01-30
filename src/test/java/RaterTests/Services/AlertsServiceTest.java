package RaterTests.Services;

import Rater.Controllers.AlertsController;
import Rater.Controllers.MetricsController;
import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Alerts.OrgAlertUpdateRequest;
import Rater.Models.Alerts.UserAlert;
import Rater.Models.Alerts.UserAlertCreateRequest;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
import Rater.Repositories.AlertsRepository;
import Rater.Repositories.OrgAlertConfigRepository;
import Rater.Repositories.UserAlertsRepository;
import Rater.Security.SecurityService;
import Rater.Services.AlertsService;
import Rater.Services.MetricsService;
import Rater.Services.ServiceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AlertsServiceTest {
    @InjectMocks
    private AlertsService alertsService;
    @Mock
    private MetricsService metricsService;
    @Mock
    private SecurityService securityService;
    @Mock
    private AlertsRepository alertsRepository;
    @Mock
    private UserAlertsRepository userAlertsRepository;
    @Mock
    private OrgAlertConfigRepository orgAlertConfigRepository;
    private Org testOrg;

    @Before
    public void setup() throws InternalServerException, UnauthorizedException {
        testOrg = new Org("TestOrg");
        testOrg.setId(UUID.randomUUID());
    }

    @Test
    public void testGetAlerts() {
        alertsService.getAlerts(testOrg.getId());

        verify(alertsRepository, times(1)).getOrgAlerts(eq(testOrg.getId()));
    }

    @Test
    public void testDeleteAlert() {
        UUID alertId = UUID.randomUUID();

        alertsService.deleteAlert(testOrg.getId(), alertId);

        verify(alertsRepository, times(1)).deleteByIdAndOrgId(eq(alertId), eq(testOrg.getId()));
    }

    @Test
    public void testSaveApiAlerts() {
        List<Object[]> in = Arrays.asList(
                new Object[]{UUID.randomUUID(), "", 0L, 0L},
                new Object[]{UUID.randomUUID(), "", 0L, 0L},
                new Object[]{UUID.randomUUID(), "", 0L, 0L},
                new Object[]{UUID.randomUUID(), "", 0L, 0L},
                new Object[]{UUID.randomUUID(), "", 0L, 0L},
                new Object[]{UUID.randomUUID(), "", 0L, 0L}
        );

        alertsService.saveApiAlerts(in, null, null);

        verify(alertsRepository, times(in.size())).save(any());
    }

    @Test
    public void testSaveUserAlerts() {
        List<Object[]> in = Arrays.asList(
                new Object[]{UUID.randomUUID(), "", 0L, 0L},
                new Object[]{UUID.randomUUID(), "", 0L, 0L},
                new Object[]{UUID.randomUUID(), "", 0L, 0L},
                new Object[]{UUID.randomUUID(), "", 0L, 0L},
                new Object[]{UUID.randomUUID(), "", 0L, 0L},
                new Object[]{UUID.randomUUID(), "", 0L, 0L}
        );

        alertsService.saveUserAlerts(in, null, null);

        verify(alertsRepository, times(in.size())).save(any());
    }

    @Test
    public void testConfigureUserAlert() {
        UUID userData = UUID.randomUUID();

        alertsService.configureUserAlert(testOrg, userData.toString());

        verify(userAlertsRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteUserAlert() {
        UUID userData = UUID.randomUUID();

        alertsService.deleteUserAlert(testOrg, userData.toString());

        verify(userAlertsRepository, times(1)).deleteByUserData(eq(testOrg.getId()), eq(userData.toString()));
    }

    @Test
    public void testGetUserAlerts() throws BadRequestException {
        alertsService.getUserAlerts(testOrg.getId());

        verify(metricsService, times(1)).getTrackedUsersMetrics(eq(testOrg.getId()), any());
    }

    @Test
    public void testSetOrgAlertSettings() {
        OrgAlertUpdateRequest orgAlertUpdateRequest = new OrgAlertUpdateRequest(10, 10, 10, 10);

        alertsService.saveOrgAlertSettings(testOrg, orgAlertUpdateRequest);

        verify(orgAlertConfigRepository, times(1)).save(any());
    }
}
