package bookyourinstructor.usecase.event.schedule;

import bookyourinstructor.usecase.event.common.port.UserData;
import bookyourinstructor.usecase.event.common.store.EventScheduleStore;
import bookyourinstructor.usecase.event.schedule.result.GetEventScheduleResult;
import bookyourinstructor.usecase.event.schedule.result.GetEventScheduleResultItem;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import com.quary.bookyourinstructor.model.event.EventScheduleOwner;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class GetEventScheduleUseCase {

    private final EventScheduleStore eventScheduleStore;
    private final TransactionFacade transactionFacade;
    private final TimeUtils timeUtils;

    public GetEventScheduleResult getEventSchedule(final UserData user) {
        final Instant now = timeUtils.nowInstant();
        final EventScheduleOwner owner = mapToScheduleOwner(user);
        return transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.READ_COMMITTED, () -> {
           List<GetEventScheduleResultItem> items = eventScheduleStore.getSchedule(user.getId(), owner, now);
           return GetEventScheduleResult.of(items);
        });
    }

    private static EventScheduleOwner mapToScheduleOwner(UserData user) {
        return user.isStudent() ? EventScheduleOwner.STUDENT : EventScheduleOwner.INSTRUCTOR;
    }
}
