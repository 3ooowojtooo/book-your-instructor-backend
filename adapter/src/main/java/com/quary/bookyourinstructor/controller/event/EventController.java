package com.quary.bookyourinstructor.controller.event;

import bookyourinstructor.usecase.event.cyclic.DeclareCyclicEventUseCase;
import bookyourinstructor.usecase.event.single.DeclareSingleEventUseCase;
import com.quary.bookyourinstructor.configuration.security.annotation.InstructorAllowed;
import com.quary.bookyourinstructor.configuration.security.model.UserContext;
import com.quary.bookyourinstructor.controller.event.mapper.EventMapper;
import com.quary.bookyourinstructor.controller.event.request.DeclareCyclicEventRequest;
import com.quary.bookyourinstructor.controller.event.request.DeclareSingleEventRequest;
import com.quary.bookyourinstructor.controller.event.response.DeclareCyclicEventResponse;
import com.quary.bookyourinstructor.controller.event.response.DeclareSingleEventResponse;
import com.quary.bookyourinstructor.model.event.EventRealization;
import com.quary.bookyourinstructor.model.event.NewCyclicEventData;
import com.quary.bookyourinstructor.model.event.NewSingleEventData;
import com.quary.bookyourinstructor.model.event.exception.InvalidCyclicEventBoundariesException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventMapper mapper;
    private final DeclareSingleEventUseCase declareSingleEventUseCase;
    private final DeclareCyclicEventUseCase declareCyclicEventUseCase;

    @PostMapping(path = "/single", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @InstructorAllowed
    public DeclareSingleEventResponse declareSingleEvent(@RequestBody final DeclareSingleEventRequest request,
                                                         @AuthenticationPrincipal final UserContext user) {
        final NewSingleEventData eventData = mapper.mapToNewSingleEventData(request, user.getId());
        final EventRealization eventRealization = declareSingleEventUseCase.declareNewSingleEvent(eventData);
        return mapper.mapToDeclareSingleEventResponse(eventRealization);
    }

    @PostMapping(path = "/cyclic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @InstructorAllowed
    public DeclareCyclicEventResponse declareCyclicEvent(@RequestBody final DeclareCyclicEventRequest request,
                                                         @AuthenticationPrincipal final UserContext user) throws InvalidCyclicEventBoundariesException {
        final NewCyclicEventData eventData = mapper.mapToNewCyclicEventData(request, user.getId());
        final List<EventRealization> eventRealizations = declareCyclicEventUseCase.declareNewCyclicEvent(eventData);
        return mapper.mapToDeclareCyclicEventResponse(eventRealizations);
    }
}
