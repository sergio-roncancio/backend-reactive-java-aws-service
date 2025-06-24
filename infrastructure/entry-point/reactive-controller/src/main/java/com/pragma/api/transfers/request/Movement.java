package com.pragma.api.transfers.request;

import com.pragma.api.commons.pageable.Pageable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.pragma.api.validation.constants.ValidationConstants.ACCOUNT_NUMBER_INVALID;
import static com.pragma.api.validation.constants.ValidationConstants.GENERIC_EMPTY_FIELD_MESSAGE;

public record Movement (
        @NotNull(message = GENERIC_EMPTY_FIELD_MESSAGE)
        @Valid
        Pageable pageable,
        @NotBlank(message = GENERIC_EMPTY_FIELD_MESSAGE)
        @Size(min = 16, max = 16, message = ACCOUNT_NUMBER_INVALID)
        String numberAccount
) { }
