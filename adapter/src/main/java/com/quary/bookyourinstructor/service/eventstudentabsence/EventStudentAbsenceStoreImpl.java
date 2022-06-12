package com.quary.bookyourinstructor.service.eventstudentabsence;

import bookyourinstructor.usecase.event.common.store.EventStudentAbsenceStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventStudentAbsenceStoreImpl implements EventStudentAbsenceStore {

    private final EventStudentAbsenceStoreMapper mapper;
}
