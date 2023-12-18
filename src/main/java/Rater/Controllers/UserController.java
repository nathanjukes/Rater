package Rater.Controllers;

import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Org.Org;
import Rater.Models.User.User;
import Rater.Models.User.UserRole;
import Rater.Models.User.UserUpdateRequest;
import Rater.Security.RefreshTokenService;
import Rater.Security.SecurityService;
import Rater.Services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static Rater.Security.SecurityService.throwIfNoAuth;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);

    private UserService userService;
    private SecurityService securityService;
    private RefreshTokenService refreshTokenService;

    @Autowired
    public UserController(UserService userService, SecurityService securityService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.securityService = securityService;
        this.refreshTokenService = refreshTokenService;
    }

    @RequestMapping(value = "/{id}", method = GET)
    public ResponseEntity<Optional<User>> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @CrossOrigin
    @RequestMapping(value = "", method = GET)
    public ResponseEntity<List<User>> getUsers() throws InternalServerException, UnauthorizedException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        return ResponseEntity.ok(userService.getUsers(org.map(Org::getId).orElseThrow(UnauthorizedException::new)));
    }

    @CrossOrigin
    @RequestMapping(value = "/me", method = GET)
    public ResponseEntity<Optional<User>> getUser() throws InternalServerException, UnauthorizedException {
        return ResponseEntity.ok(securityService.getAuthedUser());
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) throws InternalServerException, UnauthorizedException, BadRequestException {
        Optional<Org> org = securityService.getAuthedOrg();
        Optional<User> auth = securityService.getAuthedUser();
        throwIfNoAuth(org);

        log.info("Delete user request for userId: {}", id);
        if (auth.isEmpty() || !isAuthedToOperate(auth) || auth.map(User::getId).get().equals(id)) {
            log.info("Delete user request for userId: {} failed - no auth or self delete", id);
            throw new UnauthorizedException();
        }

        // Can only delete user accounts
        Optional<User> user = userService.getUser(id);
        if (user.map(User::getRole).orElseThrow().equals(UserRole.user)) {
            refreshTokenService.deleteRefreshToken(id);
        } else {
            log.info("Error deleting user {} - role is not user", id);
            throw new BadRequestException();
        }

        if (userService.deleteUser(id, org.orElseThrow()) != 1) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("");
    }

    @RequestMapping(value = "/{id}", method = PUT)
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest updateRequest) throws InternalServerException, UnauthorizedException, BadRequestException {
        Optional<Org> org = securityService.getAuthedOrg();
        throwIfNoAuth(org);

        if (!isAuthedToOperate(securityService.getAuthedUser())) {
            throw new UnauthorizedException();
        }

        return ResponseEntity.ok(userService.updateUser(id, updateRequest, org.orElseThrow()));
    }

    private boolean isAuthedToOperate(Optional<User> auth) {
        return !auth.map(User::getRole).get().equals(UserRole.user);
    }
}
