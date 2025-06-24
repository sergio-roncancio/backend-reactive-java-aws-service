package com.pragma.r2dbc.account.repository;

import com.pragma.domain.account.Account;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AccountRepository extends ReactiveCrudRepository<AccountEntity, Integer> {

    @Query(value = "SELECT * FROM account a WHERE a.number IN (:accountNumbers)")
    Flux<AccountEntity> findAccountsByNumbers(List<String> accountNumbers);

    @Modifying
    @Query(value = "UPDATE account SET "
                 + "    balance = b.balance, "
                 + "    modification_date = b.modification_date "
                 + "from (values "
                 + "    (:#{#accountFrom.balance}, :#{#accountFrom.modificationDate}, :#{#accountFrom.number}),"
                 + "    (:#{#accountTo.balance}, :#{#accountTo.modificationDate}, :#{#accountTo.number})"
                 + ") AS b (balance, modification_date, number_account) "
                 + "where number = b.number_account")
    Mono<Void> updateAccountsBalance(Account accountFrom, Account accountTo);

    Mono<AccountEntity> findByNumber(String number);

}
