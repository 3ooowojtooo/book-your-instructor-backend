package bookyourinstructor.usecase.authentication.facebook;

import com.quary.bookyourinstructor.model.authentication.EmailAndExternalIdentity;
import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
public class FacebookUserData {

    private final EmailAndExternalIdentity emailAndExternalIdentity;
    private final String name;
    private final String surname;

    public FacebookUserData(EmailAndExternalIdentity emailAndExternalIdentity, String name, String surname) {
        validateConstructorArgs(name, surname);
        this.emailAndExternalIdentity = emailAndExternalIdentity;
        this.name = name;
        this.surname = surname;
    }

    private static void validateConstructorArgs(String name, String surname) {
        checkArgument(isNotBlank(name), "User name cannot be blank");
        checkArgument(isNotBlank(surname), "User surname cannot be blank");
    }
}
