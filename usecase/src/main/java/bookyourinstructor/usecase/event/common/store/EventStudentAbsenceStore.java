package bookyourinstructor.usecase.event.common.store;

import com.quary.bookyourinstructor.model.event.EventStudentAbsence;

public interface EventStudentAbsenceStore {

    void save(EventStudentAbsence absence);

    boolean existsByRealizationIdAndStudentId(Integer eventRealizationId, Integer studentId);
}
