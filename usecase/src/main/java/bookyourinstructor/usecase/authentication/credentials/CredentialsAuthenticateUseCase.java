package bookyourinstructor.usecase.authentication.credentials;

import bookyourinstructor.usecase.authentication.jwt.JwtGenerator;
import com.quary.bookyourinstructor.model.authentication.EmailAndPassword;
import com.quary.bookyourinstructor.model.authentication.exception.InvalidEmailOrPasswordException;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class CredentialsAuthenticateUseCase {

    private final CredentialsAuthenticationStore credentialsAuthenticationStore;
    private final JwtGenerator jwtGenerator;
    private final Duration jwtValidityDuration;

    public String authenticate(EmailAndPassword emailAndPassword) throws InvalidEmailOrPasswordException {
        credentialsAuthenticationStore.tryAuthenticateOrThrow(emailAndPassword);
        final String jwtSubject = emailAndPassword.getEmail();
        return jwtGenerator.generateJwt(jwtSubject, jwtValidityDuration);
    }
}
