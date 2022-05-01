package bookyourinstructor.usecase.authentication.facebook;

import bookyourinstructor.usecase.authentication.jwt.JwtGenerator;
import bookyourinstructor.usecase.authentication.user.UserStore;
import com.quary.bookyourinstructor.model.authentication.EmailAndExternalIdentity;
import com.quary.bookyourinstructor.model.authentication.EmailAndPassword;
import com.quary.bookyourinstructor.model.authentication.exception.UserWithEmailAlreadyExists;
import com.quary.bookyourinstructor.model.user.ExternalIdentity;
import com.quary.bookyourinstructor.model.user.ExternalIdentityProvider;
import com.quary.bookyourinstructor.model.user.User;
import com.quary.bookyourinstructor.model.user.UserType;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

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
        if (!userStore.userExists(emailAndExternalId.getEmail())) {
            final User user = createUser(emailAndExternalId);
            userStore.registerUser(user);
        }
        return jwtGenerator.generateJwt(emailAndExternalId.getEmail(), jwtValidityDuration);
    }

    private static User createUser(EmailAndExternalIdentity emailAndExternalId) {
        return User.createNewExternalUser(emailAndExternalId.getEmail(), UserType.UNDECLARED,
                emailAndExternalId.getExternalIdentity());
    }
}
