package com.quary.bookyourinstructor.controller.event;

import bookyourinstructor.usecase.event.booklock.ConfirmEventBookLockData;
import bookyourinstructor.usecase.event.booklock.ConfirmEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.CreateEventBookLockData;
import bookyourinstructor.usecase.event.booklock.CreateEventBookLockUseCase;
import bookyourinstructor.usecase.event.cyclic.result.DeclareCyclicEventResult;
import bookyourinstructor.usecase.event.cyclic.DeclareCyclicEventUseCase;
import bookyourinstructor.usecase.event.cyclic.data.NewCyclicEventData;
import bookyourinstructor.usecase.event.single.DeclareSingleEventResult;
import bookyourinstructor.usecase.event.single.DeclareSingleEventUseCase;
import bookyourinstructor.usecase.event.single.NewSingleEventData;
import com.quary.bookyourinstructor.configuration.security.annotation.InstructorAllowed;
import com.quary.bookyourinstructor.configuration.security.annotation.StudentAllowed;
import com.quary.bookyourinstructor.configuration.security.model.UserContext;
import com.quary.bookyourinstructor.controller.event.mapper.EventMapper;
import com.quary.bookyourinstructor.controller.event.request.DeclareCyclicEventRequest;
import com.quary.bookyourinstructor.controller.event.request.DeclareSingleEventRequest;
import com.quary.bookyourinstructor.controller.event.response.CreateEventBookLockResponse;
import com.quary.bookyourinstructor.controller.event.response.DeclareCyclicEventResponse;
import com.quary.bookyourinstructor.controller.event.response.DeclareSingleEventResponse;
import com.quary.bookyourinstructor.model.event.EventLock;
import com.quary.bookyourinstructor.model.event.exception.EventBookLockExpiredException;
import com.quary.bookyourinstructor.model.event.exception.EventBookingAlreadyLockedException;
import com.quary.bookyourinstructor.model.event.exception.EventChangedException;
import com.quary.bookyourinstructor.model.event.exception.InvalidCyclicEventBoundariesException;
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

    @PostMapping(path = "/{id}/{version}/book-lock", produces = MediaType.APPLICATION_JSON_VALUE)
    @StudentAllowed
    public CreateEventBookLockResponse createEventBookLock(@PathVariable("id") final Integer eventId,
                                                           @PathVariable("version") final Integer eventVersion,
                                                           @AuthenticationPrincipal final UserContext user) throws EventChangedException, EventBookingAlreadyLockedException {
        final CreateEventBookLockData data = new CreateEventBookLockData(eventId, eventVersion, user.getId());
        final EventLock eventLock = createEventBookLockUseCase.createEventBookLock(data);
        return mapper.mapToCreateEventBookLockResponse(eventLock);
    }

    @PutMapping(path = "/book-lock/{id}/confirm")
    @StudentAllowed
    public void confirmBookLock(@PathVariable("id") final Integer bookLockId,
                                @AuthenticationPrincipal final UserContext user) throws EventChangedException, EventBookLockExpiredException {
        ConfirmEventBookLockData data = new ConfirmEventBookLockData(bookLockId, user.getId());
        confirmEventBookLockUseCase.confirmEventBookLock(data);
    }
}
