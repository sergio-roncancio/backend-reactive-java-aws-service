package com.pragma.domain.transfer.request;

import java.math.BigDecimal;

public record RequestTransfer (String numberAccountFrom, String numberAccountTo, BigDecimal amount){}
