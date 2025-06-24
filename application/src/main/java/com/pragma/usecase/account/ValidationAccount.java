package com.pragma.usecase.account;

import com.pragma.domain.account.Account;
import com.pragma.domain.exceptions.AccountException;
import com.pragma.domain.exceptions.TransferException;
import com.pragma.domain.exceptions.error.Error;
import com.pragma.domain.exceptions.error.impl.CustomError;
import reactor.core.publisher.Mono;

import static com.pragma.domain.exceptions.error.impl.ErrorDefinition.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class ValidationAccount {

    public Mono<Void> validNumberAccountsAreEquals(String numberAccountFrom,
                                              String numberAccountTo) throws TransferException {
        if(isBlank(numberAccountFrom) || isBlank(numberAccountTo)){
            return Mono.error(new TransferException(SOME_ACCOUNT_NUMBER_IS_NULL_OR_BLANK));
        }
        if (numberAccountFrom.equals(numberAccountTo)) {
            return Mono.error(new TransferException(ACCOUNT_CAN_NOT_EQUALS));
        }
        return Mono.empty();
    }

    public void validAccount(Account account, String accountNumberSearched)
            throws AccountException {
        if(account == null){
            throw createTransferException(ACCOUNT_NOT_EXIST, accountNumberSearched);
        }
        if(!account.isActive()){
            throw createTransferException(ACCOUNT_IS_INACTIVE, accountNumberSearched);
        }
    }

    private AccountException createTransferException(Error error, String accountNumberSearched){
        var customError = new CustomError(error, accountNumberSearched);
        return new AccountException(customError);
    }

}
