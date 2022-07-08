package bookyourinstructor.usecase.authentication.facebook;

import bookyourinstructor.usecase.authentication.jwt.JwtGenerator;
import bookyourinstructor.usecase.authentication.user.UserStore;
import com.quary.bookyourinstructor.model.authentication.EmailAndExternalIdentity;
import com.quary.bookyourinstructor.model.authentication.exception.UserWithEmailAlreadyExists;
import com.quary.bookyourinstructor.model.user.User;
import com.quary.bookyourinstructor.model.user.UserType;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@RequiredArgsConstructor
public class FacebookAuthenticateUseCase {

    private final FacebookProfileDataFetcher profileDataFetcher;
    private final UserStore userStore;
    private final JwtGenerator jwtGenerator;
    private final Duration jwtValidityDuration;

    public String authenticate(String facebookAccessKey) throws UserWithEmailAlreadyExists {
        checkArgument(isNotBlank(facebookAccessKey), "Facebook access key cannot be blank");
        final EmailAndExternalIdentity emailAndExternalId = profileDataFetcher.fetchEmailAndExternalId(facebookAccessKey);
        final Optional<User> existingUser = userStore.getByExternalIdentity(emailAndExternalId.getExternalIdentity());
        final User jwtPrincipalUser;
        if (existingUser.isPresent()) {
            jwtPrincipalUser = existingUser.get();
        } else {
            jwtPrincipalUser = registerUser(emailAndExternalId);
        }
        return jwtGenerator.generateJwt(jwtPrincipalUser.getEmail(), jwtPrincipalUser.getType(), jwtValidityDuration);

    }

    private User registerUser(EmailAndExternalIdentity emailAndExternalIdentity) throws UserWithEmailAlreadyExists {
        final User user = createUser(emailAndExternalIdentity);
        userStore.registerUser(user);
        return user;
    }

    private static User createUser(EmailAndExternalIdentity emailAndExternalId) {
        return User.createNewExternalUser(emailAndExternalId.getEmail(), UserType.UNDECLARED,
                emailAndExternalId.getExternalIdentity());
    }
}
