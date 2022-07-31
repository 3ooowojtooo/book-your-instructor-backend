package com.quary.bookyourinstructor.controller.event;

import bookyourinstructor.usecase.event.booklock.ConfirmEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.CreateEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.DeleteEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.data.ConfirmEventBookLockData;
import bookyourinstructor.usecase.event.booklock.data.CreateEventBookLockData;
import bookyourinstructor.usecase.event.booklock.data.DeleteEventBookLockData;
import bookyourinstructor.usecase.event.common.AcceptEventUseCase;
import bookyourinstructor.usecase.event.common.DeleteDraftEventUseCase;
import bookyourinstructor.usecase.event.common.GetEventDetailsAsStudentUseCase;
import bookyourinstructor.usecase.event.common.ReportAbsenceUseCase;
import bookyourinstructor.usecase.event.common.data.AcceptEventData;
import bookyourinstructor.usecase.event.common.data.DeleteDraftEventData;
import bookyourinstructor.usecase.event.common.data.GetEventDetailsAsStudentData;
import bookyourinstructor.usecase.event.common.data.ReportAbsenceData;
import bookyourinstructor.usecase.event.common.result.GetEventDetailsAsStudentResult;
import bookyourinstructor.usecase.event.cyclic.DeclareCyclicEventUseCase;
import bookyourinstructor.usecase.event.cyclic.ResignCyclicEventUseCase;
import bookyourinstructor.usecase.event.cyclic.UpdateCyclicEventRealizationUseCase;
import bookyourinstructor.usecase.event.cyclic.data.NewCyclicEventData;
import bookyourinstructor.usecase.event.cyclic.data.ResignCyclicEventData;
import bookyourinstructor.usecase.event.cyclic.data.UpdateCyclicEventRealizationData;
import bookyourinstructor.usecase.event.cyclic.result.DeclareCyclicEventResult;
import bookyourinstructor.usecase.event.search.SearchEventsUseCase;
import bookyourinstructor.usecase.event.search.data.SearchEventsData;
import bookyourinstructor.usecase.event.search.result.SearchEventsResult;
import bookyourinstructor.usecase.event.single.DeclareSingleEventUseCase;
import bookyourinstructor.usecase.event.single.data.NewSingleEventData;
import bookyourinstructor.usecase.event.single.result.DeclareSingleEventResult;
import com.quary.bookyourinstructor.configuration.security.annotation.InstructorAllowed;
import com.quary.bookyourinstructor.configuration.security.annotation.InstructorAndStudentAllowed;
import com.quary.bookyourinstructor.configuration.security.annotation.StudentAllowed;
import com.quary.bookyourinstructor.configuration.security.model.UserContext;
import com.quary.bookyourinstructor.controller.event.mapper.EventMapper;
import com.quary.bookyourinstructor.controller.event.request.*;
import com.quary.bookyourinstructor.controller.event.response.*;
import com.quary.bookyourinstructor.model.event.EventLock;
import com.quary.bookyourinstructor.model.event.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventMapper mapper;
    private final DeclareSingleEventUseCase declareSingleEventUseCase;
    private final DeclareCyclicEventUseCase declareCyclicEventUseCase;
    private final CreateEventBookLockUseCase createEventBookLockUseCase;
    private final ConfirmEventBookLockUseCase confirmEventBookLockUseCase;
    private final AcceptEventUseCase acceptEventUseCase;
    private final UpdateCyclicEventRealizationUseCase updateCyclicEventRealizationUseCase;
    private final DeleteEventBookLockUseCase deleteEventBookLockUseCase;
    private final DeleteDraftEventUseCase deleteDraftEventUseCase;
    private final ReportAbsenceUseCase reportAbsenceUseCase;
    private final ResignCyclicEventUseCase resignCyclicEventUseCase;
    private final SearchEventsUseCase searchEventsUseCase;
    private final GetEventDetailsAsStudentUseCase getEventDetailsAsStudentUseCase;

    @PostMapping(path = "/single", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @InstructorAllowed
    public DeclareSingleEventResponse declareSingleEvent(@RequestBody final DeclareSingleEventRequest request,
                                                         @AuthenticationPrincipal final UserContext user) {
        final NewSingleEventData eventData = mapper.mapToNewSingleEventData(request, user.getId());
        final DeclareSingleEventResult result = declareSingleEventUseCase.declareNewSingleEvent(eventData);
        return mapper.mapToDeclareSingleEventResponse(result);
    }

    @PostMapping(path = "/cyclic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @InstructorAllowed
    public DeclareCyclicEventResponse declareCyclicEvent(@RequestBody final DeclareCyclicEventRequest request,
                                                         @AuthenticationPrincipal final UserContext user) throws InvalidCyclicEventBoundariesException {
        final NewCyclicEventData eventData = mapper.mapToNewCyclicEventData(request, user.getId());
        final DeclareCyclicEventResult result = declareCyclicEventUseCase.declareNewCyclicEvent(eventData);
        return mapper.mapToDeclareCyclicEventResponse(result);
    }

    @DeleteMapping(path = "/{id}/draft")
    @InstructorAllowed
    public void deleteEventDraft(@PathVariable("id") Integer eventId,
                                 @AuthenticationPrincipal UserContext user) {
        DeleteDraftEventData data = new DeleteDraftEventData(eventId, user.getId());
        deleteDraftEventUseCase.deleteDraftEvent(data);
    }

    @PutMapping(path = "/cyclic/realization/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @InstructorAllowed
    public void updateCyclicEventRealization(@RequestBody final UpdateCyclicEventRealizationRequest request,
                                             @PathVariable("id") Integer eventRealizationId,
                                             @AuthenticationPrincipal final UserContext user)
            throws CyclicEventRealizationCollisionException, CyclicEventRealizationOutOfEventBoundException {
        UpdateCyclicEventRealizationData data = mapper.mapToUpdateCyclicEventRealizationData(request, eventRealizationId,
                user.getId());
        updateCyclicEventRealizationUseCase.updateCyclicEventRealization(data);
    }

    @PutMapping(path = "/{id}/accept")
    @InstructorAllowed
    public void acceptEvent(@PathVariable("id") final Integer eventId,
                            @AuthenticationPrincipal final UserContext user) {
        final AcceptEventData data = new AcceptEventData(eventId, user.getId());
        acceptEventUseCase.acceptEvent(data);
    }

    @PostMapping(path = "/realization/{id}/absence", consumes = MediaType.APPLICATION_JSON_VALUE)
    @InstructorAndStudentAllowed
    public void reportAbsence(@PathVariable("id") final Integer eventRealizationId,
                              @RequestBody final ReportAbsenceRequest request,
                              @AuthenticationPrincipal final UserContext user) throws EventChangedException, ConcurrentDataModificationException {
        ReportAbsenceData data = new ReportAbsenceData(eventRealizationId, request.getEventVersion(), user);
        reportAbsenceUseCase.reportAbsence(data);
    }

    @PutMapping(path = "/cyclic/{id}/{version}/resign")
    @StudentAllowed
    public void resignCyclicEvent(@PathVariable("id") final Integer cyclicEventId,
                                  @PathVariable("version") final Integer cyclicEventVersion,
                                  @AuthenticationPrincipal final UserContext user) throws EventChangedException,
            CyclicEventNoFutureRealizationsFoundException, ConcurrentDataModificationException {
        ResignCyclicEventData data = new ResignCyclicEventData(cyclicEventId, cyclicEventVersion, user.getId());
        resignCyclicEventUseCase.resignCyclicEvent(data);
    }

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @StudentAllowed
    public SearchEventsResponse searchEvents(@RequestBody final SearchEventsRequest request) {
        SearchEventsData data = mapper.mapToSearchEventsData(request);
        SearchEventsResult result = searchEventsUseCase.searchEvents(data);
        return mapper.mapToSearchEventsResponse(result);
    }

    @GetMapping(path = "/{id}/details/student", produces = MediaType.APPLICATION_JSON_VALUE)
    @StudentAllowed
    public GetEventDetailsAsStudentResponse getDetailsAsStudent(@PathVariable("id") final Integer eventId,
                                                                @AuthenticationPrincipal final UserContext user) {
        final GetEventDetailsAsStudentData data = new GetEventDetailsAsStudentData(eventId, user.getId());
        final GetEventDetailsAsStudentResult result = getEventDetailsAsStudentUseCase.getDetails(data);
        return mapper.mapToGetEventDetailsAsStudentResponse(result);
    }
}
