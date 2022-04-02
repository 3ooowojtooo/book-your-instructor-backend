package bookyourinstructor.usecase.authentication;

import com.quary.bookyourinstructor.model.user.authentication.EmailAndPassword;
import com.quary.bookyourinstructor.model.user.authentication.exception.InvalidEmailOrPasswordException;

public interface AuthenticationStore {

    void tryAuthenticateOrThrow(EmailAndPassword emailAndPassword) throws InvalidEmailOrPasswordException;
}
