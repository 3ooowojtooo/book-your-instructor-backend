package bookyourinstructor.usecase.authentication;

import com.quary.bookyourinstructor.model.user.EmailAndPassword;
import com.quary.bookyourinstructor.model.user.exception.InvalidEmailOrPasswordException;

public interface AuthenticationStore {

    void tryAuthenticateOrThrow(EmailAndPassword emailAndPassword) throws InvalidEmailOrPasswordException;
}
