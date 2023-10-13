package Rater.Security;

import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.App.App;
import Rater.Models.BuildComponent;
import Rater.Models.Org.Org;
import Rater.Models.User.User;
import Rater.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SecurityService {
    private final UserService userService;

    @Autowired
    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public boolean hasOrg(String orgName) throws InternalServerException, UnauthorizedException {
        Optional<User> user = getAuthedUser();

        return user.map(u -> u.getOrg().getName())
                .filter(n -> n.equals(orgName))
                .isPresent();
    }

    public boolean hasOrg(UUID orgId) throws InternalServerException, UnauthorizedException {
        Optional<Org> org = getAuthedOrg();

        return org.map(o -> o.getId())
                .filter(o -> o.equals(orgId))
                .isPresent();
    }

    public boolean hasOrg(Optional<BuildComponent> obj) throws InternalServerException, UnauthorizedException {
        return obj.isEmpty() ? true : hasOrg(obj.get().getOrgId());
    }

    private Optional<User> getAuthedUser() throws InternalServerException, UnauthorizedException {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            return userService.getUserByEmail(userDetails.getUsername());
        } catch (ClassCastException ex) {
            // When token not provided or token expired
            throw new UnauthorizedException();
        } catch (Exception ex) {
            // Only expecting true 500s here
            throw new InternalServerException();
        }
    }

    public Optional<Org> getAuthedOrg() throws InternalServerException, UnauthorizedException {
        return Optional.of(getAuthedUser().map(u -> u.getOrg()).orElseThrow());
    }

    public static void throwIfNoAuth(Optional<Org> org) throws UnauthorizedException {
        if (org.isEmpty()) {
            throw new UnauthorizedException();
        }
    }
}
