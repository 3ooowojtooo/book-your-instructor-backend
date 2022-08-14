package com.quary.bookyourinstructor.controller.booklock;

import bookyourinstructor.usecase.event.booklock.ConfirmEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.CreateEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.DeleteEventBookLockUseCase;
import bookyourinstructor.usecase.event.booklock.data.ConfirmEventBookLockData;
import bookyourinstructor.usecase.event.booklock.data.CreateEventBookLockData;
import bookyourinstructor.usecase.event.booklock.data.DeleteEventBookLockData;
import com.quary.bookyourinstructor.configuration.security.annotation.StudentAllowed;
import com.quary.bookyourinstructor.configuration.security.model.UserPrincipal;
import com.quary.bookyourinstructor.controller.booklock.mapper.BookLockMapper;
import com.quary.bookyourinstructor.controller.booklock.request.CreateEventBookLockRequest;
import com.quary.bookyourinstructor.controller.booklock.response.CreateEventBookLockResponse;
import com.quary.bookyourinstructor.model.event.EventLock;
import com.quary.bookyourinstructor.model.event.exception.ConcurrentDataModificationException;
import com.quary.bookyourinstructor.model.event.exception.EventBookLockExpiredException;
import com.quary.bookyourinstructor.model.event.exception.EventBookingAlreadyLockedException;
import com.quary.bookyourinstructor.model.event.exception.EventChangedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book-lock")
@RequiredArgsConstructor
public class BookLockController {

    private final BookLockMapper mapper;
    private final CreateEventBookLockUseCase createEventBookLockUseCase;
    private final ConfirmEventBookLockUseCase confirmEventBookLockUseCase;
    private final DeleteEventBookLockUseCase deleteEventBookLockUseCase;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @StudentAllowed
    public CreateEventBookLockResponse createEventBookLock(@RequestBody final CreateEventBookLockRequest request,
                                                           @AuthenticationPrincipal final UserPrincipal user)
            throws EventChangedException, EventBookingAlreadyLockedException, ConcurrentDataModificationException {
        final CreateEventBookLockData data = mapper.mapToCreateEventBookLockData(request, user.getId());
        final EventLock eventLock = createEventBookLockUseCase.createEventBookLock(data);
        return mapper.mapToCreateEventBookLockResponse(eventLock);
    }

    @PutMapping(path = "/{id}/confirm")
    @StudentAllowed
    public void confirmBookLock(@PathVariable("id") final Integer bookLockId,
                                @AuthenticationPrincipal final UserPrincipal user) throws EventChangedException,
            EventBookLockExpiredException, ConcurrentDataModificationException {
        ConfirmEventBookLockData data = new ConfirmEventBookLockData(bookLockId, user.getId());
        confirmEventBookLockUseCase.confirmEventBookLock(data);
    }

    @DeleteMapping(path = "/{id}")
    @StudentAllowed
    public void removeBookLock(@PathVariable("id") final Integer bookLockId,
                               @AuthenticationPrincipal final UserPrincipal user) {
        DeleteEventBookLockData data = new DeleteEventBookLockData(bookLockId, user.getId());
        deleteEventBookLockUseCase.deleteBookLock(data);
    }
}
