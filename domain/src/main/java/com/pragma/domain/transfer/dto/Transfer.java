package com.pragma.domain.transfer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transfer(String numberAccountFrom, String numberAccountTo,
                       BigDecimal amount, LocalDateTime date) { }
