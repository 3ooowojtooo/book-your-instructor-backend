package bookyourinstructor.usecase.authentication.jwt;

import com.quary.bookyourinstructor.model.authentication.exception.ExpiredJwtException;
import com.quary.bookyourinstructor.model.authentication.exception.InvalidJwtException;
import io.jsonwebtoken.Claims;

import java.time.Instant;
import java.util.function.Function;

public interface JwtClaimExtractor {

    <T> T extractClaim(String token, Function<Claims, T> extractor) throws InvalidJwtException, ExpiredJwtException;

    String extractSubject(String token) throws InvalidJwtException, ExpiredJwtException;

    Instant extractExpirationTime(String token) throws InvalidJwtException, ExpiredJwtException;
}
