package com.pragma.domain.transfer.response.movement;

import java.math.BigDecimal;

public record AccountInfo (
    String numberAccount,
    BigDecimal currentBalance
){}
