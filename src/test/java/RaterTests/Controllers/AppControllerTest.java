package RaterTests.Controllers;

import Rater.Controllers.AppController;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.App.App;
import Rater.Models.App.AppCreateRequest;
import Rater.Models.Org.Org;
import Rater.Security.SecurityService;
import Rater.Services.AppService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppControllerTest {
    @InjectMocks
    private AppController appController;
    @Mock
    private AppService appService;
    @Mock
    private SecurityService securityService;

    private Org testOrg;

    @Before
    public void setup() throws InternalServerException, UnauthorizedException {
        testOrg = new Org("TestOrg");
        testOrg.setId(UUID.randomUUID());
        when(securityService.getAuthedOrg()).thenReturn(Optional.of(testOrg));
    }

    @Test
    public void testCreateApp() throws InternalServerException, UnauthorizedException {
        AppCreateRequest appCreateRequest = new AppCreateRequest("testApp");

        appController.createApp(appCreateRequest);

        verify(appService).createApp(appCreateRequest, testOrg);
    }

    @Test
    public void testGetApp() {
        UUID id = UUID.randomUUID();
        appController.getApp(id);
        verify(appService).getApp(id);
    }

    @Test
    public void testGetAppNoId() {
        ResponseEntity<Optional<App>> response = appController.getApp(null);
        assert(response.getBody() == null);
        assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(404));
    }

    @Test
    public void testGetApps() throws InternalServerException, UnauthorizedException {
        appController.getApps();
        verify(appService).getApps(testOrg.getId());
    }

    @Test
    public void testDeleteApp() throws InternalServerException, UnauthorizedException {
        UUID id = UUID.randomUUID();

        App testApp = new App("testApp", testOrg);
        doReturn(Optional.of(testApp)).when(appService).getApp(id);

        appController.deleteApp(id);

        verify(appService).getApp(id);
        verify(appService).deleteApp(id, testOrg);
    }

    @Test
    public void testDeleteAppInvalidOrgId() throws InternalServerException, UnauthorizedException {
        UUID id = UUID.randomUUID();

        App testApp = new App("testApp", new Org("t"));
        doReturn(Optional.of(testApp)).when(appService).getApp(id);

        appController.deleteApp(id);

        verify(appService).getApp(id);
        verify(appService, times(0)).deleteApp(id, testOrg);
    }
}
