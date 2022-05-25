package bookyourinstructor.usecase.authentication.jwt.impl;

import bookyourinstructor.usecase.authentication.jwt.JwtClaimExtractor;
import bookyourinstructor.usecase.authentication.jwt.JwtValidator;
import bookyourinstructor.usecase.util.time.TimeUtils;
import com.quary.bookyourinstructor.model.authentication.exception.ExpiredJwtException;
import com.quary.bookyourinstructor.model.authentication.exception.InvalidJwtException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class JwtValidatorImpl implements JwtValidator {

    private final JwtClaimExtractor jwtClaimExtractor;
    private final TimeUtils timeUtils;

    @Override
    public boolean isTokenValid(String token, String subject) {
        try {
            final String extractedSubject = jwtClaimExtractor.extractSubject(token);
            return nonNull(token) &&
                    nonNull(subject) &&
                    subject.equals(extractedSubject) &&
                    !isTokenExpired(token);
        } catch (InvalidJwtException | ExpiredJwtException ignored) {
            return false;
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            final Instant now = timeUtils.nowInstant();
            final Instant tokenExpiration = jwtClaimExtractor.extractExpirationTime(token);
            return tokenExpiration.isBefore(now) || tokenExpiration.equals(now);
        } catch (InvalidJwtException | ExpiredJwtException ignored) {
            return true;
        }
    }
}
