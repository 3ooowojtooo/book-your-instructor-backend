package bookyourinstructor.usecase.authentication.jwt.impl;

import bookyourinstructor.usecase.authentication.jwt.JwtGenerator;
import bookyourinstructor.usecase.util.time.TimeUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.HashMap;

@RequiredArgsConstructor
public class JwtGeneratorImpl implements JwtGenerator {

    private final String jwtSecret;
    private final TimeUtils timeUtils;

    @Override
    public String generateJwt(String subject, Duration validityDuration) {
        final Instant now = timeUtils.nowInstant();
        final Date issuedDate = computeIssuedDate(now);
        final Date expirationDate = computeExpirationDate(now, validityDuration);
        return Jwts.builder()
                .setClaims(new HashMap<>())
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
}
