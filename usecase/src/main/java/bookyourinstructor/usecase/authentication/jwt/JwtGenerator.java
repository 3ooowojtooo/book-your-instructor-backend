package bookyourinstructor.usecase.authentication.jwt;

import java.time.Duration;

public interface JwtGenerator {

    String generateJwt(String subject, Duration validityDuration);
}
