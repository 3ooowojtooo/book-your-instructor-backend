package bookyourinstructor.usecase.authentication.credentials;

import com.quary.bookyourinstructor.model.authentication.EmailAndPassword;
import com.quary.bookyourinstructor.model.authentication.exception.InvalidEmailOrPasswordException;

public interface CredentialsAuthenticationStore {

    void tryAuthenticateOrThrow(EmailAndPassword emailAndPassword) throws InvalidEmailOrPasswordException;
}
