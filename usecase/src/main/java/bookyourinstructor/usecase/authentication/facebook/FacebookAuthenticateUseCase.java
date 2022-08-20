package bookyourinstructor.usecase.authentication.facebook;

import bookyourinstructor.usecase.authentication.jwt.JwtGenerator;
import bookyourinstructor.usecase.authentication.user.UserStore;
import com.quary.bookyourinstructor.model.authentication.EmailAndExternalIdentity;
import com.quary.bookyourinstructor.model.authentication.exception.UserNotFoundException;
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

    public String authenticate(String facebookAccessKey, UserType userType) throws UserWithEmailAlreadyExists, UserNotFoundException {
        checkArgument(isNotBlank(facebookAccessKey), "Facebook access key cannot be blank");
        final FacebookUserData userData = profileDataFetcher.fetchEmailAndExternalId(facebookAccessKey);
        final EmailAndExternalIdentity emailAndExternalId = userData.getEmailAndExternalIdentity();
        final Optional<User> existingUser = userStore.getByExternalIdentity(emailAndExternalId.getExternalIdentity());
        if (existingUser.isPresent()) {
            User jwtPrincipalUser = existingUser.get();
            return jwtGenerator.generateJwt(jwtPrincipalUser.getEmail(), jwtPrincipalUser.getType(), jwtValidityDuration);
        } else if (userType != null) {
            User jwtPrincipalUser = registerUser(userData, userType);
            return jwtGenerator.generateJwt(jwtPrincipalUser.getEmail(), jwtPrincipalUser.getType(), jwtValidityDuration);
        } else {
            throw new UserNotFoundException();
        }
    }

    private User registerUser(FacebookUserData facebookUserData, UserType userType) throws UserWithEmailAlreadyExists {
        final User user = createUser(facebookUserData, userType);
        userStore.registerUser(user);
        return user;
    }

    private static User createUser(FacebookUserData userData, UserType userType) {
        final EmailAndExternalIdentity emailAndExternalId = userData.getEmailAndExternalIdentity();
        return User.createNewExternalUser(emailAndExternalId.getEmail(), userData.getName(), userData.getSurname(), userType,
                emailAndExternalId.getExternalIdentity());
    }
}
