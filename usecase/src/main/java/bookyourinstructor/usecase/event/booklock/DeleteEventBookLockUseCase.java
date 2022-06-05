package bookyourinstructor.usecase.event.booklock;

import bookyourinstructor.usecase.event.booklock.data.DeleteEventBookLockData;
import bookyourinstructor.usecase.event.common.store.EventLockStore;
import bookyourinstructor.usecase.util.tx.TransactionFacade;
import bookyourinstructor.usecase.util.tx.TransactionIsolation;
import bookyourinstructor.usecase.util.tx.TransactionPropagation;
import com.quary.bookyourinstructor.model.event.EventLock;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@RequiredArgsConstructor
public class DeleteEventBookLockUseCase {

    private final EventLockStore eventLockStore;
    private final TransactionFacade transactionFacade;

    public void deleteBookLock(DeleteEventBookLockData data) {
        transactionFacade.executeInTransaction(TransactionPropagation.REQUIRED, TransactionIsolation.READ_COMMITTED, () -> {
            Optional<EventLock> eventLockOpt = eventLockStore.findById(data.getBookLockId());
            if (eventLockOpt.isPresent()) {
                EventLock eventLock = eventLockOpt.get();
                validateEventLockOwner(eventLock, data.getStudentId());
                eventLockStore.deleteById(data.getBookLockId());
            }
            return null;
        });
    }

    private static void validateEventLockOwner(EventLock eventLock, Integer ownerId) {
        checkArgument(Objects.equals(eventLock.getUserId(), ownerId), "You can only delete yours book lock");
    }
}
