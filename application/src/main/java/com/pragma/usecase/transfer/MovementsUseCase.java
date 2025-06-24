package com.pragma.usecase.transfer;

import com.pragma.domain.account.Account;
import com.pragma.domain.commons.pageable.Pageable;
import com.pragma.domain.transfer.dto.Transfer;
import com.pragma.domain.transfer.port.out.TransferPort;
import com.pragma.domain.transfer.request.RequestMovement;
import com.pragma.domain.transfer.response.movement.AccountInfo;
import com.pragma.domain.transfer.response.movement.Movement;
import com.pragma.domain.transfer.response.movement.MovementResponse;
import com.pragma.usecase.account.AccountUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MovementsUseCase {

    private final TransferPort transferPort;
    private final AccountUseCase accountUseCase;

    private static final BigDecimal TRANSFER_MOVEMENT = BigDecimal.valueOf(-1);

    public Mono<MovementResponse> listMovements(RequestMovement requestMovement){
        return accountUseCase.statement(requestMovement.numberAccount())
                .flatMap(account ->
                        transferPort.getMovements(account.getNumber(), requestMovement.pageable())
                                .collectList()
                                .flatMap(listMovements ->
                                        createMovementsResponse(account, listMovements, requestMovement.pageable()))
                );
    }

    private Mono<MovementResponse> createMovementsResponse(Account account,
                                                           List<Transfer> movementList,
                                                           Pageable pageable){
        var accountInfo = new AccountInfo(account.getNumber(), account.getBalance());
        var movements = new ArrayList<Movement>();
        for (Transfer transfer: movementList){
            String accountMovement;
            BigDecimal amount;
            if(transfer.numberAccountFrom().equals(account.getNumber())){
                accountMovement = transfer.numberAccountTo();
                amount = transfer.amount().multiply(TRANSFER_MOVEMENT);
            }else {
                accountMovement = transfer.numberAccountFrom();
                amount = transfer.amount();
            }
            movements.add(new Movement(accountMovement, amount,transfer.date()));
        }
        return Mono.just(new MovementResponse(accountInfo, movements, pageable));
    }

}
