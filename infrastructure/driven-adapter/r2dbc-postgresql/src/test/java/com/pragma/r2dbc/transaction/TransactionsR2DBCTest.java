package com.pragma.r2dbc.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionsR2DBCTest {

    @Mock
    private TransactionalOperator transactionalOperator;

    @InjectMocks
    private TransactionsR2DBC transactionsR2DBC;

    @Test
    void shouldCreateTransaction(){

        var responseTransaction = 6;

        var operations = Mono.just(3)
                .map(i -> i * 2);

        when(transactionalOperator.transactional(Mockito.<Mono<Integer>>any()))
                .thenReturn(Mono.just(responseTransaction));

        var transaction = transactionsR2DBC.createTransaction(operations);

        StepVerifier.create(transaction)
                .expectNextMatches(i -> {
                    assertEquals(6, i);
                    return true;
                })
                .verifyComplete();

    }

    @Test
    void shouldGenerateErrorCreateTransaction(){

        var operations = Mono.just(3)
                .map(i -> i * 2);

        when(transactionalOperator.transactional(Mockito.<Mono<Integer>>any()))
                .thenReturn(Mono.error(new RuntimeException("Error transaction")));

        var transaction = transactionsR2DBC.createTransaction(operations);

        StepVerifier.create(transaction)
                .expectErrorSatisfies(ex ->{
                    assertInstanceOf(RuntimeException.class, ex);
                }).verify();
    }

}
