package bookyourinstructor.usecase.authentication.jwt.impl;

import bookyourinstructor.usecase.authentication.jwt.JwtClaimExtractor;
import bookyourinstructor.usecase.util.time.TimeUtils;
import com.quary.bookyourinstructor.model.authentication.exception.ExpiredJwtException;
import com.quary.bookyourinstructor.model.authentication.exception.InvalidJwtException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
public class JwtClaimExtractorImpl implements JwtClaimExtractor {

    private final String jwtSecret;
    private final TimeUtils timeUtils;

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimExtractor) throws InvalidJwtException, ExpiredJwtException {
        final Claims claims = extractAllClaims(token);
        return claimExtractor.apply(claims);
    }

    private Claims extractAllClaims(String token) throws InvalidJwtException, ExpiredJwtException {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new InvalidJwtException(ex);
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            throw new ExpiredJwtException(ex);
        }
    }

    @Override
    public String extractSubject(String token) throws InvalidJwtException, ExpiredJwtException {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public LocalDateTime extractExpirationTime(String token) throws InvalidJwtException, ExpiredJwtException {
        final Date expirationDate = extractClaim(token, Claims::getExpiration);
        return timeUtils.toLocalDateTime(expirationDate.toInstant());
    }
}
