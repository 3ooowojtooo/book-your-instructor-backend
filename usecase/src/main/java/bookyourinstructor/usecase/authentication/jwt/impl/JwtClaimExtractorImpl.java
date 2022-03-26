package bookyourinstructor.usecase.authentication.jwt.impl;

import bookyourinstructor.usecase.authentication.jwt.JwtClaimExtractor;
import bookyourinstructor.usecase.util.time.TimeUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
public class JwtClaimExtractorImpl implements JwtClaimExtractor {

    private final String jwtSecret;
    private final TimeUtils timeUtils;

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimExtractor) {
        final Claims claims = extractAllClaims(token);
        return claimExtractor.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public LocalDateTime extractExpirationTime(String token) {
        final Date expirationDate = extractClaim(token, Claims::getExpiration);
        return timeUtils.toLocalDateTime(expirationDate.toInstant());
    }
}
