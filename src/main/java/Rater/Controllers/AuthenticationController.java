package Rater.Controllers;

import Rater.Models.Org.Org;
import Rater.Models.User.User;
import Rater.Models.User.UserCreateRequest;
import Rater.Models.User.UserLoginRequest;
import Rater.Security.JwtUtil;
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
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationController(UserService userService, OrgService orgService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.orgService = orgService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /*   @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<Map<String, UUID>> registerUser(@Valid @RequestBody User user) throws DataConflictException {
        *//*if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }*//*

      *//*  if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }*//*

        user.encodePassword(encoder);
        user = userService.addUser(user);
        return ResponseEntity.ok(Collections.singletonMap("id", user.getId()));
    }*/

    @RequestMapping(value = "/register", method = POST)
    public ResponseEntity<Optional<User>> userRegistration(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        // 1. Does Org Exist already?
        // 2. Does user exist in org?

        Optional<Org> userOrg = orgService.getOrg(userCreateRequest.getOrgName());

        Optional<User> user = userService.createUser(userCreateRequest, userOrg.orElseThrow(), passwordEncoder);

        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/login", method = POST)
    public ResponseEntity<String> userRegistration(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtUtil.generateJwtToken(auth);

        return ResponseEntity.ok(jwt);
    }
}
