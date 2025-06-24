package com.pragma.domain.account.port.out;

import com.pragma.domain.account.Account;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface AccountPort {

    Mono<Map<String, Account>> findAccountsByNumbers(List<String> numbers);
    Mono<Account> statement(String numberAccount);
    Mono<Void> updateAccountsBalance(Account accountFrom, Account accountTo);

}
