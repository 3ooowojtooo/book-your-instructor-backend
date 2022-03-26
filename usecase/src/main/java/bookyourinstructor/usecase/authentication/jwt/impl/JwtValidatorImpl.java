package bookyourinstructor.usecase.authentication.jwt.impl;

import bookyourinstructor.usecase.authentication.jwt.JwtClaimExtractor;
import bookyourinstructor.usecase.authentication.jwt.JwtValidator;
import bookyourinstructor.usecase.util.time.TimeUtils;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class JwtValidatorImpl implements JwtValidator {

    private final JwtClaimExtractor jwtClaimExtractor;
    private final TimeUtils timeUtils;

    @Override
    public boolean isTokenValid(String token, String subject) {
        final String extractedSubject = jwtClaimExtractor.extractSubject(token);
        return subject.equals(extractedSubject) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        final LocalDateTime now = timeUtils.nowLocalDateTime();
        final LocalDateTime tokenExpiration = jwtClaimExtractor.extractExpirationTime(token);
        return tokenExpiration.isBefore(now) || tokenExpiration.isEqual(now);
    }
}
