package com.pragma.r2dbc.transfer.adapter;

import com.pragma.domain.commons.pageable.Pageable;
import com.pragma.domain.transfer.dto.Transfer;
import com.pragma.domain.transfer.port.out.TransferPort;
import com.pragma.r2dbc.transfer.repository.TransferEntity;
import com.pragma.r2dbc.transfer.repository.TransferRepository;
import com.pragma.domain.commons.modelmapper.ModelMapperPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferAdapterImpl implements TransferPort {

    private final TransferRepository repository;
    private final ModelMapperPort modelMapperPort;

    @Override
    public Mono<Integer> makeTransfer(Transfer createTransfer) {
        return modelMapperPort.mapReactive(createTransfer, TransferEntity.class)
                .flatMap(repository::save)
                .flatMap(transfer -> Mono.just(transfer.getId()));
    }

    @Override
    public Flux<Transfer> getMovements(String numberAccount, Pageable pageable) {
        return repository.historicalTransfers(numberAccount,
                PageRequest.of(pageable.pageNumber(), pageable.pageSize()))
                .flatMap(this::mapTransfer);
    }

    private Mono<Transfer> mapTransfer(TransferEntity transferEntity){
        return Mono.just(new Transfer(transferEntity.getNumberAccountFrom(), transferEntity.getNumberAccountTo(),
                transferEntity.getAmount(), transferEntity.getDate()));
    }

}
