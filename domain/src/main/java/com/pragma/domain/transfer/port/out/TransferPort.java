package com.pragma.domain.transfer.port.out;

import com.pragma.domain.commons.pageable.Pageable;
import com.pragma.domain.transfer.dto.Transfer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransferPort {

    Mono<Integer> makeTransfer(Transfer transfer);
    Flux<Transfer> getMovements(String numberAccount, Pageable pageable);

}
