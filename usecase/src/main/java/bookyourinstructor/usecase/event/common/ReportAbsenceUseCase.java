package bookyourinstructor.usecase.event.common;

import bookyourinstructor.usecase.event.common.data.ReportAbsenceData;
import bookyourinstructor.usecase.event.common.helper.InstructorAbsenceReporter;
import bookyourinstructor.usecase.event.common.helper.StudentAbsenceReporter;
import bookyourinstructor.usecase.event.common.port.UserData;
import com.quary.bookyourinstructor.model.event.exception.ConcurrentDataModificationException;
import com.quary.bookyourinstructor.model.event.exception.EventChangedException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReportAbsenceUseCase {

    private final InstructorAbsenceReporter instructorAbsenceReporter;
    private final StudentAbsenceReporter studentAbsenceReporter;

    public void reportAbsence(ReportAbsenceData data) throws EventChangedException, ConcurrentDataModificationException {
        UserData user = data.getUser();
        if (user.isInstructor()) {
            instructorAbsenceReporter.reportAbsence(data);
        } else if (user.isStudent()) {
            studentAbsenceReporter.reportAbsence(data);
        }
    }
}
