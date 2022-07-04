package bookyourinstructor.usecase.authentication.user;

import com.quary.bookyourinstructor.model.authentication.exception.UserWithEmailAlreadyExists;
import com.quary.bookyourinstructor.model.user.ExternalIdentity;
import com.quary.bookyourinstructor.model.user.User;

import java.util.Optional;

public interface UserStore {

    boolean userExists(String email);

    boolean userExists(ExternalIdentity externalIdentity);

    void registerUser(User user) throws UserWithEmailAlreadyExists;

    Optional<User> getByEmail(String email);

    Optional<User> findById(Integer id);
}
