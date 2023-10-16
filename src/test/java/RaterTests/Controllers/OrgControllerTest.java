package RaterTests.Controllers;

import Rater.Controllers.OrgController;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Org.Org;
import Rater.Repositories.OrgRepository;
import Rater.Security.SecurityService;
import Rater.Services.OrgService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrgControllerTest {
    @InjectMocks
    private OrgController orgController;
    @Mock
    private OrgService orgService;
    @Mock
    private SecurityService securityService;

    @Before
    public void setup() throws InternalServerException, UnauthorizedException {
        Org org = new Org("TestOrg");
        org.setId(UUID.randomUUID());
        when(securityService.getAuthedOrg()).thenReturn(Optional.of(org));
    }

    @Test
    public void testGetOrg() {
        orgController.getOrg("TestOrg");
        verify(orgService).getOrg("TestOrg");
    }

    @Test
    public void testDeleteOrg() throws InternalServerException, UnauthorizedException {
        orgController.deleteOrg();
        verify(orgService).deleteOrg(any());
    }
}
