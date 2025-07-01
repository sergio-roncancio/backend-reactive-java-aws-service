package com.pragma.r2dbc.transfer.adapter;

import com.pragma.domain.commons.modelmapper.ModelMapperPort;
import com.pragma.domain.commons.pageable.Pageable;
import com.pragma.domain.transfer.dto.Transfer;
import com.pragma.r2dbc.transfer.repository.TransferEntity;
import com.pragma.r2dbc.transfer.repository.TransferRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferAdapterImplTest {

    @Mock
    private TransferRepository repository;
    @Mock
    private ModelMapperPort modelMapperPort;

    @InjectMocks
    private TransferAdapterImpl transferAdapter;

    @Test
    void shouldReturnErrorMakeTransfer(){
        var makeTransfer = transferAdapter.makeTransfer(null);

        StepVerifier.create(makeTransfer)
                .expectErrorSatisfies(ex -> {
                    assertInstanceOf(IllegalArgumentException.class, ex);
                })
                .verify();

    }

    @Test
    void shouldReturnIdTransferMakeTransfer(){

        final Integer idTransfer = 1;

        final String numberAccountFrom = "NUMBER-FROM";
        final String numberAccountTo = "NUMBER-TO";
        final BigDecimal amount = BigDecimal.TEN;
        final LocalDateTime date = LocalDateTime.now();

        var createTransfer = new Transfer(numberAccountFrom, numberAccountTo,
                amount, date);

        var transferEntity = new TransferEntity();
        transferEntity.setNumberAccountFrom(numberAccountFrom);
        transferEntity.setNumberAccountTo(numberAccountTo);
        transferEntity.setAmount(amount);
        transferEntity.setDate(date);

        when(modelMapperPort.mapReactive(createTransfer, TransferEntity.class))
                .thenReturn(Mono.just(transferEntity));
        when(repository.save(transferEntity)).thenAnswer(invocation -> {
            var transferEntitySaved = invocation.getArgument(0, TransferEntity.class);
            transferEntitySaved.setId(idTransfer);
            return Mono.just(transferEntitySaved);
        });

        var makeTransfer = transferAdapter.makeTransfer(createTransfer);

        StepVerifier.create(makeTransfer)
                .expectNextMatches(id -> {
                    assertEquals(idTransfer, id);
                    return true;
                }).verifyComplete();

    }

    @ParameterizedTest
    @MethodSource("provideInvalidArgumentsGetMovements")
    void shouldReturnEmptyGetMovements(String numberAccount, Pageable pageable){
        var movements = transferAdapter.getMovements(numberAccount, pageable);

        StepVerifier.create(movements)
                .verifyComplete();

        verify(repository, never()).historicalTransfers(anyString(),
                any(org.springframework.data.domain.Pageable.class));

    }

    private static Stream<Arguments> provideInvalidArgumentsGetMovements() {
        final String numberAccount = "NUMBER-ACCOUNT";
        return Stream.of(Arguments.of(null, null),
                Arguments.of(numberAccount, null),
                Arguments.of(numberAccount, new Pageable(0,0)),
                        Arguments.of(numberAccount, new Pageable(0,-1))
        );
    }

    @Test
    void shouldReturnMovementsGetMovements(){

        final String numberAccount1 = "NUMBER-ACCOUNT-1";
        final Pageable pageable = new Pageable(5, 0);

        var transfer1 = new TransferEntity(1, BigDecimal.TEN, numberAccount1, "NUMBER-ACCOUNT-2",
                LocalDateTime.now());
        var transfer2 = new TransferEntity(2, BigDecimal.ONE, "NUMBER-ACCOUNT-2", numberAccount1,
                LocalDateTime.now());

        var transfers = List.of(transfer1, transfer2);

        when(repository.historicalTransfers(eq(numberAccount1),
                any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(Flux.fromIterable(transfers));

        var movements = transferAdapter.getMovements(numberAccount1, pageable);

        StepVerifier.create(movements)
                .expectNextMatches(transfer -> {
                    assertEquals(transfer1.getNumberAccountFrom(), transfer.numberAccountFrom());
                    assertEquals(transfer1.getNumberAccountTo(), transfer.numberAccountTo());
                    assertEquals(transfer1.getAmount(), transfer.amount());
                    assertEquals(transfer1.getDate(), transfer.date());
                    return true;
                })
                .expectNextMatches(transfer -> {
                    assertEquals(transfer2.getNumberAccountFrom(), transfer.numberAccountFrom());
                    assertEquals(transfer2.getNumberAccountTo(), transfer.numberAccountTo());
                    assertEquals(transfer2.getAmount(), transfer.amount());
                    assertEquals(transfer2.getDate(), transfer.date());
                    return true;
                })
                .verifyComplete();
    }


}
