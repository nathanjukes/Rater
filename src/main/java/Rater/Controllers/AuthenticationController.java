package Rater.Controllers;

import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.DataConflictException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Auth.RefreshToken;
import Rater.Models.Auth.RefreshTokenRequest;
import Rater.Models.Auth.TokenResponse;
import Rater.Models.Org.Org;
import Rater.Models.Org.OrgCreateRequest;
import Rater.Models.User.*;
import Rater.Security.JwtUtil;
import Rater.Security.RefreshTokenService;
import Rater.Security.SecurityService;
import Rater.Services.OrgService;
import Rater.Services.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static Rater.Security.SecurityService.throwIfNoAuth;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private static final Logger log = LogManager.getLogger(AuthenticationController.class);

    private final UserService userService;
    private final OrgService orgService;
    private final RefreshTokenService refreshTokenService;
    private final SecurityService securityService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(UserService userService, OrgService orgService, RefreshTokenService refreshTokenService, SecurityService securityService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.orgService = orgService;
        this.refreshTokenService = refreshTokenService;
        this.securityService = securityService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @CrossOrigin
    @RequestMapping(value = "/register", method = POST)
    public ResponseEntity<TokenResponse> userRegistrationAndOrgCreation(@RequestBody @Valid UserCreateRequest userCreateRequest) throws BadRequestException, InternalServerException, DataConflictException {
        Optional<Org> userOrg = orgService.createOrg(new OrgCreateRequest(userCreateRequest.getOrgName()));

        if (userOrg.isEmpty()) {
            throw new BadRequestException();
        }

        log.info("User Registration Request - OrgId: " + userOrg.map(Org::getId).get());

        try {
            Optional<User> user = userService.createUser(userCreateRequest, userOrg.orElseThrow(), passwordEncoder);

            TokenResponse jwt = jwtUtil.generateTokenResponse(user.map(User::getEmail).orElseThrow());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
            jwt.setRefreshToken(refreshToken.getToken().toString());

            return ResponseEntity.ok(jwt);
        } catch (DataIntegrityViolationException ex) {
            orgService.deleteOrg(userOrg.get().getId());
            throw new DataConflictException();
        } catch (Exception ex) {
            orgService.deleteOrg(userOrg.get().getId());
            throw new InternalServerException();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/registerOrgUser", method = POST)
    public ResponseEntity<Optional<User>> userRegistrationWithOrgExisting(@RequestBody @Valid OrgUserCreateRequest orgUserCreateRequest) throws BadRequestException, InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);
        if (!org.map(Org::getId).orElseThrow().equals(orgUserCreateRequest.getOrgId())) {
            throw new UnauthorizedException();
        }

        Optional<Org> userOrg = orgService.getOrg(orgUserCreateRequest.getOrgId());
        if (userOrg.isEmpty()) {
            throw new BadRequestException();
        }

        log.info("User Registration Request - OrgId: " + userOrg.map(Org::getId).get());

        if (!UserRole.user.equals(orgUserCreateRequest.getRole()) && !isAuthedToOperate(securityService.getAuthedUser())) {
            throw new UnauthorizedException();
        }

        try {
            Optional<User> user = userService.createUser(orgUserCreateRequest, userOrg.orElseThrow(), passwordEncoder);
            return ResponseEntity.ok(user);
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException();
        } catch (Exception ex) {
            throw new InternalServerException();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/login", method = POST)
    public ResponseEntity<TokenResponse> userLogin(@RequestBody @Valid UserLoginRequest userLoginRequest) throws InternalServerException, UnauthorizedException {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        TokenResponse jwt = jwtUtil.generateTokenResponse(auth);

        refreshTokenService.deleteRefreshToken();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken();
        jwt.setRefreshToken(refreshToken.getToken().toString());

        return ResponseEntity.ok(jwt);
    }

    @CrossOrigin
    @RequestMapping(value = "/refreshToken", method = POST)
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) throws UnauthorizedException {
        Optional<RefreshToken> refreshToken = refreshTokenService.getRefreshToken(refreshTokenRequest.getRefreshToken());
        if (!refreshTokenService.verifyToken(refreshToken)) {
            throw new UnauthorizedException();
        }

        TokenResponse jwt = jwtUtil.generateTokenResponse(refreshToken.map(RefreshToken::getUser).orElseThrow().getEmail());
        jwt.setRefreshToken(refreshTokenRequest.getRefreshToken().toString());

        return ResponseEntity.ok(jwt);
    }

    @CrossOrigin
    @RequestMapping(value = "/logout", method = POST)
    public ResponseEntity<?> logout() throws InternalServerException, UnauthorizedException {
        refreshTokenService.deleteRefreshToken();
        return ResponseEntity.ok().build();
    }

    private boolean isAuthedToOperate(Optional<User> auth) {
        return !auth.map(User::getRole).get().equals(UserRole.user);
    }
}
