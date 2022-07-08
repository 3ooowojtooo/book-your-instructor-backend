package bookyourinstructor.usecase.authentication.jwt;

import com.quary.bookyourinstructor.model.user.UserType;

import java.time.Duration;

public interface JwtGenerator {

    String generateJwt(String subject, UserType userType, Duration validityDuration);
}
