package com.pragma.r2dbc.account.adapter;

import com.pragma.domain.account.Account;
import com.pragma.domain.account.enums.State;
import com.pragma.domain.account.enums.Type;
import com.pragma.domain.commons.modelmapper.ModelMapperPort;
import com.pragma.r2dbc.account.repository.AccountEntity;
import com.pragma.r2dbc.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AccountAdapterImplTest {

    @Mock
    private AccountRepository repository;
    @Mock
    private ModelMapperPort modelMapperPort;

    @InjectMocks
    private AccountAdapterImpl accountAdapter;

    @ParameterizedTest
    @MethodSource("provideInvalidListAccountNumber")
    void shouldReturnEmptyfindAccountsByNumbers(List<String> numbers){
        var findAccountByNumbers = accountAdapter.findAccountsByNumbers(numbers);

        StepVerifier.create(findAccountByNumbers)
                .verifyComplete();

        verify(repository, never()).findAccountsByNumbers(Mockito.<List<String>>any());
        verify(modelMapperPort, never()).map(any(AccountEntity.class), eq(Account.class));

    }

    private static Stream<Arguments> provideInvalidListAccountNumber() {
        return Stream.of(null, Arguments.of(new ArrayList<String>()));
    }

    @Test
    void shouldMapAccountfindAccountsByNumbers(){

        final String numberAccount1 = "NUMBER-1";
        final String numberAccount2 = "NUMBER-2";
        var numbersAccounts = List.of(numberAccount1, numberAccount2);

        var accountEntity1 = new AccountEntity();
        accountEntity1.setNumber(numberAccount1);

        var accountEntity2 = new AccountEntity();
        accountEntity2.setNumber(numberAccount2);

        var fluxAccounts = Flux.just(accountEntity1, accountEntity2);

        var account1 = new Account(1, numberAccount1, BigDecimal.TEN,
                Type.SAVINGS_ACCOUNT, State.ACTIVE, LocalDateTime.now(),
                LocalDateTime.now());
        var account2 = new Account(2, numberAccount2, BigDecimal.TWO,
                Type.SAVINGS_ACCOUNT, State.ACTIVE, LocalDateTime.now(),
                LocalDateTime.now());

        when(repository.findAccountsByNumbers(numbersAccounts))
                .thenReturn(fluxAccounts);

        when(modelMapperPort.map(any(AccountEntity.class), eq(Account.class)))
                .thenReturn(account1)
                .thenReturn(account2);

        var findAccountByNumbers = accountAdapter.findAccountsByNumbers(numbersAccounts);

        StepVerifier.create(findAccountByNumbers)
                .expectNextMatches(accounts -> {

                    assertEquals(numbersAccounts.size(), accounts.size());
                    assertTrue(accounts.containsKey(numberAccount1));
                    assertTrue(accounts.containsKey(numberAccount2));

                    assertEquals(accounts.get(numberAccount1), account1);
                    assertEquals(accounts.get(numberAccount2), account2);

                    return true;
                })
                .verifyComplete();

    }

    @ParameterizedTest
    @MethodSource("provideInvalidAccountNumber")
    void shouldReturnEmptyStatement(String numberAccount){

        var findAccountByNumbers = accountAdapter.statement(numberAccount);

        StepVerifier.create(findAccountByNumbers)
                .verifyComplete();

        verify(repository, never()).findByNumber(anyString());
        verify(modelMapperPort, never()).mapReactive(any(AccountEntity.class), eq(Account.class));

    }

    private static Stream<Arguments> provideInvalidAccountNumber() {
        return Stream.of(null, Arguments.of(""), Arguments.of(" "));
    }

    @Test
    void shouldReturnStatement(){

        final String numberAccount = "NUMBER";

        var accountEntity = new AccountEntity();
        accountEntity.setNumber(numberAccount);

        var account = new Account(1, numberAccount, BigDecimal.TEN,
                Type.SAVINGS_ACCOUNT, State.ACTIVE, LocalDateTime.now(),
                LocalDateTime.now());

        when(repository.findByNumber(numberAccount)).thenReturn(Mono.just(accountEntity));
        when(modelMapperPort.mapReactive(accountEntity, Account.class)).thenReturn(Mono.just(account));

        var statement = accountAdapter.statement(numberAccount);

        StepVerifier.create(statement)
                .expectNextMatches(state -> {
                    assertEquals(account, state);
                    return true;
                })
                .verifyComplete();

    }

    @ParameterizedTest
    @MethodSource("provideInvalidAccountsBalance")
    void shouldReturnErrorUpdateAccountsBalance(Account accountFrom, Account accountTo){
        var findAccountByNumbers = accountAdapter.updateAccountsBalance(accountFrom, accountTo);

        StepVerifier.create(findAccountByNumbers)
                        .expectErrorSatisfies(ex -> {
                            assertInstanceOf(IllegalArgumentException.class, ex);
                        }).verify();

        verify(repository, never()).updateAccountsBalance(any(Account.class), any(Account.class));

    }

    private static Stream<Arguments> provideInvalidAccountsBalance() {
        return Stream.of(Arguments.of(null, null),
                Arguments.of(new Account(), null),
                Arguments.of(new Account(), new Account()));
    }

    @Test
    void shouldUpdateAccountsBalance(){
        var accountFrom = new Account(1, "NUMBER-FROM", BigDecimal.TEN,
                Type.SAVINGS_ACCOUNT, State.ACTIVE, LocalDateTime.now(),
                LocalDateTime.now());

        var accountTo = new Account(2, "NUMBER-TO", BigDecimal.TWO,
                Type.SAVINGS_ACCOUNT, State.ACTIVE, LocalDateTime.now(),
                LocalDateTime.now());

        when(repository.updateAccountsBalance(accountFrom, accountTo)).thenReturn(Mono.empty());

        var findAccountByNumbers = accountAdapter.updateAccountsBalance(accountFrom, accountTo);

        StepVerifier.create(findAccountByNumbers)
                .verifyComplete();

    }

}
