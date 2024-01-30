package RaterTests.Janitor;

import Rater.Controllers.AlertsController;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Janitor.AlertJanitor;
import Rater.Models.App.App;
import Rater.Models.Org.Org;
import Rater.Repositories.AlertsRepository;
import Rater.Security.SecurityService;
import Rater.Services.AlertsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AlertJanitorTest {
    @InjectMocks
    private AlertJanitor alertJanitor;
    @Mock
    private AlertsService alertsService;
    @Mock
    private AlertsRepository alertsRepository;
    private List<Object[]> apiData;
    private List<Object[]> userData;


    @Before
    public void setup() throws InternalServerException, UnauthorizedException {
        apiData = Collections.emptyList();
        userData = Collections.emptyList();

        doReturn(apiData).when(alertsRepository).getApiAlertData(any(), any());
        doReturn(userData).when(alertsRepository).getUserAlertData(any(), any());
    }

    @Test
    public void testAlertMonitoring() {
        alertJanitor.alertMonitoring();

        verify(alertsService, times(1)).saveApiAlerts(eq(apiData), any(), any());
        verify(alertsService, times(1)).saveUserAlerts(eq(userData), any(), any());
    }
}
