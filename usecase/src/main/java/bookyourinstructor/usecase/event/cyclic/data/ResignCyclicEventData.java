package bookyourinstructor.usecase.event.cyclic.data;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class ResignCyclicEventData {

    private final Integer cyclicEventId;
    private final Integer cyclicEventVersion;
    private final Integer studentId;

    public ResignCyclicEventData(Integer cyclicEventId, Integer cyclicEventVersion, Integer studentId) {
        validateConstructorArgs(cyclicEventId, cyclicEventVersion, studentId);
        this.cyclicEventId = cyclicEventId;
        this.cyclicEventVersion = cyclicEventVersion;
        this.studentId = studentId;
    }

    private static void validateConstructorArgs(Integer cyclicEventId, Integer cyclicEventVersion, Integer studentId) {
        checkNotNull(cyclicEventId, "Cyclic event id cannot be null");
        checkNotNull(cyclicEventVersion, "Cyclic event verion cannot be null");
        checkNotNull(studentId, "Student id cannot be null");
    }
}
