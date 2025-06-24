package com.pragma.domain.transfer.response;

import java.math.BigDecimal;

public record Source(
        String numberAccountFrom,
        BigDecimal currentBalance
){}
