package RaterTests.Controllers;

import Rater.Controllers.AppController;
import Rater.Controllers.AuthenticationController;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Auth.RefreshToken;
import Rater.Models.Auth.RefreshTokenRequest;
import Rater.Models.Auth.TokenResponse;
import Rater.Models.Org.Org;
import Rater.Models.User.User;
import Rater.Security.JwtUtil;
import Rater.Security.RefreshTokenService;
import Rater.Security.SecurityService;
import Rater.Services.AppService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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
    @Spy
    private JwtUtil jwtUtil;

    private Org testOrg;

    @Before
    public void setup() throws InternalServerException, UnauthorizedException {
        testOrg = new Org("TestOrg");
        testOrg.setId(UUID.randomUUID());
        when(securityService.getAuthedOrg()).thenReturn(Optional.of(testOrg));
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

}
