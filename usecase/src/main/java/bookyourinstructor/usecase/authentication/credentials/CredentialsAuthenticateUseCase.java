package bookyourinstructor.usecase.authentication.credentials;

import bookyourinstructor.usecase.authentication.jwt.JwtGenerator;
import bookyourinstructor.usecase.authentication.user.UserStore;
import com.quary.bookyourinstructor.model.authentication.EmailAndPassword;
import com.quary.bookyourinstructor.model.authentication.exception.InvalidEmailOrPasswordException;
import com.quary.bookyourinstructor.model.user.User;
import com.quary.bookyourinstructor.model.user.UserType;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class CredentialsAuthenticateUseCase {

    private final CredentialsAuthenticationStore credentialsAuthenticationStore;
    private final UserStore userStore;
    private final JwtGenerator jwtGenerator;
    private final Duration jwtValidityDuration;

    public String authenticate(EmailAndPassword emailAndPassword) throws InvalidEmailOrPasswordException {
        credentialsAuthenticationStore.tryAuthenticateOrThrow(emailAndPassword);
        final String jwtSubject = emailAndPassword.getEmail();
        final UserType userType = getUserType(emailAndPassword.getEmail());
        return jwtGenerator.generateJwt(jwtSubject, userType, jwtValidityDuration);
    }

    private UserType getUserType(String email) throws InvalidEmailOrPasswordException {
        return userStore.getByEmail(email)
                .map(User::getType)
                .orElseThrow(InvalidEmailOrPasswordException::new);
    }
}
