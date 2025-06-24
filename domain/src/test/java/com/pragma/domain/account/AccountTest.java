package com.pragma.domain.account;

import com.pragma.domain.account.enums.State;
import com.pragma.domain.account.enums.Type;
import com.pragma.domain.exceptions.AccountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.pragma.domain.exceptions.error.impl.ErrorDefinition.NOT_FOUNDS_ACCOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    private static final String NUMBER_ACCOUNT = "NUMBER_ACCOUNT";

    @Test
    void shouldThrowAccountExceptionSubstracMoney(){

        var account = new Account(1, NUMBER_ACCOUNT, BigDecimal.TEN,
                Type.SAVINGS_ACCOUNT, State.ACTIVE, LocalDateTime.now(),
                LocalDateTime.now());

        var exception = assertThrows(AccountException.class, () -> account.subtractMoney(BigDecimal.valueOf(11)));
        assertEquals(NOT_FOUNDS_ACCOUNT, exception.getError());
    }

    @Test
    void shouldSubstractMoneyOK(){

        var account = new Account(1, NUMBER_ACCOUNT, BigDecimal.TEN,
                Type.SAVINGS_ACCOUNT, State.ACTIVE, LocalDateTime.now(),
                LocalDateTime.now());

        account.subtractMoney(BigDecimal.TWO);
        assertEquals(BigDecimal.valueOf(8), account.getBalance());
    }

}
