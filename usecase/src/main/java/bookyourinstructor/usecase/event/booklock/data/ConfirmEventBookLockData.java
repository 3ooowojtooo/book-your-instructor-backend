package bookyourinstructor.usecase.event.booklock.data;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class ConfirmEventBookLockData {

    private final Integer bookLockId;
    private final Integer studentId;

    public ConfirmEventBookLockData(Integer bookLockId, Integer userId) {
        validateConstructorArgs(bookLockId, userId);
        this.bookLockId = bookLockId;
        this.studentId = userId;
    }

    private static void validateConstructorArgs(Integer bookLockId, Integer userId) {
        checkNotNull(bookLockId, "Confirm book lock event id cannot be null");
        checkNotNull(userId, "Confirm book lock event student id cannot be null");
    }
}
