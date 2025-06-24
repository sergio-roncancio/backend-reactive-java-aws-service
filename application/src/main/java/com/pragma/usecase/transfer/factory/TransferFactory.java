package com.pragma.usecase.transfer.factory;

import com.pragma.domain.account.Account;
import com.pragma.domain.exceptions.BankException;
import com.pragma.domain.transfer.dto.Transfer;
import com.pragma.domain.transfer.request.RequestTransfer;
import com.pragma.usecase.account.ValidationAccount;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TransferFactory {

    private final ValidationAccount validationAccount;

    public Transfer createTransfer(Account accountFrom, Account accountTo, RequestTransfer requestTransfer)
            throws BankException {

        validationAccount.validAccount(accountFrom, requestTransfer.numberAccountFrom());
        validationAccount.validAccount(accountTo, requestTransfer.numberAccountTo());

        var date = LocalDateTime.now();
        accountFrom.subtractMoney(requestTransfer.amount());
        accountFrom.setModificationDate(date);
        accountTo.addMoney(requestTransfer.amount());
        accountTo.setModificationDate(date);

        return new Transfer(accountFrom.getNumber(), accountTo.getNumber(), requestTransfer.amount(), date);
    }

}
