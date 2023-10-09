package Rater.Security;

import Rater.Exceptions.InternalServerException;
import Rater.Models.Org.Org;
import Rater.Models.User.User;
import Rater.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private Optional<User> getAuthedUser() throws InternalServerException {
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

    public Optional<Org> getAuthedOrg() throws InternalServerException {
        return Optional.of(getAuthedUser().map(u -> u.getOrg()).orElseThrow());
    }
}
