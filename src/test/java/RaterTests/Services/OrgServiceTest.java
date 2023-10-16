package RaterTests.Services;

import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Models.Org.Org;
import Rater.Models.Org.OrgCreateRequest;
import Rater.Repositories.OrgRepository;
import Rater.Services.OrgService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrgServiceTest {
    @InjectMocks
    private OrgService orgService;
    @Mock
    private OrgRepository orgRepository;

    @Before
    public void setup() {
        Org testOrg = new Org("testOrg");
        when(orgRepository.findByName("testOrg")).thenReturn(Optional.of(testOrg));
        when(orgRepository.findAll()).thenReturn(List.of(testOrg));
    }

    @Test
    public void testGetOrgByName() {
        Optional<Org> org = orgService.getOrg("testOrg");
        assertEquals("testOrg", org.map(Org::getName).orElse(null));
    }

    @Test
    public void testGetOrgByNameDoesNotExist() {
        Optional<Org> org = orgService.getOrg("test org test");
        assertEquals(null, org.map(Org::getName).orElse(null));
    }

    @Test
    public void testCreateOrg() throws Exception {
        OrgCreateRequest request = new OrgCreateRequest("TestOrg");
        Org org = Org.from(request);

        when(orgRepository.existsByName("TestOrg")).thenReturn(false);
        when(orgRepository.save(any())).thenReturn(org);

        Optional<Org> result = orgService.createOrg(request);

        assertTrue(result.isPresent());
        assertEquals("TestOrg", result.get().getName());

        verify(orgRepository, times(1)).existsByName("TestOrg");
        verify(orgRepository, times(1)).save(any());
    }

    @Test
    public void testCreateOrgDuplicateName() {
        OrgCreateRequest request = new OrgCreateRequest("ExistingOrg");

        when(orgRepository.existsByName("ExistingOrg")).thenReturn(true);

        try {
            orgService.createOrg(request);
            fail();
        } catch (Exception ex) {
            // passed
        }

        verify(orgRepository, times(1)).existsByName("ExistingOrg");
        verify(orgRepository, never()).save(any());
    }

    @Test
    public void testCreateOrgDatabaseIssue() {
        OrgCreateRequest request = new OrgCreateRequest("TestOrg");
        Org org = Org.from(request);

        when(orgRepository.existsByName("TestOrg")).thenReturn(false);
        when(orgRepository.save(any())).thenThrow(new RuntimeException("something bad"));

        try {
            orgService.createOrg(request);
            fail("Expected InternalServerException");
        } catch (Exception e) {
            // passed
        }

        verify(orgRepository, times(1)).existsByName("TestOrg");
        verify(orgRepository, times(0)).save(org);
    }

    @Test
    public void testOrgDelete() {
        orgService.deleteOrg(UUID.randomUUID());

        verify(orgRepository, times(1)).deleteById(any());
    }

    @Test
    public void testOrgGetAll() {
        List<Org> orgList = List.of(new Org("testOrg"));

        List<Org> comparisonList = orgService.getOrgs().get();

        for (int i = 0; i < orgList.size(); i++) {
            assertEquals(orgList.get(i).getName(), comparisonList.get(i).getName());
        }
    }
}
