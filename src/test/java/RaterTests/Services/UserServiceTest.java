package RaterTests.Services;

import Rater.Exceptions.BadRequestException;
import Rater.Models.Org.Org;
import Rater.Models.User.OrgUserCreateRequest;
import Rater.Models.User.User;
import Rater.Models.User.UserCreateRequest;
import Rater.Models.User.UserRole;
import Rater.Repositories.UserRepository;
import Rater.Services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testGetUser() {
        UUID userId = UUID.randomUUID();
        userService.getUser(userId);

        verify(userRepository, times(1)).findById(eq(userId));
    }

    @Test
    public void testGetByEmail() {
        String email = "testEmail";
        userService.getUserByEmail(email);

        verify(userRepository, times(1)).findByEmail(eq(email));
    }

    @Test
    public void testGetUsers() {
        UUID orgId = UUID.randomUUID();
        userService.getUsers(orgId);

        verify(userRepository, times(1)).findByOrgId(eq(orgId));
    }

    @Test
    public void testCreateUser() {
        String email = "testEmail";
        String password = "testPassword";
        Org org = new Org("testOrg");

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, org.getName());

        userService.createUser(userCreateRequest, org, passwordEncoder);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateOrgUser() throws BadRequestException {
        String email = "testEmail";
        String password = "testPassword";
        Org org = new Org("testOrg");

        OrgUserCreateRequest userCreateRequest = new OrgUserCreateRequest(email, password, UUID.randomUUID(), "user");

        userService.createUser(userCreateRequest, org, passwordEncoder);

        verify(userRepository, times(1)).save(any(User.class));
    }
}
