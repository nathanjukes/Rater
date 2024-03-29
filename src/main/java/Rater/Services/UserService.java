package Rater.Services;

import Rater.Controllers.OrgController;
import Rater.Exceptions.BadRequestException;
import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Org.Org;
import Rater.Models.User.*;
import Rater.Repositories.UserRepository;
import Rater.Security.RefreshTokenService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static Rater.Models.User.UserRole.owner;

@Service
@Transactional
public class UserService {
    private static final Logger log = LogManager.getLogger(UserService.class);

    private UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserService(UserRepository userRepository, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    public Optional<User> getUser(UUID userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsers(UUID orgId) {
        return userRepository.findByOrgId(orgId);
    }

    public Optional<User> createUser(UserCreateRequest userCreateRequest, Org org, PasswordEncoder passwordEncoder) {
        userCreateRequest.encode(passwordEncoder);
        User user = User.from(userCreateRequest, org);
        return Optional.ofNullable(userRepository.save(user));
    }

    public Optional<User> createUser(OrgUserCreateRequest orgUserCreateRequest, Org org, PasswordEncoder passwordEncoder) {
        orgUserCreateRequest.encode(passwordEncoder);
        User user = User.from(orgUserCreateRequest, org);
        return Optional.ofNullable(userRepository.save(user));
    }

    public Optional<User> createUser(ServiceAccountCreateRequest serviceAccountCreateRequest, Org org, PasswordEncoder passwordEncoder) {
        serviceAccountCreateRequest.encode(passwordEncoder);
        User user = User.from(serviceAccountCreateRequest, org);
        return Optional.ofNullable(userRepository.save(user));
    }

    public int deleteUser(UUID id, Org org, boolean orgDelete) throws BadRequestException, InternalServerException, UnauthorizedException {
        Optional<User> user = getUser(id);

        // Validates the user deleted is not an owner/admin unless the org is being deleted
        if (user.map(User::getRole).orElseThrow().equals(UserRole.user) || orgDelete) {
            refreshTokenService.deleteRefreshToken(id);
        } else {
            log.info("Error deleting user {} - role is not user", id);
            throw new BadRequestException();
        }

        return userRepository.deleteById(id, org.getId());
    }

    public void deleteUser(String email, Org org) {
        userRepository.deleteByEmail(email);
    }

    public Optional<User> updateUser(UUID id, UserUpdateRequest userUpdateRequest, Org org) throws BadRequestException {
        if (userUpdateRequest.getRole() == null || userUpdateRequest.getRole().equals(owner)) {
            throw new BadRequestException();
        }

        Optional<User> user = getUser(id);
        if (user.map(User::getRole).orElseThrow().equals(owner)) {
            throw new BadRequestException();
        }

        if (user.isPresent()) {
            user.get().setRole(userUpdateRequest.getRole());
            return Optional.of(userRepository.save(user.get()));
        }

        return Optional.empty();
    }
}
