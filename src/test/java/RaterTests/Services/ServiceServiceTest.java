package RaterTests.Services;

import Rater.Models.App.App;
import Rater.Models.Service.Service;
import Rater.Models.Service.ServiceCreateRequest;
import Rater.Models.Org.Org;
import Rater.Repositories.ServiceRepository;
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

@RunWith(MockitoJUnitRunner.class)
public class ServiceServiceTest {
    @InjectMocks
    private ServiceService serviceService;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private AppService appService;

    private Org testOrg;
    private App testApp;

    @Before
    public void setup() {
        testOrg = new Org("test");
        testOrg.setId(UUID.randomUUID());
        testApp = new App("testApp", testOrg);
        Service testService = new Service("testService", testApp, testOrg);
        when(serviceRepository.findByOrgIdAndAppId(any(), any())).thenReturn(Optional.of(List.of(testService)));
        when(serviceRepository.findByOrgId(any())).thenReturn(Optional.of(List.of(testService)));
        when(serviceRepository.findById(any())).thenReturn(Optional.of(testService));
        when(serviceRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))).thenReturn(Optional.empty());
    }

    @Test
    public void testGetServiceById() {
        Optional<Service> service = serviceService.getService(UUID.randomUUID());
        assertEquals("testService", service.map(Service::getName).orElse(null));
    }

    @Test
    public void testGetServiceByIdDoesNotExist() {
        Optional<Service> service = serviceService.getService(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertEquals(null, service.map(Service::getName).orElse(null));
    }

    @Test
    public void testGetServiceNullId() {
        Optional<Service> service = serviceService.getService(null);
        assertEquals(Optional.empty(), service);
    }

    @Test
    public void testCreateService() throws Exception {
        ServiceCreateRequest request = new ServiceCreateRequest("TestingService", UUID.randomUUID());
        Rater.Models.Service.Service service = new Rater.Models.Service.Service("TestingService", testApp, testOrg);

        when(serviceRepository.save(any())).thenReturn(service);
        when(appService.getApp(any())).thenReturn(Optional.ofNullable(testApp));

        Optional<Service> result = serviceService.createService(request, testOrg);

        assertTrue(result.isPresent());
        assertEquals("TestingService", result.get().getName());

        verify(serviceRepository, times(1)).save(any());
    }

    @Test
    public void testCreateServiceDatabaseIssue() {
        ServiceCreateRequest request = new ServiceCreateRequest("Testservice", UUID.randomUUID());
        Service service = new Service("Testservice", testApp, testOrg);

        try {
            serviceService.createService(request, testOrg);
            fail("Expected InternalServerException");
        } catch (Exception e) {
            // passed
        }

        verify(serviceRepository, times(0)).save(service);
    }

    @Test
    public void testCreateServiceAppDoesNotExist() {
        UUID appId = UUID.randomUUID();
        doReturn(Optional.empty()).when(appService).getApp(appId);
        ServiceCreateRequest request = new ServiceCreateRequest("Testservice", appId);
        Service service = new Service("Testservice", testApp, testOrg);

        try {
            serviceService.createService(request, testOrg);
            fail("Expected UnauthorizedException");
        } catch (Exception e) {
            // passed
        }

        verify(serviceRepository, times(0)).save(service);
    }

    @Test
    public void testCreateServiceAppWrongOrgId() {
        UUID appId = UUID.randomUUID();
        App testApp2 = testApp;
        testApp2.setOrg(new Org("Test"));
        doReturn(Optional.empty()).when(appService).getApp(appId);

        ServiceCreateRequest request = new ServiceCreateRequest("Testservice", appId);
        Service service = new Service("Testservice", testApp, testOrg);

        try {
            serviceService.createService(request, testOrg);
            fail("Expected UnauthorizedException");
        } catch (Exception e) {
            // passed
        }

        verify(serviceRepository, times(0)).save(service);
    }

    @Test
    public void testServiceDelete() {
        serviceService.deleteService(UUID.randomUUID(), testOrg);

        verify(serviceRepository, times(1)).deleteByIdAndOrgId(any(), any());
    }

    @Test
    public void testServiceGetAll() {
        List<Service> serviceList = List.of(new Service("testService", testApp, testOrg));

        List<Service> comparisonList = serviceService.getServices(UUID.randomUUID(), UUID.randomUUID()).get();

        for (int i = 0; i < serviceList.size(); i++) {
            assertEquals(serviceList.get(i).getName(), comparisonList.get(i).getName());
        }
    }

    @Test
    public void testServiceGetAllNoAppId() {
        List<Service> serviceList = List.of(new Service("testService", testApp, testOrg));

        List<Service> comparisonList = serviceService.getServices(UUID.randomUUID(), null).get();

        for (int i = 0; i < serviceList.size(); i++) {
            assertEquals(serviceList.get(i).getName(), comparisonList.get(i).getName());
        }
    }
}
