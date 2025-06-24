package com.pragma.usecase.account;

import com.pragma.domain.account.Account;
import com.pragma.domain.account.enums.State;
import com.pragma.domain.account.enums.Type;
import com.pragma.domain.account.port.out.AccountPort;
import com.pragma.domain.exceptions.AccountException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.pragma.domain.exceptions.error.impl.ErrorDefinition.ACCOUNT_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountUseCaseTest {

    @Mock
    private AccountPort accountPort;

    @InjectMocks
    private AccountUseCase accountUseCase;

    private static final String NUMBER_ACCOUNT = "NUMBER-ACCOUNT";

    @Test
    void shouldReturnAccount(){

        var account = new Account(1, NUMBER_ACCOUNT, BigDecimal.TEN,
                Type.SAVINGS_ACCOUNT, State.ACTIVE, LocalDateTime.now(),
                LocalDateTime.now());

        Mono<Account> monoAccount = Mono.just(account);

        when(accountPort.statement(NUMBER_ACCOUNT)).thenReturn(monoAccount);

        var accountStatement = accountUseCase.statement(NUMBER_ACCOUNT);

        StepVerifier.create(accountStatement)
                .expectNextMatches(accountReturned -> {
                    assertEquals(account.getId(), accountReturned.getId());
                    assertEquals(account.getNumber(), accountReturned.getNumber());
                    assertEquals(account.getBalance(), accountReturned.getBalance());
                    assertEquals(account.getType(), accountReturned.getType());
                    assertEquals(account.getState(), accountReturned.getState());
                    assertEquals(account.getModificationDate(), accountReturned.getModificationDate());
                    assertEquals(account.getCreationDate(), accountReturned.getCreationDate());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenAccountNotFound(){
        when(accountPort.statement(NUMBER_ACCOUNT)).thenReturn(Mono.empty());

        var accountStatement = accountUseCase.statement(NUMBER_ACCOUNT);

        StepVerifier.create(accountStatement)
                .expectErrorSatisfies(ex -> {
                    assertInstanceOf(AccountException.class, ex);
                    AccountException accountException = (AccountException) ex;
                    var error = accountException.getError();
                    assertEquals(ACCOUNT_NOT_EXIST.getCode(), error.getCode());
                    assertEquals(String.format(ACCOUNT_NOT_EXIST.getMessage() , NUMBER_ACCOUNT), error.getMessage());
                }).verify();

    }

}
