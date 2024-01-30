package RaterTests.Controllers;

import Rater.Controllers.OrgController;
import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Org.Org;
import Rater.Models.Org.OrgUpdateRequest;
import Rater.Models.User.User;
import Rater.Models.User.UserRole;
import Rater.Repositories.OrgRepository;
import Rater.Security.SecurityService;
import Rater.Services.MetricsService;
import Rater.Services.OrgService;
import Rater.Services.UserService;
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
    private UserService userService;
    @Mock
    private MetricsService metricsService;
    @Mock
    private SecurityService securityService;
    private Org org;

    @Before
    public void setup() throws InternalServerException, UnauthorizedException {
        org = new Org("TestOrg");
        org.setId(UUID.randomUUID());
        org.setHealthPageEnabled(true);
        User user = new User();
        user.setRole(UserRole.owner);
        user.setId(UUID.randomUUID());
        when(securityService.getAuthedOrg()).thenReturn(Optional.of(org));
        when(securityService.getAuthedUser()).thenReturn(Optional.of(user));
        when(orgService.getOrg(eq(org.getName()))).thenReturn(Optional.of(org));
    }

    @Test
    public void testGetOrg() {
        orgController.getOrg("TestOrg");
        verify(orgService).getOrg("TestOrg");
    }

    @Test
    public void testGetOrgHealth() {
        orgController.getOrgHealth("TestOrg");
        verify(metricsService).getOrgHealth(eq(org));
    }

    @Test
    public void testDeleteOrg() throws InternalServerException, UnauthorizedException, BadRequestException {
        orgController.deleteOrg();
        verify(orgService).deleteOrg(any());
    }

    @Test
    public void testUpdateOrg() throws InternalServerException, UnauthorizedException, BadRequestException, DataConflictException {
        OrgUpdateRequest orgUpdateRequest = new OrgUpdateRequest(true);

        orgController.updateOrg(org.getId(), orgUpdateRequest);

        verify(orgService).updateOrg(eq(org.getId()), eq(orgUpdateRequest));
    }
}
