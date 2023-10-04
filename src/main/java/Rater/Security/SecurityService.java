package Rater.Security;

import Rater.Exceptions.InternalServerException;
import Rater.Models.User;
import Rater.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {
    private final UserService userService;

    @Autowired
    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public boolean hasOrg(String orgName) throws InternalServerException {
        Optional<User> user = getAuthedUser();

        return user.map(u -> u.getOrg().getName())
                .filter(n -> n.equals(orgName))
                .isPresent();
    }

    public Optional<User> getAuthedUser() throws InternalServerException {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            return userService.getUserByEmail(userDetails.getUsername());
        } catch (Exception ex) {
            // Only expecting 500s here
            throw new InternalServerException();
        }
    }
}
