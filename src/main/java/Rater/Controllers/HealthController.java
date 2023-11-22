package Rater.Controllers;

import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Health.HealthObject;
import Rater.Models.Org.Org;
import Rater.Models.User.User;
import Rater.Security.SecurityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/health")
public class HealthController {
    private static final Logger log = LogManager.getLogger(HealthController.class);

    private final SecurityService securityService;

    @Autowired
    public HealthController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @RequestMapping(method = GET)
    public ResponseEntity<HealthObject> getHealth() {
        return ResponseEntity.ok(new HealthObject("healthy"));
    }

    @CrossOrigin
    @RequestMapping(method = GET, value = "/me")
    public ResponseEntity<Optional<User>> getUser() throws InternalServerException, UnauthorizedException {
        return ResponseEntity.ok(securityService.getAuthedUser());
    }

    @RequestMapping(method = GET, value = "/mew")
    public ResponseEntity<Optional<User>> getUser2() throws InternalServerException, UnauthorizedException {
        return ResponseEntity.ok(Optional.of(new User("test", "testpass", new Org("tr3ijt"))));
    }
}
