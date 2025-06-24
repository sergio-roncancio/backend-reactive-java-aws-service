package com.pragma.r2dbc.transfer.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TransferRepository extends ReactiveCrudRepository<TransferEntity, Integer> {

    @Query(value = "SELECT * FROM transfer t "
                 + "WHERE t.account_from = :numberAccount OR "
                 + "t.account_to = :numberAccount ORDER BY t.date DESC "
                 + "LIMIT :#{#pageable.getPageSize()} OFFSET :#{#pageable.getOffset()}")
    Flux<TransferEntity> historicalTransfers(String numberAccount, Pageable pageable);

}
