package com.quary.bookyourinstructor.service.eventstudentabsence;

import bookyourinstructor.usecase.event.common.store.EventStudentAbsenceStore;
import com.quary.bookyourinstructor.entity.EventEntity;
import com.quary.bookyourinstructor.entity.EventRealizationEntity;
import com.quary.bookyourinstructor.entity.EventStudentAbsenceEntity;
import com.quary.bookyourinstructor.entity.UserEntity;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.EventStudentAbsence;
import com.quary.bookyourinstructor.repository.EventRealizationRepository;
import com.quary.bookyourinstructor.repository.EventStudentAbsenceRepository;
import com.quary.bookyourinstructor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventStudentAbsenceStoreImpl implements EventStudentAbsenceStore {

    private final EventStudentAbsenceRepository absenceRepository;
    private final UserRepository userRepository;
    private final EventRealizationRepository eventRealizationRepository;
    private final EventStudentAbsenceStoreMapper mapper;

    @Override
    public void save(EventStudentAbsence absence) {
        EventStudentAbsenceEntity entity = mapToEntity(absence);
        absenceRepository.save(entity);
    }

    private EventStudentAbsenceEntity mapToEntity(EventStudentAbsence absence) {
        Integer eventRealizationId = absence.getEventRealizationId();
        Integer studentId = absence.getStudentId();
        final EventRealizationEntity eventRealization = eventRealizationRepository.findById(eventRealizationId)
                .orElseThrow(() -> new IllegalStateException("Event realization instance with id " + eventRealizationId + " not found"));
        final UserEntity student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("User instance with id " + studentId + " not found"));
        return mapper.mapToEntity(absence, eventRealization, student);
    }
}
