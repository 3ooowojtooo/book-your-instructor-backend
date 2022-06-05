package bookyourinstructor.usecase.event.booklock.data;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class DeleteEventBookLockData {

    private final Integer bookLockId;
    private final Integer studentId;

    public DeleteEventBookLockData(Integer bookLockId, Integer studentId) {
        validateConstructorArgs(bookLockId, studentId);
        this.bookLockId = bookLockId;
        this.studentId = studentId;
    }

    private static void validateConstructorArgs(Integer bookLockId, Integer studentId) {
        checkNotNull(bookLockId, "Book lock id cannot be null");
        checkNotNull(studentId, "Book lock student id cannot be null");
    }
}
