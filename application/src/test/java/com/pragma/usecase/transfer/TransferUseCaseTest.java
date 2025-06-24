package com.pragma.usecase.transfer;

import com.pragma.domain.account.Account;
import com.pragma.domain.account.enums.State;
import com.pragma.domain.account.enums.Type;
import com.pragma.domain.account.port.out.AccountPort;
import com.pragma.domain.commons.transactions.Transaction;
import com.pragma.domain.transfer.dto.Transfer;
import com.pragma.domain.transfer.port.out.TransferPort;
import com.pragma.domain.transfer.request.RequestTransfer;
import com.pragma.usecase.account.ValidationAccount;
import com.pragma.usecase.transfer.factory.TransferFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferUseCaseTest {

    @Mock
    private TransferPort transferPort;
    @Mock
    private AccountPort accountPort;
    @Mock
    private Transaction transaction;
    @Mock
    private TransferFactory transferFactory;
    @Mock
    private ValidationAccount validationAccount;

    @InjectMocks
    private TransferUseCase transferUseCase;

    @Test
    void shouldMakeTransfer(){

        var numberAccountFrom = "ACCOUNT-FROM";
        var numberAccountTo = "ACCOUNT-TO";
        var amount = BigDecimal.TEN;
        var idTransfer = 5;

        var requestTransfer = new RequestTransfer(numberAccountFrom, numberAccountTo, amount);

        var accountFrom = new Account(1, numberAccountFrom, BigDecimal.TEN,
                Type.SAVINGS_ACCOUNT, State.ACTIVE, LocalDateTime.now(),
                LocalDateTime.now());

        var accountTo = new Account(2, numberAccountTo, BigDecimal.TWO,
                Type.CHECKING_ACCOUNT, State.ACTIVE, LocalDateTime.now(),
                LocalDateTime.now());

        var mapAccounts = Map.of(numberAccountFrom, accountFrom,
                numberAccountTo, accountTo);

        var transfer = new Transfer(numberAccountFrom, numberAccountTo, amount, LocalDateTime.now());

        var idTransferMono = Mono.just(idTransfer);

        when(validationAccount.validNumberAccountsAreEquals(numberAccountFrom, numberAccountTo))
                .thenReturn(Mono.empty());
        when(accountPort.findAccountsByNumbers(List.of(numberAccountFrom, numberAccountTo)))
                .thenReturn(Mono.defer(() -> Mono.just(mapAccounts)));
        when(transferFactory.createTransfer(accountFrom, accountTo, requestTransfer))
                .thenAnswer(invocation -> {
                    var accountFromModify = invocation.getArgument(0, Account.class);
                    accountFromModify.subtractMoney(amount);
                    return transfer;
                });

        when(accountPort.updateAccountsBalance(accountFrom, accountTo))
                .thenReturn(Mono.empty());
        when(transferPort.makeTransfer(transfer)).thenReturn(idTransferMono);
        when(transaction.createTransaction(Mockito.<Mono<Integer>>any())).thenReturn(idTransferMono);

        var trasferResponse = transferUseCase.makeTransfer(requestTransfer);

        StepVerifier.create(trasferResponse)
                .expectNextMatches(transferCreated -> {
                    assertEquals(idTransfer, transferCreated.idTransfer());

                    var newCurrentBalanceAccountFrom = BigDecimal.ZERO;

                    var source = transferCreated.source();
                    assertEquals(numberAccountFrom, source.numberAccountFrom());
                    assertEquals(newCurrentBalanceAccountFrom, source.currentBalance());

                    var destination = transferCreated.destination();
                    assertEquals(numberAccountTo, destination.numberAccountTo());
                    assertEquals(amount, destination.transferValue());

                    return true;
                })
                .verifyComplete();

    }

}
