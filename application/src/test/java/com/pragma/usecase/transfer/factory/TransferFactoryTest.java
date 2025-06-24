package com.pragma.usecase.transfer.factory;

import com.pragma.domain.account.Account;
import com.pragma.domain.account.enums.State;
import com.pragma.domain.account.enums.Type;
import com.pragma.domain.transfer.request.RequestTransfer;
import com.pragma.usecase.account.ValidationAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TransferFactoryTest {

    @Mock
    private ValidationAccount validationAccount;
    @InjectMocks
    private TransferFactory transferFactory;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<String> numberAccountCaptor;

    @Test
    void shouldCreateTransfer(){
        final String numberAccountFrom = "ACCOUNT-FROM";
        final String numberAccountTo = "ACCOUNT-TO";
        final BigDecimal amount = BigDecimal.TEN;

        var requestTransfer = new RequestTransfer(numberAccountFrom, numberAccountTo, amount);
        var accountFrom = new Account(1, numberAccountFrom, BigDecimal.TEN, Type.SAVINGS_ACCOUNT,
                State.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
        var accountTo = new Account(2, numberAccountTo,BigDecimal.TWO, Type.SAVINGS_ACCOUNT,
                State.ACTIVE, LocalDateTime.now(), LocalDateTime.now());

        doNothing().when(validationAccount)
                .validAccount(any(Account.class), anyString());

        var transfer = transferFactory.createTransfer(accountFrom, accountTo, requestTransfer);

        assertEquals(BigDecimal.TEN.add(BigDecimal.TWO), accountTo.getBalance());
        assertEquals(BigDecimal.ZERO, accountFrom.getBalance());

        assertEquals(numberAccountFrom, transfer.numberAccountFrom());
        assertEquals(numberAccountTo, transfer.numberAccountTo());
        assertEquals(amount, transfer.amount());

        verify(validationAccount, times(2))
                .validAccount(accountCaptor.capture(), numberAccountCaptor.capture());

        var accounts = accountCaptor.getAllValues();

        var accountFromValid = accounts.getFirst();
        assertEquals(accountFrom, accounts.getFirst());
        assertEquals(accountTo, accounts.getLast());

        var numberAccountsValid = numberAccountCaptor.getAllValues();
        assertEquals(numberAccountFrom, numberAccountsValid.getFirst());
        assertEquals(numberAccountTo, numberAccountsValid.getLast());
    }

}
