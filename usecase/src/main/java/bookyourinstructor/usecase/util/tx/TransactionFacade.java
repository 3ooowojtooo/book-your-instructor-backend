package bookyourinstructor.usecase.util.tx;

import java.util.function.Supplier;

public interface TransactionFacade {

    <T> T executeInTransaction(Supplier<T> action);
}
