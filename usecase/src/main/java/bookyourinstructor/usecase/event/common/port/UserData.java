package bookyourinstructor.usecase.event.common.port;

import com.quary.bookyourinstructor.model.user.UserType;

public interface UserData {

    Integer getId();

    String getEmail();

    boolean isInstructor();

    boolean isStudent();

    UserType getType();
}
