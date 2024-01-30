package RaterTests.Controllers;

import Rater.Controllers.AlertsController;
import Rater.Controllers.MetricsController;
import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Alerts.OrgAlertUpdateRequest;
import Rater.Models.Alerts.UserAlertCreateRequest;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Models.Service.Service;
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

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AlertsControllerTest {
    @InjectMocks
    private AlertsController alertsController;
    @Mock
    private AlertsService alertsService;
    @Mock
    private SecurityService securityService;
    private Org testOrg;
    private App testApp;

    @Before
    public void setup() throws InternalServerException, UnauthorizedException {
        testOrg = new Org("TestOrg");
        testOrg.setId(UUID.randomUUID());
        testApp = new App("testApp", testOrg);
        testApp.setId(UUID.randomUUID());
        when(securityService.getAuthedOrg()).thenReturn(Optional.of(testOrg));
    }

    @Test
    public void testGetAlerts() throws InternalServerException, UnauthorizedException {
        alertsController.getAlerts();

        verify(alertsService, times(1)).getAlerts(eq(testOrg.getId()));
    }

    @Test
    public void testDeleteAlert() throws InternalServerException, UnauthorizedException {
        UUID alertId = UUID.randomUUID();

        alertsController.deleteAlert(alertId);

        verify(alertsService, times(1)).deleteAlert(eq(testOrg.getId()), eq(alertId));
    }

    @Test
    public void testCreateUserAlert() throws InternalServerException, UnauthorizedException {
        UUID userData = UUID.randomUUID();
        UserAlertCreateRequest userAlertCreateRequest = new UserAlertCreateRequest(userData.toString());

        alertsController.createUserAlert(userAlertCreateRequest);

        verify(alertsService, times(1)).configureUserAlert(eq(testOrg), eq(userData.toString()));
    }

    @Test
    public void testDeleteUserAlert() throws InternalServerException, UnauthorizedException {
        UUID userData = UUID.randomUUID();

        alertsController.deleteUserAlert(userData.toString());

        verify(alertsService, times(1)).deleteUserAlert(eq(testOrg), eq(userData.toString()));
    }

    @Test
    public void testGetUserAlerts() throws InternalServerException, UnauthorizedException, BadRequestException {
        alertsController.getUserAlerts();

        verify(alertsService, times(1)).getUserAlerts(eq(testOrg.getId()));
    }

    @Test
    public void testGetOrgAlertSettings() throws InternalServerException, UnauthorizedException, BadRequestException {
        alertsController.getOrgAlertSettings();

        verify(alertsService, times(1)).getOrgAlertSettings(eq(testOrg));
    }

    @Test
    public void testSetOrgAlertSettings() throws InternalServerException, UnauthorizedException, BadRequestException {
        OrgAlertUpdateRequest orgAlertUpdateRequest = new OrgAlertUpdateRequest(10, 10, 10, 10);

        alertsController.setOrgAlertSettings(orgAlertUpdateRequest);

        verify(alertsService, times(1)).saveOrgAlertSettings(eq(testOrg), eq(orgAlertUpdateRequest));
    }
}
