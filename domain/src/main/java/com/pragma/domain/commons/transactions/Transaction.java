package com.pragma.domain.commons.transactions;

import reactor.core.publisher.Mono;

public interface Transaction {

    <T> Mono<T> createTransaction(Mono<T> operations);

}
