package com.pragma.usecase.transfer;

import com.pragma.domain.account.Account;
import com.pragma.domain.account.enums.State;
import com.pragma.domain.account.enums.Type;
import com.pragma.domain.commons.pageable.Pageable;
import com.pragma.domain.transfer.dto.Transfer;
import com.pragma.domain.transfer.port.out.TransferPort;
import com.pragma.domain.transfer.request.RequestMovement;
import com.pragma.usecase.account.AccountUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementsUseCaseTest {

    @Mock
    private TransferPort transferPort;
    @Mock
    private AccountUseCase accountUseCase;

    @InjectMocks
    private MovementsUseCase movementsUseCase;

    @Test
    void shouldListMovements(){
        final String numberAccount = "NUMBER-ACCOUNT";
        final String numberAccountTwo = "NUMBER-ACCOUNT-2";

        var pageable = new Pageable(0, 5);
        var requestMovement = new RequestMovement(pageable, numberAccount);

        var account = new Account(1, numberAccount, BigDecimal.TEN,
                Type.SAVINGS_ACCOUNT, State.ACTIVE, LocalDateTime.now(),
                LocalDateTime.now());

        var movementsOne = new Transfer(numberAccount, numberAccountTwo, BigDecimal.TWO, LocalDateTime.now());
        var movementsTwo = new Transfer(numberAccountTwo, numberAccount, BigDecimal.TEN, LocalDateTime.now());

        Flux<Transfer> movements = Flux.just(movementsOne, movementsTwo);

        when(accountUseCase.statement(numberAccount)).thenReturn(Mono.just(account));
        when(transferPort.getMovements(numberAccount, pageable)).thenReturn(movements);

        var listMovements = movementsUseCase.listMovements(requestMovement);

        StepVerifier.create(listMovements)
                .expectNextMatches(movementsResponse ->{

                    assertEquals(pageable, movementsResponse.pageable());

                    var accountInfo = movementsResponse.accountInfo();
                    assertEquals(account.getNumber(), accountInfo.numberAccount());
                    assertEquals(account.getBalance(), accountInfo.currentBalance());

                    var movementsList = movementsResponse.movementsLists();
                    assertEquals(2, movementsList.size());

                    var firstMovement = movementsList.getFirst();
                    assertEquals(numberAccountTwo, firstMovement.accountNumber());
                    assertEquals(movementsOne.amount().multiply(BigDecimal.valueOf(-1)), firstMovement.amount());
                    assertEquals(movementsOne.date(), firstMovement.date());

                    var secondMovement = movementsList.getLast();
                    assertEquals(numberAccountTwo, secondMovement.accountNumber());
                    assertEquals(movementsTwo.amount(), secondMovement.amount());
                    assertEquals(movementsTwo.date(), secondMovement.date());

                    return true;
                }).verifyComplete();

    }


}
