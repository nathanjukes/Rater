package RaterTests.Services;

import Rater.Models.App.App;
import Rater.Models.App.AppCreateRequest;
import Rater.Models.Org.Org;
import Rater.Models.Org.OrgCreateRequest;
import Rater.Repositories.AppRepository;
import Rater.Repositories.OrgRepository;
import Rater.Services.AppService;
import Rater.Services.OrgService;
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

@RunWith(MockitoJUnitRunner.class)
public class AppServiceTest {
    @InjectMocks
    private AppService appService;
    @Mock
    private AppRepository appRepository;

    private Org testOrg;

    @Before
    public void setup() {
        testOrg = new Org("test");
        App testApp = new App("testapp", testOrg);
        when(appRepository.findById(any())).thenReturn(Optional.of(testApp));
        when(appRepository.findByOrgId(any())).thenReturn(Optional.of(List.of(testApp)));
        when(appRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))).thenReturn(Optional.empty());
    }

    @Test
    public void testGetAppById() {
        Optional<App> app = appService.getApp(UUID.randomUUID());
        assertEquals("testapp", app.map(App::getName).orElse(null));
    }

    @Test
    public void testGetAppByIdDoesNotExist() {
        Optional<App> app = appService.getApp(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertEquals(null, app.map(App::getName).orElse(null));
    }

    @Test
    public void testCreateApp() throws Exception {
        AppCreateRequest request = new AppCreateRequest("Testingapp");
        App app = App.from(request, testOrg);

        when(appRepository.save(any())).thenReturn(app);

        Optional<App> result = appService.createApp(request, testOrg);

        assertTrue(result.isPresent());
        assertEquals("Testingapp", result.get().getName());

        verify(appRepository, times(1)).save(any());
    }

    @Test
    public void testCreateAppDatabaseIssue() {
        AppCreateRequest request = new AppCreateRequest("Testapp");
        App app = App.from(request, testOrg);

        when(appRepository.save(any())).thenThrow(new RuntimeException("something bad"));

        try {
            appService.createApp(request, testOrg);
            fail("Expected InternalServerException");
        } catch (Exception e) {
            // passed
        }

        verify(appRepository, times(0)).save(app);
    }

    @Test
    public void testAppDelete() {
        appService.deleteApp(UUID.randomUUID());

        verify(appRepository, times(1)).deleteById(any());
    }

    @Test
    public void testAppGetAll() {
        List<App> appList = List.of(new App("testapp", testOrg));

        List<App> comparisonList = appService.getApps(UUID.randomUUID()).get();

        for (int i = 0; i < appList.size(); i++) {
            assertEquals(appList.get(i).getName(), comparisonList.get(i).getName());
        }
    }
}
