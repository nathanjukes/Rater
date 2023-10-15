package Rater.Controllers;

import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Auth.RefreshToken;
import Rater.Models.Auth.RefreshTokenRequest;
import Rater.Models.Auth.TokenResponse;
import Rater.Models.Org.Org;
import Rater.Models.User.User;
import Rater.Models.User.UserCreateRequest;
import Rater.Models.User.UserLoginRequest;
import Rater.Security.JwtUtil;
import Rater.Security.RefreshTokenService;
import Rater.Services.OrgService;
import Rater.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final UserService userService;
    private final OrgService orgService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationController(UserService userService, OrgService orgService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.orgService = orgService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @RequestMapping(value = "/register", method = POST)
    public ResponseEntity<Optional<User>> userRegistration(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        // 1. Does Org Exist already?
        // 2. Does user exist in org?

        Optional<Org> userOrg = orgService.getOrg(userCreateRequest.getOrgName());

        Optional<User> user = userService.createUser(userCreateRequest, userOrg.orElseThrow(), passwordEncoder);

        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/login", method = POST)
    public ResponseEntity<TokenResponse> userRegistration(@RequestBody @Valid UserLoginRequest userLoginRequest) throws InternalServerException, UnauthorizedException {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        TokenResponse jwt = jwtUtil.generateTokenResponse(auth);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken();
        jwt.setRefreshToken(refreshToken.getToken().toString());

        return ResponseEntity.ok(jwt);
    }

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

    @RequestMapping(value = "/logout", method = POST)
    public ResponseEntity<?> logout() throws InternalServerException, UnauthorizedException {
        refreshTokenService.deleteRefreshToken();
        return ResponseEntity.ok().build();
    }
}
