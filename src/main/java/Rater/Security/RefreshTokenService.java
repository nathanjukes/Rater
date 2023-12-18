package Rater.Security;

import Rater.Exceptions.InternalServerException;
import Rater.Exceptions.UnauthorizedException;
import Rater.Models.Auth.RefreshToken;
import Rater.Models.User.User;
import Rater.Repositories.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final int EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 Hours in MS

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(Optional<User> userOpt) {
        RefreshToken refreshToken = new RefreshToken(
                userOpt.orElseThrow(),
                UUID.randomUUID(),
                new Date(System.currentTimeMillis() + EXPIRATION_TIME)
        );

        return saveToken(refreshToken);
    }

    private RefreshToken saveToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean verifyToken(Optional<RefreshToken> refreshToken) {
        if (refreshToken.isPresent() && !refreshToken.map(RefreshToken::getExpiration).map(d -> d.before(new Date())).orElse(false)) {
            return true;
        }

        return false;
    }

    public Optional<RefreshToken> getRefreshToken(UUID refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    public void deleteRefreshToken(Optional<User> userOpt) throws InternalServerException, UnauthorizedException {
        refreshTokenRepository.deleteByUserId(userOpt.map(User::getId).orElseThrow());
    }

    public void deleteRefreshToken(UUID id) throws InternalServerException, UnauthorizedException {
        refreshTokenRepository.deleteByUserId(id);
    }
}
