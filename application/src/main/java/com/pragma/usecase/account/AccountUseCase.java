package com.pragma.usecase.account;

import com.pragma.domain.account.Account;
import com.pragma.domain.account.port.out.AccountPort;
import com.pragma.domain.exceptions.AccountException;
import com.pragma.domain.exceptions.error.impl.CustomError;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.pragma.domain.exceptions.error.impl.ErrorDefinition.ACCOUNT_NOT_EXIST;

@RequiredArgsConstructor
public class AccountUseCase {

    private final AccountPort accountPort;

    public Mono<Account> statement(String numberAccount){
        return accountPort.statement(numberAccount)
                .switchIfEmpty(Mono.defer(() -> {
                        var error = new CustomError(ACCOUNT_NOT_EXIST, numberAccount);
                        return Mono.error(new AccountException(error));
                }));
    }

}
