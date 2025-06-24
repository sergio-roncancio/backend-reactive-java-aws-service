package com.pragma.domain.transfer.response;

public record TransferResponse (
        int idTransfer,
        Source source,
        Destination destination
){}
