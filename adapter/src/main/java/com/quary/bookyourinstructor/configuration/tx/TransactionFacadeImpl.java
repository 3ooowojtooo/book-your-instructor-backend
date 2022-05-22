package com.quary.bookyourinstructor.configuration.tx;

import bookyourinstructor.usecase.util.tx.TransactionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TransactionFacadeImpl implements TransactionFacade {

    private final PlatformTransactionManager platformTransactionManager;

    @Override
    public <T> T executeInTransaction(final Supplier<T> action) {
        TransactionTemplate template = new TransactionTemplate(platformTransactionManager);
        return template.execute(status -> action.get());
    }

}
