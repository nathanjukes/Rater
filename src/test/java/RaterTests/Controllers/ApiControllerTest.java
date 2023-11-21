package RaterTests.Controllers;

import Rater.Controllers.APIController;
import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.API.*;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static Rater.Models.API.HttpMethod.GET;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApiControllerTest {
    @InjectMocks
    private APIController apiController;
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
    public void testCreateAPI() throws InternalServerException, UnauthorizedException, DataConflictException {
        APICreateRequest apiCreateRequest = new APICreateRequest("testAPI", testService.getId(), GET, 10);

        apiController.createAPI(apiCreateRequest);

        verify(apiService).createAPI(apiCreateRequest, testOrg);
    }

    @Test
    public void testGetAPI() throws InternalServerException, UnauthorizedException {
        UUID id = UUID.randomUUID();
        apiController.getAPI(id);
        verify(apiService).getAPI(id);
    }

    @Test
    public void testGetAPINoId() throws InternalServerException, UnauthorizedException {
        ResponseEntity<Optional<API>> response = apiController.getAPI(null);
        assert(response.getBody() == null);
        assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(404));
    }

    @Test
    public void testGetAPIs() throws InternalServerException, UnauthorizedException {
        apiController.getAPIs(null, null);
        verify(apiService).getAPIs(eq(testOrg.getId()), any(), any());
    }

    @Test
    public void testDeleteAPI() throws InternalServerException, UnauthorizedException {
        UUID id = UUID.randomUUID();

        API testAPI = new API("testAPI", 10, testService, GET, testOrg);
        doReturn(Optional.of(testAPI)).when(apiService).getAPI(id);

        apiController.deleteAPI(id);

        verify(apiService).getAPI(id);
        verify(apiService).deleteAPI(id, testOrg);
    }

    @Test
    public void testDeleteAPIInvalidOrgId() throws InternalServerException, UnauthorizedException {
        UUID id = UUID.randomUUID();

        API testAPI = new API("testAPI", 10, testService, GET, new Org("t"));
        doReturn(Optional.of(testAPI)).when(apiService).getAPI(id);

        apiController.deleteAPI(id);

        verify(apiService).getAPI(id);
        verify(apiService, times(0)).deleteAPI(id, testOrg);
    }
}
