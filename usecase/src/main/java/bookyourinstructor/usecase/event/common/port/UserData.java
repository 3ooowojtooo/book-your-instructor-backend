package bookyourinstructor.usecase.event.common.port;

public interface UserData {

    Integer getId();
    String getEmail();
    boolean isInstructor();
    boolean isStudent();
    boolean isUndeclared();
}
