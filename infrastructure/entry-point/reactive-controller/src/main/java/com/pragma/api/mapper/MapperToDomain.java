package com.pragma.api.mapper;

import com.pragma.api.transfers.request.Movement;
import com.pragma.api.transfers.request.Transfer;
import com.pragma.domain.transfer.request.RequestMovement;
import com.pragma.domain.transfer.request.RequestTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MapperToDomain {

    RequestTransfer transferToRequestTransfer(Transfer transfer);
    RequestMovement movementToRequestMovements(Movement movement);

}
