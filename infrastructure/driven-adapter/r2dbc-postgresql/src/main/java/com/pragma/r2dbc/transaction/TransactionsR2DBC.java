package com.pragma.r2dbc.transaction;

import com.pragma.domain.commons.transactions.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionsR2DBC implements Transaction {

    private final TransactionalOperator transactionalOperator;

    @Override
    public <T> Mono<T> createTransaction(Mono<T> operations) {
        return transactionalOperator.transactional(operations)
                .doOnSuccess(result -> log.info("Transaction successfully"))
                .doOnError(error -> log.error("Error performing the transaction ", error));
    }

}
