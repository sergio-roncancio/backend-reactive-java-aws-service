package com.pragma.api.transfers;

import com.pragma.api.mapper.MapperToDomain;
import com.pragma.api.response.ResponseFactory;
import com.pragma.api.transfers.request.Movement;
import com.pragma.api.transfers.request.Transfer;
import com.pragma.api.validation.ValidationResquest;
import com.pragma.usecase.transfer.MovementsUseCase;
import com.pragma.usecase.transfer.TransferUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.pragma.domain.commons.enums.operation.Succesfull.OK_MOVEMENTS_LIST;
import static com.pragma.domain.commons.enums.operation.Succesfull.OK_TRANSACTION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@RestController
@RequiredArgsConstructor
public class TransferController {

    private final TransferUseCase transferAdapter;
    private final MovementsUseCase movementsUseCase;
    private final ResponseFactory responseFactory;
    private final ValidationResquest validationResquest;
    private final MapperToDomain mapperToDomain;

    public Mono<ServerResponse> makeTransfer(ServerRequest serverRequest){
        return serverRequest.bodyToMono(Transfer.class)
                        .flatMap(validationResquest::validate)
                        .flatMap(transfer -> Mono.just(mapperToDomain.transferToRequestTransfer(transfer)))
                        .flatMap(transferAdapter::makeTransfer)
                        .flatMap(transferResponse ->
                                status(CREATED).bodyValue(responseFactory.createResponse(transferResponse, OK_TRANSACTION)));
    }

    public Mono<ServerResponse> getMovements(ServerRequest serverRequest){
        return serverRequest.bodyToMono(Movement.class)
                .flatMap(validationResquest::validate)
                .flatMap(movement -> Mono.just(mapperToDomain.movementToRequestMovements(movement)))
                .flatMap(movementsUseCase::listMovements)
                .flatMap(movementsList ->
                        ok().bodyValue(responseFactory.createResponse(movementsList, OK_MOVEMENTS_LIST)));
    }

}
