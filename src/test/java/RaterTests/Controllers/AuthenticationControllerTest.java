package RaterTests.Controllers;

import Rater.Controllers.AppController;
import Rater.Controllers.AuthenticationController;
import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Auth.RefreshToken;
import Rater.Models.Auth.RefreshTokenRequest;
import Rater.Models.Auth.TokenResponse;
import Rater.Models.Org.Org;
import Rater.Models.Org.OrgCreateRequest;
import Rater.Models.User.OrgUserCreateRequest;
import Rater.Models.User.User;
import Rater.Models.User.UserCreateRequest;
import Rater.Models.User.UserRole;
import Rater.Security.JwtUtil;
import Rater.Security.RefreshTokenService;
import Rater.Security.SecurityService;
import Rater.Services.AppService;
import Rater.Services.OrgService;
import Rater.Services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;
    @Mock
    private SecurityService securityService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private UserService userService;
    @Mock
    private OrgService orgService;
    @Mock
    private JwtUtil jwtUtil;

    private Org testOrg;

    @Before
    public void setup() throws InternalServerException, UnauthorizedException {
        testOrg = new Org("TestOrg");
        testOrg.setId(UUID.randomUUID());
        when(orgService.getOrg(eq(testOrg.getId()))).thenReturn(Optional.of(testOrg));
        when(securityService.getAuthedOrg()).thenReturn(Optional.of(testOrg));
    }

    @Test
    public void testUserAndOrgRegistration() throws InternalServerException, UnauthorizedException, BadRequestException, DataConflictException {
        UserCreateRequest request = new UserCreateRequest("test", "test", "testorg");
        RefreshToken refreshToken = spy(RefreshToken.class);

        when(orgService.createOrg(any())).thenReturn(Optional.of(testOrg));
        when(userService.createUser(any(UserCreateRequest.class), any(), any())).thenReturn(Optional.of(new User("test", "password", testOrg, UserRole.user)));
        when(jwtUtil.generateTokenResponse(anyString())).thenReturn(new TokenResponse("test", Date.from(Instant.now())));
        when(refreshTokenService.createRefreshToken(any())).thenReturn(refreshToken);
        when(refreshToken.getToken()).thenReturn(UUID.randomUUID());

        authenticationController.userRegistrationAndOrgCreation(request);

        verify(orgService, times(1)).createOrg(any());
        verify(userService, times(1)).createUser(eq(request), eq(testOrg), any());
    }

    @Test
    public void testUserRegistrationWithExistingOrg() throws InternalServerException, UnauthorizedException, BadRequestException {
        OrgUserCreateRequest request = new OrgUserCreateRequest("test", "test", testOrg.getId(), "user");

        authenticationController.userRegistrationWithOrgExisting(request);

        verify(userService, times(1)).createUser(eq(request), eq(testOrg), any());
    }

    @Test
    public void testUserRegistrationFailsWithIncorrectOrg() throws BadRequestException {
        OrgUserCreateRequest request = new OrgUserCreateRequest("test", "test", UUID.randomUUID(), "user");

        try {
           authenticationController.userRegistrationWithOrgExisting(request);
           fail();
        } catch (Exception ex) {
            // passed
        }

        verify(userService, times(0)).createUser(eq(request), eq(testOrg), any());
    }

    @Test
    public void testRefreshToken() throws UnauthorizedException {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(UUID.randomUUID());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(new User());
        refreshToken.getUser().setEmail("test");

        TokenResponse jwt = new TokenResponse("", new Date());

        doReturn(true).when(refreshTokenService).verifyToken(any());
        doReturn(Optional.of(refreshToken)).when(refreshTokenService).getRefreshToken(refreshTokenRequest.getRefreshToken());

        doReturn(jwt).when(jwtUtil).generateTokenResponse("test");

        ResponseEntity<?> response = authenticationController.refreshToken(refreshTokenRequest);

        verify(refreshTokenService).getRefreshToken(refreshTokenRequest.getRefreshToken());
        verify(refreshTokenService).verifyToken(any());
        verify(jwtUtil).generateTokenResponse(refreshToken.getUser().getEmail());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwt, response.getBody());
    }


    @Test
    public void testRefreshTokenBadToken() throws UnauthorizedException {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(UUID.randomUUID());

        doReturn(false).when(refreshTokenService).verifyToken(any());

        try {
            authenticationController.refreshToken(refreshTokenRequest);
            fail();
        } catch (Exception ex) {
            // passed
        }
    }

    @Test
    public void testLogout() throws InternalServerException, UnauthorizedException {
        authenticationController.logout();

        verify(refreshTokenService, times(1)).deleteRefreshToken();
    }

}
