package com.pragma.domain.transfer.response;

import java.math.BigDecimal;

public record Destination(
        String numberAccountTo,
        BigDecimal transferValue
){}
