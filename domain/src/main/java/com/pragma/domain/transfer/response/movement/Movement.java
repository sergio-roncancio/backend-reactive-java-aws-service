package com.pragma.domain.transfer.response.movement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Movement (
        String accountNumber,
        BigDecimal amount,
        LocalDateTime date
) { }
