package bookyourinstructor.usecase.authentication.credentials;

import bookyourinstructor.usecase.authentication.user.UserStore;
import com.quary.bookyourinstructor.model.authentication.NewUserData;
import com.quary.bookyourinstructor.model.authentication.NewUserType;
import com.quary.bookyourinstructor.model.authentication.exception.UserWithEmailAlreadyExists;
import com.quary.bookyourinstructor.model.user.User;
import com.quary.bookyourinstructor.model.user.UserType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NewUserRegistrationCredentialsUseCase {

    private final UserStore userStore;
    private final PasswordEncoderHelper passwordEncoderHelper;

    public void registerNewUser(NewUserData newUserData) throws UserWithEmailAlreadyExists {
        if (userStore.userExists(newUserData.getEmail())) {
            throw new UserWithEmailAlreadyExists(newUserData.getEmail());
        }
        final User user = buildUser(newUserData);
        userStore.registerUser(user);
    }

    private User buildUser(NewUserData newUserData) {
        final String encodedPassword = passwordEncoderHelper.encodePassword(newUserData.getPassword());
        return User.createNewCredentialsUser(
                newUserData.getEmail(),
                encodedPassword,
                newUserData.getName(),
                newUserData.getSurname(),
                mapToUserType(newUserData.getType())
        );
    }

    private static UserType mapToUserType(NewUserType newUserType) {
        return newUserType == NewUserType.INSTRUCTOR ?
                UserType.INSTRUCTOR :
                UserType.STUDENT;
    }
}
