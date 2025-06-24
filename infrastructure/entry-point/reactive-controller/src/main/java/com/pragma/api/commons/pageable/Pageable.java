package com.pragma.api.commons.pageable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import static com.pragma.api.validation.constants.ValidationConstants.GENERIC_EMPTY_FIELD_MESSAGE;
import static com.pragma.api.validation.constants.ValidationConstants.NEGATIVE_FIELD_MESSAGE;

public record Pageable (
        @NotNull(message = GENERIC_EMPTY_FIELD_MESSAGE)
        @Min(value = 0, message = NEGATIVE_FIELD_MESSAGE)
        Integer pageNumber,
        @NotNull(message = GENERIC_EMPTY_FIELD_MESSAGE)
        @Min(value = 0, message = NEGATIVE_FIELD_MESSAGE)
        @Max(value = 20, message = MAX_PAGE_SIZE_MESSAGE)
        Integer pageSize
) {
    private static final String MAX_PAGE_SIZE_MESSAGE = "Only possible to return up to a maximum of 20 elements";
}
