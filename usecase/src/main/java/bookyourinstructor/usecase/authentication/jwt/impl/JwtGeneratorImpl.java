package bookyourinstructor.usecase.authentication.jwt.impl;

import bookyourinstructor.usecase.authentication.jwt.JwtGenerator;
import bookyourinstructor.usecase.util.time.TimeUtils;
import com.quary.bookyourinstructor.model.user.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@RequiredArgsConstructor
public class JwtGeneratorImpl implements JwtGenerator {

    private static final String ROLE_CLAIM_KEY = "role";

    private final String jwtSecret;
    private final TimeUtils timeUtils;

    @Override
    public String generateJwt(String subject, UserType userType, Duration validityDuration) {
        final Instant now = timeUtils.nowInstant();
        final Date issuedDate = computeIssuedDate(now);
        final Date expirationDate = computeExpirationDate(now, validityDuration);
        final Claims claims = createClaims(userType);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    private Date computeIssuedDate(Instant now) {
        return instantToDate(now);
    }

    private Date computeExpirationDate(Instant now, Duration validityDuration) {
        final Instant expirationInstant = now.plus(validityDuration);
        return instantToDate(expirationInstant);
    }

    private static Date instantToDate(Instant instant) {
        return new Date(instant.toEpochMilli());
    }

    private Claims createClaims(UserType userType) {
        final Claims claims = new DefaultClaims();
        claims.put(ROLE_CLAIM_KEY, userType.name());
        return claims;
    }
}
