package com.pragma.api.transfers.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

import static com.pragma.api.validation.constants.ValidationConstants.*;

public record Transfer (

    @NotBlank(message = GENERIC_EMPTY_FIELD_MESSAGE)
    @Size(min = 16, max = 16, message = ACCOUNT_NUMBER_INVALID)
    String numberAccountFrom,
    @NotBlank(message = GENERIC_EMPTY_FIELD_MESSAGE)
    @Size(min = 16, max = 16, message = ACCOUNT_NUMBER_INVALID)
    String numberAccountTo,
    @NotNull(message = GENERIC_EMPTY_FIELD_MESSAGE)
    @Digits(integer = 12, fraction = 2, message = AMOUNT_LIMIT_INVALID)
    @DecimalMin(value = "0.0", inclusive = false, message = AMOUNT_TRANSFER_INVALID)
    BigDecimal amount

){
    public static final String AMOUNT_LIMIT_INVALID = "Amount to be transferred not valid";
    public static final String AMOUNT_TRANSFER_INVALID = "Amount to be transferred must be greater than 0";
}
