package Rater.Controllers;

import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Org.Org;
import Rater.Models.User.User;
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
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);

    private UserService userService;
    private SecurityService securityService;

    @Autowired
    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
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
}
