package bookyourinstructor.usecase.authentication.jwt;

import io.jsonwebtoken.Claims;

import java.time.LocalDateTime;
import java.util.function.Function;

public interface JwtClaimExtractor {

    <T> T extractClaim(String token, Function<Claims, T> extractor);

    String extractSubject(String token);

    LocalDateTime extractExpirationTime(String token);
}
