package RaterTests.Services;

import Rater.Models.Org;
import Rater.Services.OrgService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class OrgServiceTest {
    private OrgService orgService;

    @Before
    public void setup() {
        orgService = mock(OrgService.class);

        Org testOrg = new Org("test org");
        doReturn(Optional.of(testOrg)).when(orgService).getOrg("test org");
    }

    @Test
    public void testGetOrgByName() {
        Optional<Org> org = orgService.getOrg("test org");
        assertEquals("test org", org.map(Org::getName).orElse(null));
    }

    @Test
    public void testGetOrgByNameDoesNotExist() {
        Optional<Org> org = orgService.getOrg("test org test");
        assertEquals(null, org.map(Org::getName).orElse(null));
    }
}
