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

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountAdapterImpl implements AccountPort {

    private final AccountRepository repository;
    private final ModelMapperPort modelMapperPort;

    @Override
    public Mono<Map<String, Account>> findAccountsByNumbers(List<String> numbers) {
        if(numbers == null || numbers.isEmpty()) {
            return Mono.just(emptyMap());
        }
        return repository.findAccountsByNumbers(numbers)
                .collectMap(
                        AccountEntity::getNumber,
                        accountEntity -> modelMapperPort.map(accountEntity, Account.class)
                );
    }

    @Override
    public Mono<Account> statement(String numberAccount) {
        if(isBlank(numberAccount)){
            return Mono.empty();
        }
        return repository.findByNumber(numberAccount)
                .flatMap(account ->  modelMapperPort.mapReactive(account, Account.class));
    }

    @Override
    public Mono<Void> updateAccountsBalance(Account accountFrom, Account accountTo) {
        if(accountTo == null || accountFrom == null || accountFrom.equals(accountTo)) {
            return Mono.error(new IllegalArgumentException("Must send both the destination account and the source account"));
        }
        return repository.updateAccountsBalance(accountFrom, accountTo);
    }

}
