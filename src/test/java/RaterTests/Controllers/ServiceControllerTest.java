package RaterTests.Controllers;

import Rater.Controllers.ServiceController;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.App.App;
import Rater.Models.Service.Service;
import Rater.Models.Service.ServiceCreateRequest;
import Rater.Models.Org.Org;
import Rater.Security.SecurityService;
import Rater.Services.AppService;
import Rater.Services.ServiceService;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceControllerTest {
    @InjectMocks
    private ServiceController serviceController;
    @Mock
    private AppService appService;
    @Mock
    private ServiceService serviceService;
    @Mock
    private SecurityService securityService;

    private Org testOrg;
    private App testApp;

    @Before
    public void setup() throws InternalServerException, UnauthorizedException {
        testOrg = new Org("TestOrg");
        testOrg.setId(UUID.randomUUID());
        testApp = new App("testApp", testOrg);
        when(securityService.getAuthedOrg()).thenReturn(Optional.of(testOrg));
    }

    @Test
    public void testCreateService() throws InternalServerException, UnauthorizedException {
        ServiceCreateRequest serviceCreateRequest = new ServiceCreateRequest("testService", testApp.getId());

        serviceController.createService(serviceCreateRequest);

        verify(serviceService).createService(eq(serviceCreateRequest), any(), eq(testOrg));
    }

    @Test
    public void testGetService() {
        UUID id = UUID.randomUUID();
        serviceController.getService(id);
        verify(serviceService).getService(id);
    }

    @Test
    public void testGetServiceNoId() {
        ResponseEntity<Optional<Service>> response = serviceController.getService(null);
        assert(response.getBody() == null);
        assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(404));
    }

    @Test
    public void testGetServices() throws InternalServerException, UnauthorizedException {
        UUID id = UUID.randomUUID();
        serviceController.getServices(id);
        verify(serviceService).getServices(testOrg.getId(), id);
    }

    @Test
    public void testDeleteService() throws InternalServerException, UnauthorizedException {
        UUID id = UUID.randomUUID();

        Service testService = new Service("testService", testApp, testOrg);
        doReturn(Optional.of(testService)).when(serviceService).getService(id);

        serviceController.deleteService(id);

        verify(serviceService).getService(id);
        verify(serviceService).deleteService(id, testOrg);
    }

    @Test
    public void testDeleteServiceInvalidOrgId() throws InternalServerException, UnauthorizedException {
        UUID id = UUID.randomUUID();

        Service testService = new Service("testService", testApp, new Org("t"));
        doReturn(Optional.of(testService)).when(serviceService).getService(id);

        serviceController.deleteService(id);

        verify(serviceService).getService(id);
        verify(serviceService, times(0)).deleteService(id, testOrg);
    }
}
