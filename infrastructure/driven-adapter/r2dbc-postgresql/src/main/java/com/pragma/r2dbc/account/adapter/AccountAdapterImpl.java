package com.pragma.r2dbc.account.adapter;

import com.pragma.domain.account.Account;
import com.pragma.domain.account.port.out.AccountPort;
import com.pragma.domain.commons.modelmapper.ModelMapperPort;
import com.pragma.r2dbc.account.repository.AccountEntity;
import com.pragma.r2dbc.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountAdapterImpl implements AccountPort {

    private final AccountRepository repository;
    private final ModelMapperPort modelMapperPort;

    @Override
    public Mono<Map<String, Account>> findAccountsByNumbers(List<String> numbers) {
        if(numbers == null || numbers.isEmpty()) {
            return Mono.empty();
        }
        return repository.findAccountsByNumbers(numbers)
                .collectList()
                .flatMap(accountsEntities ->
                    Mono.just(accountsEntities.stream()
                            .collect(Collectors.toMap(AccountEntity::getNumber,
                                    accountEntity -> modelMapperPort.map(accountEntity, Account.class))))
                );
    }

    @Override
    public Mono<Account> statement(String numberAccount) {
        return repository.findByNumber(numberAccount)
                .flatMap(account ->  modelMapperPort.mapReactive(account, Account.class));
    }

    @Override
    public Mono<Void> updateAccountsBalance(Account accountFrom, Account accountTo) {
        return repository.updateAccountsBalance(accountFrom, accountTo);
    }

}
