package com.pragma.usecase.account;

import com.pragma.domain.account.Account;
import com.pragma.domain.account.enums.State;
import com.pragma.domain.account.enums.Type;
import com.pragma.domain.exceptions.AccountException;
import com.pragma.domain.exceptions.TransferException;
import com.pragma.domain.exceptions.error.Error;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.pragma.domain.exceptions.error.impl.ErrorDefinition.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidationAccountTest {

    @InjectMocks
    private ValidationAccount validationAccount;

    private static final String ACCOUNT_NUMBER_FROM = "ACCOUNT_FROM";
    private static final String ACCOUNT_NUMBER_TO = "ACCOUNT_TO";

    @ParameterizedTest
    @MethodSource("provideInvalidAccountNumbers")
    void shouldReturnErrorAccountNumbersEquals(String numberAccountFrom,
                                               String numberAccountTo, Error errorExcepted) {
        var validNumberAccountsAreEquals = validationAccount.
                validNumberAccountsAreEquals(numberAccountFrom, numberAccountTo);

        StepVerifier.create(validNumberAccountsAreEquals)
                .expectErrorSatisfies(err -> {
                    assertInstanceOf(TransferException.class, err);
                    TransferException accountException = (TransferException) err;
                    assertEquals(errorExcepted, accountException.getError());
                });
    }

    private static Stream<Arguments> provideInvalidAccountNumbers() {
        return Stream.of(
                Arguments.of(null, null, SOME_ACCOUNT_NUMBER_IS_NULL_OR_BLANK),
                Arguments.of(ACCOUNT_NUMBER_FROM, null, SOME_ACCOUNT_NUMBER_IS_NULL_OR_BLANK),
                Arguments.of(ACCOUNT_NUMBER_FROM, ACCOUNT_NUMBER_FROM, ACCOUNT_CAN_NOT_EQUALS)
        );
    }

    @Test
    void shouldReturnEmptyAccountNumbersDiferent() {
        var validNumberAccountsAreDiferent = validationAccount.
                validNumberAccountsAreEquals(ACCOUNT_NUMBER_FROM, ACCOUNT_NUMBER_TO);

        StepVerifier.create(validNumberAccountsAreDiferent)
                .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource("provideInvalidAccount")
    void shouldInvalidAccount(Account account, String accountNumber, Error expectedError) {

        var exception = assertThrows(AccountException.class, () -> validationAccount.validAccount(account, accountNumber));
        var error = exception.getError();
        assertEquals(expectedError.getCode(), error.getCode());
        assertEquals(String.format(expectedError.getMessage(), accountNumber), error.getMessage());
    }

    private static Stream<Arguments> provideInvalidAccount() {
        return Stream.of(
                Arguments.of(null, ACCOUNT_NUMBER_FROM, ACCOUNT_NOT_EXIST),
                Arguments.of(new Account(1, ACCOUNT_NUMBER_FROM, BigDecimal.ONE, Type.SAVINGS_ACCOUNT,
                        State.INACTIVE, LocalDateTime.now(), LocalDateTime.now()), ACCOUNT_NUMBER_FROM, ACCOUNT_IS_INACTIVE)
        );
    }

}
