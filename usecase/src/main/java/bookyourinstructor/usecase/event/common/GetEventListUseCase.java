package bookyourinstructor.usecase.event.common;

import bookyourinstructor.usecase.event.common.port.UserData;
import bookyourinstructor.usecase.event.common.result.GetEventListResult;
import bookyourinstructor.usecase.event.common.result.GetEventListResultItem;
import bookyourinstructor.usecase.event.common.store.EventStore;
import bookyourinstructor.usecase.util.time.TimeUtils;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class GetEventListUseCase {

    private final EventStore eventStore;
    private final TransactionFacade transactionFacade;
    private final TimeUtils timeUtils;

    public GetEventListResult getEventList(final UserData user, final boolean showPastEvents) {
        Instant now = timeUtils.nowInstant();
        return transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.READ_COMMITTED, () -> {
            List<GetEventListResultItem> items = eventStore.getEventList(user.getId(), user.getType(), now, showPastEvents);
            return GetEventListResult.of(items);
        });
    }
}
