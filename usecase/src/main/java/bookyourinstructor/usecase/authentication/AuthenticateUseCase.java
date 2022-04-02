package bookyourinstructor.usecase.authentication;

import bookyourinstructor.usecase.authentication.jwt.JwtGenerator;
import com.quary.bookyourinstructor.model.user.authentication.EmailAndPassword;
import com.quary.bookyourinstructor.model.user.authentication.exception.InvalidEmailOrPasswordException;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class AuthenticateUseCase {

    private final AuthenticationStore authenticationStore;
    private final JwtGenerator jwtGenerator;
    private final Duration jwtValidityDuration;

    public String authenticate(EmailAndPassword emailAndPassword) throws InvalidEmailOrPasswordException {
        authenticationStore.tryAuthenticateOrThrow(emailAndPassword);
        final String jwtSubject = emailAndPassword.getEmail();
        return jwtGenerator.generateJwt(jwtSubject, jwtValidityDuration);
    }
}
